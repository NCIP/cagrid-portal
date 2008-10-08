package gov.nih.nci.cagrid.introduce.upgrade.introduce;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.service.ResourcePropertyManagement;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.common.SynchronizationException;
import gov.nih.nci.cagrid.introduce.codegen.provider.ProviderTools;
import gov.nih.nci.cagrid.introduce.codegen.services.methods.SyncHelper;
import gov.nih.nci.cagrid.introduce.codegen.services.methods.SyncSource;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.common.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.common.ServiceConstantsBaseTemplate;
import gov.nih.nci.cagrid.introduce.templates.common.ServiceConstantsTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.ServiceConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.ResourceBaseTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.ResourceHomeTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.ResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.SingletonResourceHomeTemplate;
import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.StatusBase;
import gov.nih.nci.cagrid.introduce.upgrade.one.x.IntroduceUpgraderBase;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaQName;
import org.apache.ws.jaxme.js.JavaQNameImpl;
import org.apache.ws.jaxme.js.JavaSource;
import org.apache.ws.jaxme.js.JavaSourceFactory;
import org.apache.ws.jaxme.js.util.JavaParser;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;


public class Introduce_1_0__1_3_Upgrader extends IntroduceUpgraderBase {

    public Introduce_1_0__1_3_Upgrader(IntroduceUpgradeStatus status, ServiceInformation serviceInformation,
        String servicePath) throws Exception {
        super(status, serviceInformation, servicePath, "1.0", "1.3");
    }


    protected void upgrade() throws Exception {
        // need to make sure to save a copy of hte introduce.xml to a prev file
        // so that the
        // sync tools can pick up any service changes i make here.....
        // make a copy of the model to compate with next time
        Utils.copyFile(new File(getServicePath() + File.separator + IntroduceConstants.INTRODUCE_XML_FILE), new File(
            getServicePath() + File.separator + IntroduceConstants.INTRODUCE_XML_FILE + ".prev"));

        // make a copy of the properties to compate with next time
        Utils.copyFile(new File(getServicePath() + File.separator + IntroduceConstants.INTRODUCE_PROPERTIES_FILE),
            new File(getServicePath() + File.separator + IntroduceConstants.INTRODUCE_PROPERTIES_FILE + ".prev"));

        // add the resource property management to the main service
        ServiceType mainService = getServiceInformation().getServices().getService(0);
        mainService.getResourceFrameworkOptions().setResourcePropertyManagement(new ResourcePropertyManagement());

        
        // need to delete the old registration.xml file
        File registrationFile = new File(getServicePath() + File.separator + "etc" + File.separator
            + "registration.xml");
        registrationFile.delete();
        getStatus().addDescriptionLine(
            "removed old registration.xml file, it is now replaced with service specific one for each service");

        // need to replace the build.xml
        Utils.copyFile(new File(getServicePath() + File.separator + "build.xml"), new File(getServicePath()
            + File.separator + "build.xml.OLD"));
        Utils.copyFile(new File("." + File.separator + "skeleton" + File.separator + "build.xml"), new File(
            getServicePath() + File.separator + "build.xml"));
        Utils.copyFile(new File("." + File.separator + "skeleton" + File.separator + "build-stubs.xml"), new File(
            getServicePath() + File.separator + "build-stubs.xml"));
        Utils.copyFile(new File("." + File.separator + "skeleton" + File.separator + "dev-build.xml"), new File(
            getServicePath() + File.separator + "dev-build.xml"));
        getStatus().addDescriptionLine("replaced build.xml with new version");
        getStatus().addDescriptionLine("added build-stubs.xml");
        getStatus().addIssue("Replaced the build.xml file.",
            "Put any additions you need to the service build in the dev-build.xml file which has now been created.");

        // need to replace the build-deploy.xml
        Utils.copyFile(new File(getServicePath() + File.separator + "build-deploy.xml"), new File(getServicePath()
            + File.separator + "build-deploy.xml.OLD"));
        Utils.copyFile(new File("." + File.separator + "skeleton" + File.separator + "build-deploy.xml"), new File(
            getServicePath() + File.separator + "build-deploy.xml"));
        Utils.copyFile(new File("." + File.separator + "skeleton" + File.separator + "dev-build-deploy.xml"), new File(
            getServicePath() + File.separator + "dev-build-deploy.xml"));
        getStatus().addDescriptionLine("replaced build-deploy.xml with new version");
        getStatus()
            .addIssue("Replaced the build-deploy.xml file.",
                "Put any additions you need to the service deployment in the dev-build-deploy.xml file which has now been created.");

        // clean the config
        removeResourcePropertyProvidersFromConfig();
        // clean the client impl
        removeGetResourcePropertyMethods();

        // foreach service need to replace the resource files.....
        File srcDir = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() + File.separator + "src");
        for (int i = 0; i < getServiceInformation().getServices().getService().length; i++) {
            ServiceType service = getServiceInformation().getServices().getService(i);

            File oldConstantsFile = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "ResourceConstants.java");
            oldConstantsFile.delete();

