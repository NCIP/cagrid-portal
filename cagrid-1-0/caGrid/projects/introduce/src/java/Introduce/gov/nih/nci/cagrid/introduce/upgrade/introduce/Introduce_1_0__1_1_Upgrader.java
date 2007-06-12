package gov.nih.nci.cagrid.introduce.upgrade.introduce;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.common.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;
import gov.nih.nci.cagrid.introduce.templates.service.globus.ServiceConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.ResourceConstantsTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.base.BaseResourceBaseTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.base.BaseResourceHomeTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.base.BaseResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.lifetime.LifetimeResourceBaseTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.lifetime.LifetimeResourceHomeTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.lifetime.LifetimeResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.main.MainConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.main.MainResourceHomeTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.main.MainResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.singleton.SingletonResourceBaseTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.singleton.SingletonResourceHomeTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.singleton.SingletonResourceTemplate;
import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.StatusBase;
import gov.nih.nci.cagrid.introduce.upgrade.one.one.IntroduceUpgraderBase;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.projectmobius.common.XMLUtilities;


public class Introduce_1_0__1_1_Upgrader extends IntroduceUpgraderBase {

    public Introduce_1_0__1_1_Upgrader(IntroduceUpgradeStatus status,ServiceInformation serviceInformation, String servicePath) {
        super(status,serviceInformation, servicePath, "1.0", "1.1");
    }


