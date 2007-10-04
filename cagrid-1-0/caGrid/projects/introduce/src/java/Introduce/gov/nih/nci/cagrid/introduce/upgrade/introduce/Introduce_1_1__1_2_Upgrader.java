package gov.nih.nci.cagrid.introduce.upgrade.introduce;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.common.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.common.ServiceConstantsTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.ResourceBaseTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.ResourceHomeTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.ResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.SingletonResourceHomeTemplate;
import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.StatusBase;
import gov.nih.nci.cagrid.introduce.upgrade.one.one.IntroduceUpgraderBase;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;


public class Introduce_1_1__1_2_Upgrader extends IntroduceUpgraderBase {

    public Introduce_1_1__1_2_Upgrader(IntroduceUpgradeStatus status, ServiceInformation serviceInformation,
        String servicePath) {
        super(status, serviceInformation, servicePath, "1.1", "1.2");
    }


    protected void upgrade() throws Exception {
        

        // foreach service need to replace the resource files.....
        File srcDir = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() + File.separator + "src");
        for (int i = 0; i < getServiceInformation().getServices().getService().length; i++) {
            ServiceType service = getServiceInformation().getServices().getService(i);

            if (service.getResourceFrameworkOptions().getCustom() == null) {

                // delete the old base resource
                File oldbaseResourceF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "BaseResource.java");
                oldbaseResourceF.delete();

                ServiceConstantsTemplate resourceContanstsT = new ServiceConstantsTemplate();
                String resourceContanstsS = resourceContanstsT.generate(new SpecificServiceInformation(
                    getServiceInformation(), service));
                File resourceContanstsF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "common" + File.separator + File.separator + service.getName() + "Constants.java");

                FileWriter resourceContanstsFW = new FileWriter(resourceContanstsF);
                resourceContanstsFW.write(resourceContanstsS);
                resourceContanstsFW.close();