            ServiceConstantsTemplate resourceContanstsT = new ServiceConstantsTemplate();
            String resourceContanstsS = resourceContanstsT.generate(new SpecificServiceInformation(
                getServiceInformation(), service));
            File resourceContanstsF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "common" + File.separator + service.getName()
                + "Constants.java");
            
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


            
            if (service.getResourceFrameworkOptions().getMain() != null) {

                File oldServiceConfF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator
                    + "ServiceConfiguration.java");
                oldServiceConfF.delete();

                ServiceConfigurationTemplate serviceConfT = new ServiceConfigurationTemplate();
                String serviceConfS = serviceConfT.generate(new SpecificServiceInformation(getServiceInformation(),
                    service));
                File serviceConfF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator
                    + service.getName() + "Configuration.java");
                FileWriter serviceConfFW = new FileWriter(serviceConfF);
                serviceConfFW.write(serviceConfS);
                serviceConfFW.close();
            }

            if (service.getResourceFrameworkOptions().getCustom() == null) {

                // delete the old base resource
                File oldbaseResourceF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "BaseResource.java");
                oldbaseResourceF.delete();

                File oldDaseResourceHomeF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "BaseResourceHome.java");
                oldDaseResourceHomeF.delete();

                File oldBaseResourceBaseF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "BaseResourceBase.java");
                oldBaseResourceBaseF.delete();

                File oldResourceConfigurationF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + "ResourceConfiguration.java");
                oldResourceConfigurationF.delete();

                ResourceBaseTemplate baseResourceBaseT = new ResourceBaseTemplate();
                String baseResourceBaseS = baseResourceBaseT.generate(new SpecificServiceInformation(
                    getServiceInformation(), service));
                File baseResourceBaseF = new File(srcDir.getAbsolutePath() + File.separator
                    + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                    + File.separator + "resource" + File.separator + service.getName() + "ResourceBase.java");

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
                        + File.separator + "resource" + File.separator + service.getName() + "ResourceHome.java");

                    FileWriter baseResourceHomeFW = new FileWriter(baseResourceHomeF);
                    baseResourceHomeFW.write(baseResourceHomeS);
                    baseResourceHomeFW.close();

                } else {

                    ResourceHomeTemplate baseResourceHomeT = new ResourceHomeTemplate();
                    String baseResourceHomeS = baseResourceHomeT.generate(new SpecificServiceInformation(
                        getServiceInformation(), service));
                    File baseResourceHomeF = new File(srcDir.getAbsolutePath() + File.separator
                        + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                        + File.separator + "resource" + File.separator + service.getName() + "ResourceHome.java");

                    FileWriter baseResourceHomeFW = new FileWriter(baseResourceHomeF);
                    baseResourceHomeFW.write(baseResourceHomeS);
                    baseResourceHomeFW.close();

                }

                getStatus().addDescriptionLine("replaced resource source files for service: " + service.getName());
            }
        }

        // need to add to the deploy.properties
        Properties deployProperties = getServiceInformation().getDeploymentProperties();
        deployProperties.put("index.service.registration.refresh_seconds", "600");
        deployProperties.put("index.service.index.refresh_milliseconds", "30000");
        deployProperties.put("perform.index.service.registration", "true");
        getStatus().addDescriptionLine("adding new deployment variables to deploy.properties");

        // need to add the new template var to the jndi so that it can be
        // replaced
        // by the new property for shutting off registration
        // and fix up new resource class value becuase it has a new class name
        Document jndiDoc = XMLUtilities.fileNameToDocument(getServicePath() + File.separator + "jndi-config.xml");
        List services = jndiDoc.getRootElement().getChildren("service",
            Namespace.getNamespace("http://wsrf.globus.org/jndi/config"));
        Iterator serviceI = services.iterator();
        while (serviceI.hasNext()) {
            Element service = (Element) serviceI.next();
            String serviceName = service.getAttributeValue("name");
            serviceName = serviceName.substring(serviceName.lastIndexOf("/") + 1);
            List resources = service.getChildren("resource", Namespace
                .getNamespace("http://wsrf.globus.org/jndi/config"));
            Iterator resourceI = resources.iterator();
            while (resourceI.hasNext()) {
                Element resource = (Element) resourceI.next();
                if (resource.getAttributeValue("name").equals("home")) {
                    String type = resource.getAttributeValue("type");
                    if (type.endsWith(".BaseResourceHome")) {
                        StringBuffer sb = new StringBuffer(type);
                        sb.delete(sb.lastIndexOf(".") + 1, sb.length());
                        sb.insert(sb.lastIndexOf(".") + 1, serviceName + "ResourceHome");
                        resource.setAttribute("type", sb.toString());
                    }
                } else if (resource.getAttributeValue("name").equals("configuration")) {
                    String type = resource.getAttributeValue("type");
                    StringBuffer sb = new StringBuffer(type);
                    sb.delete(sb.lastIndexOf(".") + 1, sb.length());
                    sb.insert(sb.lastIndexOf(".") + 1, serviceName + "ResourceConfiguration");
                    resource.setAttribute("type", sb.toString());
                } else if (resource.getAttributeValue("name").equals("serviceconfiguration")) {
                    String type = resource.getAttributeValue("type");
                    StringBuffer sb = new StringBuffer(type);
                    sb.delete(sb.lastIndexOf(".") + 1, sb.length());
                    sb.insert(sb.lastIndexOf(".") + 1, serviceName + "ServiceConfiguration");
                    resource.setAttribute("type", sb.toString());
                }
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
                    if (parameter.getChild("name", Namespace.getNamespace("http://wsrf.globus.org/jndi/config"))
                        .getText().equals("resourceClass")) {
                        String oldValue = parameter.getChild("value",
                            Namespace.getNamespace("http://wsrf.globus.org/jndi/config")).getText();
                        String newValue = oldValue.substring(0, oldValue.lastIndexOf(".") + 1) + serviceName
                            + "Resource";
                        parameter.getChild("value", Namespace.getNamespace("http://wsrf.globus.org/jndi/config"))
                            .setText(newValue);
                    }
                }
            }
        }
        FileWriter writer = new FileWriter(new File(getServicePath() + File.separator + "jndi-config.xml"));
        writer.write(XMLUtilities.formatXML(XMLUtilities.documentToString(jndiDoc)));
        writer.close();
        getStatus().addDescriptionLine("retemplated jndi file");

        // need to add the service tasks .jar to the tools lib directory
        FileFilter serviceTasksFilter = new FileFilter() {
            public boolean accept(File name) {
                String filename = name.getName();
                return filename.matches("caGrid.*Introduce.*serviceTasks.*jar");
            }
        };
        File serviceTasksJardir = new File("." + File.separator + "skeleton" + File.separator + "tools"
            + File.separator + "lib");
        File[] serviceTasksCandidates = serviceTasksJardir.listFiles(serviceTasksFilter);
        if (serviceTasksCandidates.length == 1) {
            File serviceTasksJar = serviceTasksCandidates[0];
            if (serviceTasksJar.exists() && serviceTasksJar.canRead()) {
                Utils.copyFile(serviceTasksJar, new File(getServicePath() + File.separator + "tools" + File.separator
                    + "lib" + File.separator + serviceTasksJar.getName()));
                getStatus().addDescriptionLine(
                    "added service tasks jar to enable patching the soap bindings that get generated for custom beans");
            } else {
                throw new Exception("Cannot find or cannot read service tasks jar to copy into the service: "
                    + serviceTasksJar.getAbsolutePath());
            }
        } else if (serviceTasksCandidates.length == 0) {
            throw new Exception("Cannot find any service tasks jar to copy into the service");
        } else {
            StringBuffer message = new StringBuffer();
            message.append("Found multiple service tasks jar candidates:\n");
            for (int i = 0; i < serviceTasksCandidates.length; i++) {
                message.append("\t").append(serviceTasksCandidates[i].getAbsolutePath());
                if (i + 1 < serviceTasksCandidates.length) {
                    message.append("\n");
                }
            }
            throw new Exception(message.toString());
        }
        
        // need to move the ant-contrib.jar to the tools lib directory
        File currentContribFile = new File(getServicePath() + File.separator + "lib" + File.separator
            + "ant-contrib.jar");
        if (currentContribFile.exists()) {
            Utils.copyFile(currentContribFile, new File(getServicePath() + File.separator + "tools" + File.separator
                + "lib" + File.separator + "ant-contrib.jar"));
            currentContribFile.delete();
        } else {
            getStatus().addIssue("Missing ant-contrib.jar", "Locate jar and place it in the tols/lib directory");
        }
        getStatus().addDescriptionLine(
            "moving ant-contrib to a location which will not cause it to be deployed with the service");

        upgradeJars();
        getStatus().addDescriptionLine("updating service with the new version of the jars");

        fixUpSchemaLocationsInModel();

        fixBugInClientLoadingClientWSDD();

        // recreate the authorization class
        // recreate the service descriptor
        // recreate the provider impl
        // those will be done during the resync

        getStatus().setStatus(StatusBase.UPGRADE_OK);
    }


    private void fixBugInClientLoadingClientWSDD() {
        ServiceType[] services = getServiceInformation().getServices().getService();
        for (int i = 0; i < services.length; i++) {
            ServiceType service = services[i];
            StringBuffer fileContent = null;
            String serviceClient = getServiceInformation().getBaseDirectory().getAbsolutePath() + File.separator
                + "src" + File.separator + CommonTools.getPackageDir(service) + File.separator + "client"
                + File.separator + service.getName() + "Client.java";
            try {
                fileContent = Utils.fileToStringBuffer(new File(serviceClient));
                int startOfMethod = SyncHelper.startOfSignature(fileContent, "private " + service.getName()
                    + "PortType createPortType()");
                if (startOfMethod >= 0) {
                    int endOfMethod = SyncHelper.bracketMatch(fileContent, startOfMethod);
                    String subString = fileContent.substring(startOfMethod, endOfMethod);
                    int lineStart = subString.indexOf("InputStream resourceAsStream =");
                    if (lineStart > 0) {
                        String lineandon = subString.substring(lineStart);
                        int lineEnd = lineandon.indexOf(";");
                        fileContent.replace(startOfMethod + lineStart, startOfMethod + lineStart + lineEnd,
                            "InputStream resourceAsStream = getClass().getResourceAsStream(\"client-config.wsdd\")");
                        FileWriter fw = new FileWriter(serviceClient);
                        fw.write(fileContent.toString());
                        fw.close();
                        getStatus().addDescriptionLine("fixed client load of wsdd bug in: " + serviceClient);
                    } else {
                        getStatus().addDescriptionLine("unable to fix client load of wsdd: " + serviceClient);
                        getStatus()
                            .addIssue(
                                "unable to fix client load of wsdd: " + serviceClient,
                                "please replace the line:\n\tInputStream resourceAsStream = ClassUtils.getResourceAsStream(getClass(), \"client-config.wsdd\"); \nwith:\n\tInputStream resourceAsStream = getClass().getResourceAsStream(\"client-config.wsdd\");\nin the createPortType() method of the "
                                    + serviceClient + " client java class");
                    }
                } else {
                    getStatus().addDescriptionLine("unable to fix client load of wsdd: " + serviceClient);
                    getStatus()
                        .addIssue(
                            "unable to fix client load of wsdd: " + serviceClient,
                            "please replace the line:\n\tInputStream resourceAsStream = ClassUtils.getResourceAsStream(getClass(), \"client-config.wsdd\"); \nwith:\n\tInputStream resourceAsStream = getClass().getResourceAsStream(\"client-config.wsdd\");\nin the createPortType() method of the "
                                + serviceClient + " client java class");
                }
            } catch (Exception e) {
                getStatus().addDescriptionLine("unable to fix client load of wsdd: " + serviceClient);
                getStatus()
                    .addIssue(
                        "unable to fix client load of wsdd: " + serviceClient,
                        "please replace the line:\n\tInputStream resourceAsStream = ClassUtils.getResourceAsStream(getClass(), \"client-config.wsdd\"); \nwith:\n\tInputStream resourceAsStream = getClass().getResourceAsStream(\"client-config.wsdd\");\nin the createPortType() method of the "
                            + serviceClient + " client java class");
            }
        }
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


    private void fixUpSchemaLocationsInModel() {
        boolean editsMade = false;
        NamespacesType namespaces = getServiceInformation().getServiceDescriptor().getNamespaces();
        if (namespaces != null && namespaces.getNamespace() != null) {
            for (NamespaceType namespace : namespaces.getNamespace()) {
                String schemaLocation = namespace.getLocation();
                if (schemaLocation != null && schemaLocation.indexOf('\\') != -1) {
                    String cleanLocation = schemaLocation.replace('\\', '/');
                    namespace.setLocation(cleanLocation);
                    getStatus().addDescriptionLine("Edited schema location of namespace " + namespace.getNamespace());
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


    private void removeResourcePropertyProvidersFromConfig() throws Exception {
        for (int i = 0; i < getServiceInformation().getServices().getService().length; i++) {
            ServiceType service = getServiceInformation().getServices().getService(i);
            ProviderTools.removeProviderFromServiceConfig(service, "GetRPProvider", getServiceInformation());
            ProviderTools.removeProviderFromServiceConfig(service, "GetMRPProvider", getServiceInformation());
            ProviderTools.removeProviderFromServiceConfig(service, "QueryRPProvider", getServiceInformation());
        }
    }


    private void removeGetResourcePropertyMethods() throws Exception {
        // foreach service need to replace the resource files.....
        // File srcDir = new
        // File(getServiceInformation().getBaseDirectory().getAbsolutePath() +
        // File.separator + "src");
        for (int i = 0; i < getServiceInformation().getServices().getService().length; i++) {
            ServiceType service = getServiceInformation().getServices().getService(i);
            SyncSource syncsource = new SyncSource(getServiceInformation().getBaseDirectory(), getServiceInformation(),
                service);

            JavaSourceFactory jsf;
            JavaParser jp;

            jsf = new JavaSourceFactory();
            jp = new JavaParser(jsf);

            String serviceClient = getServiceInformation().getBaseDirectory().getAbsolutePath() + File.separator
                + "src" + File.separator + CommonTools.getPackageDir(service) + File.separator + "client"
                + File.separator + service.getName() + "Client.java";

            try {
                jp.parse(new File(serviceClient));

            } catch (Exception e) {
                throw new SynchronizationException("Error parsing service interface:" + e.getMessage(), e);
            }
            Iterator it = jsf.getJavaSources();
            JavaSource source = (JavaSource) it.next();
            source.setForcingFullyQualifiedName(true);

            JavaMethod[] methods = source.getMethods();
            int j = 0;
            boolean found = false;
            for (j = 0; j < methods.length; j++) {
                if (methods[j].getName().equals("getResourceProperty")) {
                    found = true;
                    break;
                }
            }

            if (found) {
                JavaQName qname = JavaQNameImpl.getInstance("GetResourcePropertyResponse");

                methods[j].setType(qname);
                syncsource.removeClientImpl(methods[j]);
            }
        }

    }
}