    protected void upgrade() throws Exception {
        // need to delete the old registration.xml file
        File registrationFile = new File(getServicePath() + File.separator + "etc" + File.separator + "registration.xml");
        registrationFile.delete();
        getStatus().addDescriptionLine("removed old registration.xml file, it is now replaced with service specific one for each service");

        // need to replace the build.xml
        Utils.copyFile(new File(getServicePath() + File.separator + "build.xml"), new File(getServicePath()
            + File.separator + "build.xml.OLD"));
        Utils.copyFile(new File("." + File.separator + "skeleton" + File.separator + "build.xml"), new File(
            getServicePath() + File.separator + "build.xml"));
        Utils.copyFile(new File("." + File.separator + "skeleton" + File.separator + "dev-build.xml"), new File(
            getServicePath() + File.separator + "dev-build.xml"));
        getStatus().addDescriptionLine("replaced build.xml with new version");
        getStatus().addIssue("Replaced the build.xml file.", "Put any additions you need to the service build in the dev-build.xml file which has now been created.");

        // need to replace the build-deploy.xml
        Utils.copyFile(new File(getServicePath() + File.separator + "build-deploy.xml"), new File(getServicePath()
            + File.separator + "build-deploy.xml.OLD"));
        Utils.copyFile(new File("." + File.separator + "skeleton" + File.separator + "build-deploy.xml"), new File(
            getServicePath() + File.separator + "build-deploy.xml"));
        Utils.copyFile(new File("." + File.separator + "skeleton" + File.separator + "dev-build-deploy.xml"), new File(
            getServicePath() + File.separator + "dev-build-deploy.xml"));
        getStatus().addDescriptionLine("replaced build-deploy.xml with new version");
        getStatus().addIssue("Replaced the build-deploy.xml file.", "Put any additions you need to the service deployment in the dev-build-deploy.xml file which has now been created.");

        
        

        //foreach service need to replace the resource files.....
        File srcDir = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() + File.separator + "src");
        for(int i = 0 ; i < getServiceInformation().getServices().getService().length; i ++){
            ServiceType service = getServiceInformation().getServices().getService(i);
            
            //delete the old base resource
            File oldbaseResourceF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "BaseResource.java");
            oldbaseResourceF.delete();
            
           
            MainConfigurationTemplate metadataConfigurationT = new MainConfigurationTemplate();
            String metadataConfigurationS = metadataConfigurationT.generate(new SpecificServiceInformation(getServiceInformation(),
                service));
            File metadataConfigurationF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "ResourceConfiguration.java");

            FileWriter metadataConfigurationFW = new FileWriter(metadataConfigurationF);
            metadataConfigurationFW.write(metadataConfigurationS);
            metadataConfigurationFW.close();

            if (service.getResourceFrameworkType().equals(IntroduceConstants.INTRODUCE_BASE_RESOURCE)) {
                ResourceConstantsTemplate resourceContanstsT = new ResourceConstantsTemplate();
                String resourceContanstsS = resourceContanstsT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File resourceContanstsF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "ResourceConstants.java");

                FileWriter resourceContanstsFW = new FileWriter(resourceContanstsF);
                resourceContanstsFW.write(resourceContanstsS);
                resourceContanstsFW.close();

                BaseResourceBaseTemplate baseResourceBaseT = new BaseResourceBaseTemplate();
                String baseResourceBaseS = baseResourceBaseT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File baseResourceBaseF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "BaseResourceBase.java");

                FileWriter baseResourceBaseFW = new FileWriter(baseResourceBaseF);
                baseResourceBaseFW.write(baseResourceBaseS);
                baseResourceBaseFW.close();

                BaseResourceTemplate baseResourceT = new BaseResourceTemplate();
                String baseResourceS = baseResourceT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File baseResourceF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + service.getName() + "Resource.java");

                FileWriter baseResourceFW = new FileWriter(baseResourceF);
                baseResourceFW.write(baseResourceS);
                baseResourceFW.close();

                BaseResourceHomeTemplate baseResourceHomeT = new BaseResourceHomeTemplate();
                String baseResourceHomeS = baseResourceHomeT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File baseResourceHomeF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "BaseResourceHome.java");

                FileWriter baseResourceHomeFW = new FileWriter(baseResourceHomeF);
                baseResourceHomeFW.write(baseResourceHomeS);
                baseResourceHomeFW.close();

            } else if (service.getResourceFrameworkType().equals(IntroduceConstants.INTRODUCE_LIFETIME_RESOURCE)) {
                ResourceConstantsTemplate resourceContanstsT = new ResourceConstantsTemplate();
                String resourceContanstsS = resourceContanstsT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File resourceContanstsF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "ResourceConstants.java");

                FileWriter resourceContanstsFW = new FileWriter(resourceContanstsF);
                resourceContanstsFW.write(resourceContanstsS);
                resourceContanstsFW.close();

                LifetimeResourceBaseTemplate lifetimeResourceBaseT = new LifetimeResourceBaseTemplate();
                String lifetimeResourceBaseS = lifetimeResourceBaseT
                    .generate(new SpecificServiceInformation(getServiceInformation(), service));
                File lifetimeResourceBaseF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "BaseResourceBase.java");

                FileWriter lifetimeResourceBaseFW = new FileWriter(lifetimeResourceBaseF);
                lifetimeResourceBaseFW.write(lifetimeResourceBaseS);
                lifetimeResourceBaseFW.close();

                LifetimeResourceTemplate lifetimeResourceT = new LifetimeResourceTemplate();
                String lifetimeResourceS = lifetimeResourceT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File lifetimeResourceF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + service.getName() + "Resource.java");

                FileWriter lifetimeResourceFW = new FileWriter(lifetimeResourceF);
                lifetimeResourceFW.write(lifetimeResourceS);
                lifetimeResourceFW.close();

                LifetimeResourceHomeTemplate lifetimeResourceHomeT = new LifetimeResourceHomeTemplate();
                String lifetimeResourceHomeS = lifetimeResourceHomeT
                    .generate(new SpecificServiceInformation(getServiceInformation(), service));
                File lifetimeResourceHomeF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "BaseResourceHome.java");

                FileWriter lifetimeResourceHomeFW = new FileWriter(lifetimeResourceHomeF);
                lifetimeResourceHomeFW.write(lifetimeResourceHomeS);
                lifetimeResourceHomeFW.close();

            } else if (service.getResourceFrameworkType().equals(IntroduceConstants.INTRODUCE_SINGLETON_RESOURCE)) {
                ResourceConstantsTemplate resourceContanstsT = new ResourceConstantsTemplate();
                String resourceContanstsS = resourceContanstsT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File resourceContanstsF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "ResourceConstants.java");

                FileWriter resourceContanstsFW = new FileWriter(resourceContanstsF);
                resourceContanstsFW.write(resourceContanstsS);
                resourceContanstsFW.close();

                SingletonResourceTemplate baseResourceT = new SingletonResourceTemplate();
                String baseResourceS = baseResourceT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File baseResourceF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + service.getName() + "Resource.java");

                FileWriter baseResourceFW = new FileWriter(baseResourceF);
                baseResourceFW.write(baseResourceS);
                baseResourceFW.close();

                SingletonResourceBaseTemplate baseResourceBaseT = new SingletonResourceBaseTemplate();
                String baseResourceBaseS = baseResourceBaseT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File baseResourceBaseF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "BaseResourceBase.java");

                FileWriter baseResourceBaseFW = new FileWriter(baseResourceBaseF);
                baseResourceBaseFW.write(baseResourceBaseS);
                baseResourceBaseFW.close();

                SingletonResourceHomeTemplate baseResourceHomeT = new SingletonResourceHomeTemplate();
                String baseResourceHomeS = baseResourceHomeT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File baseResourceHomeF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "BaseResourceHome.java");

                FileWriter baseResourceHomeFW = new FileWriter(baseResourceHomeF);
                baseResourceHomeFW.write(baseResourceHomeS);
                baseResourceHomeFW.close();

            } else if (service.getResourceFrameworkType().equals(IntroduceConstants.INTRODUCE_MAIN_RESOURCE)) {
                ResourceConstantsTemplate resourceContanstsT = new ResourceConstantsTemplate();
                String resourceContanstsS = resourceContanstsT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File resourceContanstsF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "ResourceConstants.java");

                FileWriter resourceContanstsFW = new FileWriter(resourceContanstsF);
                resourceContanstsFW.write(resourceContanstsS);
                resourceContanstsFW.close();

                ServiceConfigurationTemplate serviceConfT = new ServiceConfigurationTemplate();
                String serviceConfS = serviceConfT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File serviceConfF = new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service)
                    + File.separator + "service" + File.separator + "ServiceConfiguration.java");

                FileWriter serviceConfFW = new FileWriter(serviceConfF);
                serviceConfFW.write(serviceConfS);
                serviceConfFW.close();

                MainResourceTemplate baseResourceT = new MainResourceTemplate();
                String baseResourceS = baseResourceT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File baseResourceF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + service.getName() + "Resource.java");

                FileWriter baseResourceFW = new FileWriter(baseResourceF);
                baseResourceFW.write(baseResourceS);
                baseResourceFW.close();

                MainResourceHomeTemplate baseResourceHomeT = new MainResourceHomeTemplate();
                String baseResourceHomeS = baseResourceHomeT.generate(new SpecificServiceInformation(getServiceInformation(), service));
                File baseResourceHomeF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "BaseResourceHome.java");

                FileWriter baseResourceHomeFW = new FileWriter(baseResourceHomeF);
                baseResourceHomeFW.write(baseResourceHomeS);
                baseResourceHomeFW.close();

            }
            getStatus().addDescriptionLine("replaced resource source files for service: " + service.getName());
        }
        
        
        // need to add to the deploy.properties
        Properties deployProperties = new Properties();
        deployProperties.load(new FileInputStream(new File(getServicePath() + File.separator
            + IntroduceConstants.DEPLOY_PROPERTIES_FILE)));
        deployProperties.put("index.service.registration.refresh_seconds", "600");
        deployProperties.put("index.service.index.refresh_milliseconds", "30000");
        deployProperties.put("perform.index.service.registration", "true");
        deployProperties.store(new FileOutputStream(new File(getServicePath() + File.separator
            + IntroduceConstants.DEPLOY_PROPERTIES_FILE)), "Service Deployment Properties");
        getStatus().addDescriptionLine("adding new deployment variables to deploy.properties");
        
        // need to add the new template var to the jndi so that it can be
        // replaced
        // by the new property for shutting off registration
        Document jndiDoc = XMLUtilities.fileNameToDocument(getServicePath() + File.separator + "jndi-config.xml");
        List services = jndiDoc.getRootElement().getChildren("service",
            Namespace.getNamespace("http://wsrf.globus.org/jndi/config"));
        Iterator serviceI = services.iterator();
        while (serviceI.hasNext()) {
            Element service = (Element) serviceI.next();
            List resources = service.getChildren("resource", Namespace
                .getNamespace("http://wsrf.globus.org/jndi/config"));
            Iterator resourceI = resources.iterator();
            while (resourceI.hasNext()) {
                Element resource = (Element) resourceI.next();
                List parameters = resource.getChild("resourceParams",
                    Namespace.getNamespace("http://wsrf.globus.org/jndi/config")).getChildren("parameter",
                    Namespace.getNamespace("http://wsrf.globus.org/jndi/config"));
                Iterator parameterI = parameters.iterator();
                while (parameterI.hasNext()) {
                    Element parameter = (Element) parameterI.next();
                    if (parameter.getChild("name", Namespace.getNamespace("http://wsrf.globus.org/jndi/config"))
                        .getText().equals("performRegistration")) {
                        parameter.getChild("value", Namespace.getNamespace("http://wsrf.globus.org/jndi/config"))
                            .setText("PERFORM_REGISTRATION");
                    }

                }
            }
        }
        getStatus().addDescriptionLine("retemplated jndi file");

        // need to add the soapFix.jar to the tools lib directory
        Utils.copyFile(new File("." + File.separator + "skeleton" + File.separator + "tools" + File.separator + "lib"
            + File.separator + "caGrid-1.1-Introduce-1.1-soapBindingFix.jar"), new File(getServicePath()
            + File.separator + "tools" + File.separator + "lib" + File.separator
            + "caGrid-1.1-Introduce-1.1-soapBindingFix.jar"));
        getStatus().addDescriptionLine("added soapFix jar to enable patching the soap bindings that get generated for custom beans");
        
        // need to move the ant-contrib.jar to the tools lib directory
        File currentContribFile = new File(getServicePath()
            + File.separator + "lib" + File.separator
            + "ant-contrib.jar");
        Utils.copyFile(currentContribFile , new File(getServicePath()
            + File.separator + "tools" + File.separator + "lib" + File.separator
            + "ant-contrib.jar"));
        currentContribFile.delete();
        getStatus().addDescriptionLine("moving ant-contrib to a location which will not cause it to be deployed with the service");
        

        upgradeJars();
        getStatus().addDescriptionLine("updating service with the new version of the jars");
        
        fixUpSchemaLocationsInModel();

        // recreate the authorization class
        // recreate the service descriptor
        // recreate the provider impl
        // those will be done during the resync
        
        getStatus().setStatus(StatusBase.UPGRADE_OK);
    }


    private void upgradeJars() throws Exception {

        FileFilter oldDkeletonLibFilter = new FileFilter() {
            public boolean accept(File name) {
                String filename = name.getName();
                boolean core = filename.startsWith("caGrid-1.0-core") && filename.endsWith(".jar");
                boolean security = (filename.startsWith("caGrid-1.0-ServiceSecurityProvider") || filename
                    .startsWith("caGrid-1.0-metadata-security"))
                    && filename.endsWith(".jar");
                boolean wsrf = (filename.startsWith("globus_wsrf_mds") || filename
                    .startsWith("globus_wsrf_servicegroup"))
                    && filename.endsWith(".jar");
                boolean mobius = filename.startsWith("mobius") && filename.endsWith(".jar");
                return core || security || wsrf || mobius;
            }
        };

        // locate the old libs in the service
        File serviceLibDir = new File(getServicePath() + File.separator + "lib");
        File[] serviceLibs = serviceLibDir.listFiles(oldDkeletonLibFilter);
        // delete the old libraries
        for (int i = 0; i < serviceLibs.length; i++) {
            serviceLibs[i].delete();
        }

        FileFilter srcSkeletonLibFilter = new FileFilter() {
            public boolean accept(File name) {
                String filename = name.getName();
                return filename.endsWith(".jar");
            }
        };
        // copy new libraries in (every thing in skeleton/lib)
        File skeletonLibDir = new File("skeleton" + File.separator + "lib");
        File[] skeletonLibs = skeletonLibDir.listFiles(srcSkeletonLibFilter);
        File[] outLibs = new File[skeletonLibs.length];
        for (int i = 0; i < skeletonLibs.length; i++) {
            File out = new File(serviceLibDir.getAbsolutePath() + File.separator + skeletonLibs[i].getName());
            try {
                Utils.copyFile(skeletonLibs[i], out);
            } catch (IOException ex) {
                throw new Exception("Error copying library (" + skeletonLibs[i] + ") to service: " + ex.getMessage(),
                    ex);
            }
            outLibs[i] = out;
        }
        // update the Eclipse .classpath file
        File classpathFile = new File(getServicePath() + File.separator + ".classpath");
        try {
            ExtensionUtilities.syncEclipseClasspath(classpathFile, outLibs);
        } catch (Exception ex) {
            ex.printStackTrace();
            // throw new Exception("Error updating Eclipse .classpath file:
            // " + ex.getMessage(), ex);
        }
    }
    
    
    private void fixUpSchemaLocationsInModel() {
        boolean editsMade = false;
        NamespacesType namespaces = getServiceInformation().getServiceDescriptor().getNamespaces();
        if (namespaces != null && namespaces.getNamespace() != null) {
            for (NamespaceType namespace : namespaces.getNamespace()) {
                String schemaLocation = namespace.getLocation();
                if (schemaLocation != null && schemaLocation.indexOf('\\') != -1) {
                    String cleanLocation = schemaLocation.replace('\\', '/');
                    namespace.setLocation(cleanLocation);
                    getStatus().addDescriptionLine("Edited schema location of namespace " 
                        + namespace.getNamespace());
                    getStatus().addDescriptionLine("\t(" + schemaLocation + " -> " + cleanLocation + ")");
                    editsMade = true;
                }
            }
        }
        if (editsMade) {
            getStatus().addIssue("Schema locations may not be platform independent", 
                "Please ensure that all schema locations are specified using forward slash(/) as file separators.");
        }
    }
}
