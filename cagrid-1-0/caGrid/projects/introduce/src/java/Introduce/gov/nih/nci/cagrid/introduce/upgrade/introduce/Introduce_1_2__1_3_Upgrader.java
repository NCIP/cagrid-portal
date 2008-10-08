package gov.nih.nci.cagrid.introduce.upgrade.introduce;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.common.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.common.ServiceConstantsBaseTemplate;
import gov.nih.nci.cagrid.introduce.templates.common.ServiceConstantsTemplate;
import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.StatusBase;
import gov.nih.nci.cagrid.introduce.upgrade.one.x.IntroduceUpgraderBase;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;


public class Introduce_1_2__1_3_Upgrader extends IntroduceUpgraderBase {

    public Introduce_1_2__1_3_Upgrader(IntroduceUpgradeStatus status, ServiceInformation serviceInformation,
        String servicePath) throws Exception {
        super(status, serviceInformation, servicePath, "1.2", "1.3");
    }


    private final class OldJarsFilter implements FileFilter {
        boolean hadGridGrouperJars = false;
        boolean hadCSMJars = false;


        public boolean accept(File name) {
            String filename = name.getName();
            boolean core = filename.startsWith("caGrid-core") && filename.endsWith(".jar");
            boolean advertisement = filename.startsWith("caGrid-advertisement") && filename.endsWith(".jar");
            boolean introduce = filename.startsWith("caGrid-Introduce") && filename.endsWith(".jar");
            boolean security = (filename.startsWith("caGrid-ServiceSecurityProvider") || filename
                .startsWith("caGrid-metadata-security"))
                && filename.endsWith(".jar");

            boolean gridGrouper = (filename.startsWith("caGrid-gridgrouper")) && filename.endsWith(".jar");
            if (gridGrouper) {
                hadGridGrouperJars = true;
            }
            boolean csm = (filename.startsWith("caGrid-authz-common")) && filename.endsWith(".jar");
            if (csm) {
                hadCSMJars = true;
            }

            boolean otherSecurityJarsNotNeeded = (filename.startsWith("caGrid-gridca")) && filename.endsWith(".jar");

            boolean wsrf = (filename.startsWith("globus_wsrf_mds") || filename.startsWith("globus_wsrf_servicegroup"))
                && filename.endsWith(".jar");
            boolean mobius = filename.startsWith("mobius") && filename.endsWith(".jar");

            return core || advertisement || introduce || security || gridGrouper || csm || wsrf || mobius
                || otherSecurityJarsNotNeeded;
        }

    };


    protected void upgrade() throws Exception {

        upgradeJars();
        fixConstants();
        fixWSDD();

        getStatus().setStatus(StatusBase.UPGRADE_OK);
    }
    
    protected void fixConstants() throws Exception {
        File srcDir = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() + File.separator + "src");
        for(int serviceI = 0; serviceI < getServiceInformation().getServices().getService().length; serviceI++){
            ServiceType service = getServiceInformation().getServices().getService(serviceI);
            //add new constants base class and new constants class
            ServiceConstantsTemplate resourceContanstsT = new ServiceConstantsTemplate();
            String resourceContanstsS = resourceContanstsT.generate(new SpecificServiceInformation(
                getServiceInformation(), service));
            File resourceContanstsF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "common" + File.separator + File.separator
                + service.getName() + "Constants.java");

            FileWriter resourceContanstsFW = new FileWriter(resourceContanstsF);
            resourceContanstsFW.write(resourceContanstsS);
            resourceContanstsFW.close();
            