                ResourceBaseTemplate baseResourceBaseT = new ResourceBaseTemplate();
                String baseResourceBaseS = baseResourceBaseT.generate(new SpecificServiceInformation(
                    getServiceInformation(), service));
                File baseResourceBaseF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "BaseResourceBase.java");

                FileWriter baseResourceBaseFW = new FileWriter(baseResourceBaseF);
                baseResourceBaseFW.write(baseResourceBaseS);
                baseResourceBaseFW.close();

                ResourceTemplate baseResourceT = new ResourceTemplate();
                String baseResourceS = baseResourceT.generate(new SpecificServiceInformation(getServiceInformation(),
                    service));
                File baseResourceF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + service.getName() + "Resource.java");

                FileWriter baseResourceFW = new FileWriter(baseResourceF);
                baseResourceFW.write(baseResourceS);
                baseResourceFW.close();

                if (service.getResourceFrameworkOptions().getSingleton() != null) {
                    SingletonResourceHomeTemplate baseResourceHomeT = new SingletonResourceHomeTemplate();
                    String baseResourceHomeS = baseResourceHomeT.generate(new SpecificServiceInformation(
                        getServiceInformation(), service));
                    File baseResourceHomeF = new File(srcDir.getAbsolutePath() + File.separator
                        + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                        + File.separator + "resource" + File.separator + "BaseResourceHome.java");

                    FileWriter baseResourceHomeFW = new FileWriter(baseResourceHomeF);
                    baseResourceHomeFW.write(baseResourceHomeS);
                    baseResourceHomeFW.close();

                } else {

                    ResourceHomeTemplate baseResourceHomeT = new ResourceHomeTemplate();
                    String baseResourceHomeS = baseResourceHomeT.generate(new SpecificServiceInformation(
                        getServiceInformation(), service));
                    File baseResourceHomeF = new File(srcDir.getAbsolutePath() + File.separator
                        + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                        + File.separator + "resource" + File.separator + "BaseResourceHome.java");

                    FileWriter baseResourceHomeFW = new FileWriter(baseResourceHomeF);
                    baseResourceHomeFW.write(baseResourceHomeS);
                    baseResourceHomeFW.close();

                }

                getStatus().addDescriptionLine("replaced resource source files for service: " + service.getName());
            }
        }

        upgradeJars();
        getStatus().addDescriptionLine("updating service with the new version of the jars");

     
        getStatus().setStatus(StatusBase.UPGRADE_OK);
    }


    private final class OldJarsFilter implements FileFilter {
        boolean hadGridGrouperJars = false;
        boolean hadCSMJars = false;


        public boolean accept(File name) {
            String filename = name.getName();
            boolean core = filename.startsWith("caGrid-1.0-core") && filename.endsWith(".jar");
            boolean security = (filename.startsWith("caGrid-1.0-ServiceSecurityProvider") || filename
                .startsWith("caGrid-1.0-metadata-security"))
                && filename.endsWith(".jar");
            boolean gridGrouper = (filename.startsWith("caGrid-1.0-gridgrouper")) && filename.endsWith(".jar");
            if (gridGrouper) {
                hadGridGrouperJars = true;
            }
            boolean csm = (filename.startsWith("caGrid-1.0-authz-common")) && filename.endsWith(".jar");
            if (csm) {
                hadCSMJars = true;
            }

            boolean otherSecurityJarsNotNeeded = (filename.startsWith("caGrid-1.0-gridca"))
                && filename.endsWith(".jar");

            boolean wsrf = (filename.startsWith("globus_wsrf_mds") || filename.startsWith("globus_wsrf_servicegroup"))
                && filename.endsWith(".jar");
            boolean mobius = filename.startsWith("mobius") && filename.endsWith(".jar");

            return core || security || gridGrouper || csm || wsrf || mobius || otherSecurityJarsNotNeeded;
        }

    };


    private void upgradeJars() throws Exception {

        OldJarsFilter oldDkeletonLibFilter = new OldJarsFilter();

        // locate the old libs in the service
        File serviceLibDir = new File(getServicePath() + File.separator + "lib");
        File[] serviceLibs = serviceLibDir.listFiles(oldDkeletonLibFilter);
        // delete the old libraries
        for (int i = 0; i < serviceLibs.length; i++) {
            serviceLibs[i].delete();
            getStatus().addDescriptionLine(serviceLibs[i].getName() + " removed");
        }

        FileFilter srcSkeletonLibFilter = new FileFilter() {
            public boolean accept(File name) {
                String filename = name.getName();
                return filename.endsWith(".jar");
            }
        };

        File skeletonLibDir = new File("skeleton" + File.separator + "lib");
        File extLibDir = new File("ext" + File.separator + "lib");
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
            // need to add in the optional grouper security jars
            File[] gridGrouperLibs = grouperLibDir.listFiles();
            for (int i = 0; i < gridGrouperLibs.length; i++) {
                File out = new File(serviceLibDir.getAbsolutePath() + File.separator + gridGrouperLibs[i].getName());
                try {
                    Utils.copyFile(gridGrouperLibs[i], out);
                    getStatus().addDescriptionLine(gridGrouperLibs[i].getName() + " added");
                } catch (IOException ex) {
                    throw new Exception("Error copying library (" + gridGrouperLibs[i] + ") to service: "
                        + ex.getMessage(), ex);
                }
            }
        }

        if (oldDkeletonLibFilter.hadCSMJars) {
            // need to add in the CSM security jars
            File[] gridCSMLibs = csmLibDir.listFiles();
            for (int i = 0; i < gridCSMLibs.length; i++) {
                File out = new File(serviceLibDir.getAbsolutePath() + File.separator + gridCSMLibs[i].getName());
                try {
                    Utils.copyFile(gridCSMLibs[i], out);
                    getStatus().addDescriptionLine(gridCSMLibs[i].getName() + " added");
                } catch (IOException ex) {
                    throw new Exception(
                        "Error copying library (" + gridCSMLibs[i] + ") to service: " + ex.getMessage(), ex);
                }
            }
        }

    }

}