            ServiceConstantsBaseTemplate resourcebContanstsT = new ServiceConstantsBaseTemplate();
            String resourcebContanstsS = resourcebContanstsT.generate(new SpecificServiceInformation(getServiceInformation(), service));
            File resourcebContanstsF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "common" + File.separator + service.getName() + "ConstantsBase.java");

            FileWriter resourcebContanstsFW = new FileWriter(resourcebContanstsF);
            resourcebContanstsFW.write(resourcebContanstsS);
            resourcebContanstsFW.close(); 
        }
    }


    protected void fixWSDD() throws Exception {
        Document doc = XMLUtilities.fileNameToDocument(getServiceInformation().getBaseDirectory() + File.separator
            + "server-config.wsdd");
        List servicesEls = doc.getRootElement().getChildren("service",
            Namespace.getNamespace("http://xml.apache.org/axis/wsdd/"));
        for (int serviceI = 0; serviceI < servicesEls.size(); serviceI++) {
            Element serviceEl = (Element) servicesEls.get(serviceI);
            ServiceType service = CommonTools.getService(getServiceInformation().getServices(), serviceEl.getAttributeValue("name").substring(serviceEl.getAttributeValue("name").lastIndexOf("/")+1));

            // need to add the service name att and the etc path att for each service
            Element serviceName = new Element("parameter",Namespace.getNamespace("http://xml.apache.org/axis/wsdd/"));
            serviceName.setAttribute("name",service.getName().toLowerCase()+"-serviceName");
            serviceName.setAttribute("value",service.getName());
            
            Element serviceETC = new Element("parameter",Namespace.getNamespace("http://xml.apache.org/axis/wsdd/"));
            serviceETC.setAttribute("name",service.getName().toLowerCase() +"-etcDirectoryPath");
            serviceETC.setAttribute("value","ETC-PATH");
            
            serviceEl.addContent(serviceName);
            serviceEl.addContent(serviceETC);

        }

        FileWriter fw = new FileWriter(getServiceInformation().getBaseDirectory() + File.separator
            + "server-config.wsdd");
        fw.write(XMLUtilities.formatXML(XMLUtilities.documentToString(doc)));
        fw.close();
    }


    private void upgradeJars() throws Exception {

        OldJarsFilter oldDkeletonLibFilter = new OldJarsFilter();

        // locate the old libs in the service
        File serviceLibDir = new File(getServicePath() + File.separator + "lib");
        File[] serviceLibs = serviceLibDir.listFiles(oldDkeletonLibFilter);
        // delete the old libraries
        for (int i = 0; i < serviceLibs.length; i++) {
            boolean deleted = serviceLibs[i].delete();
            if (deleted) {
                getStatus().addDescriptionLine(serviceLibs[i].getName() + " removed");
            } else {
                getStatus().addDescriptionLine(serviceLibs[i].getName() + " could not be removed");
            }
        }

        FileFilter srcSkeletonLibFilter = new FileFilter() {
            public boolean accept(File name) {
                String filename = name.getName();
                return filename.endsWith(".jar");
            }
        };

        File skeletonLibDir = new File("skeleton" + File.separator + "lib");
        // File extLibDir = new File("ext" + File.separator + "lib");
        File csmLibDir = new File("ext" + File.separator + "skeleton" + File.separator + "csm" + File.separator + "lib");
        File grouperLibDir = new File("ext" + File.separator + "skeleton" + File.separator + "gridgrouper"
            + File.separator + "lib");

        // copy new libraries in (every thing in skeleton/lib)
        File[] skeletonLibs = skeletonLibDir.listFiles(srcSkeletonLibFilter);
        for (int i = 0; i < skeletonLibs.length; i++) {
            File out = new File(serviceLibDir.getAbsolutePath() + File.separator + skeletonLibs[i].getName());
            try {
                Utils.copyFile(skeletonLibs[i], out);
                getStatus().addDescriptionLine(skeletonLibs[i].getName() + " added");
            } catch (IOException ex) {
                throw new Exception("Error copying library (" + skeletonLibs[i] + ") to service: " + ex.getMessage(),
                    ex);
            }
        }

        if (oldDkeletonLibFilter.hadGridGrouperJars) {
            // TODO
        }

        if (oldDkeletonLibFilter.hadCSMJars) {
            // TODO
        }

    }

}
