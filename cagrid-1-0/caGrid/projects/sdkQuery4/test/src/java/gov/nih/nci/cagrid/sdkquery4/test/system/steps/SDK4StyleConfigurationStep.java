package gov.nih.nci.cagrid.sdkquery4.test.system.steps;

import gov.nih.nci.cagrid.common.JarUtilities;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.extension.ClassMapping;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.sdkquery4.processor.SDK4QueryProcessor;
import gov.nih.nci.cagrid.sdkquery4.style.wizard.config.AbstractStyleConfigurationStep;
import gov.nih.nci.cagrid.sdkquery4.style.wizard.config.QueryProcessorBaseConfigurationStep;
import gov.nih.nci.cagrid.sdkquery4.style.wizard.config.QueryProcessorSecurityConfigurationStep;
import gov.nih.nci.cagrid.sdkquery4.style.wizard.config.SDK4InitialConfigurationStep;
import gov.nih.nci.cagrid.sdkquery4.style.wizard.config.SchemaMappingConfigurationStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 *  SDK4StyleConfigurationStep
 *  Step to apply configuration to the 
 *  SDK 4 Data Service Style 
 * 
 * @author David Ervin
 * 
 * @created Jan 28, 2008 11:24:21 AM
 * @version $Id: SDK4StyleConfigurationStep.java,v 1.7 2008-04-17 15:26:29 dervin Exp $ 
 */
public class SDK4StyleConfigurationStep extends Step {
    public static final String SDKQUERY4_BASE_DIR = "../sdkQuery4";
    
    public static final String EXT_SDK_DIR = SDKQUERY4_BASE_DIR + File.separator + "build/remote-client";
    public static final String DOMAIN_MODEL_FILE = SDKQUERY4_BASE_DIR + File.separator + "test/resources/sdkExampleDomainModel.xml";
    public static final String PROPERTY_REMOTE_HOST_NAME = "remote.sdk.host.name";
    public static final String PROPERTY_REMOTE_HOST_PORT = "remote.sdk.host.port";
    public static final String DEFAULT_REMOTE_HOST_NAME_VALUE = "http://localhost";
    public static final String DEFAULT_REMOTE_HOST_PORT_VALUE = "8080";
    
    private static Log LOG = LogFactory.getLog(SDK4StyleConfigurationStep.class);
    
    private File serviceBaseDirectory = null;
    private ServiceInformation serviceInformation = null;

    public SDK4StyleConfigurationStep(File serviceBaseDirectory) {
        this.serviceBaseDirectory = serviceBaseDirectory;
    }


    public void runStep() throws Throwable {
        getInitialConfiguration().applyConfiguration();
        getQueryProcessorConfiguration().applyConfiguration();
        getQueryProcessorSecurityConfiguration().applyConfiguration();
        applyDomainModelConfiguration();
        getSchemaMappingConfiguration().applyConfiguration();
        // persist the changes made by the configuration steps
        File serviceModelFile = new File(getServiceInformation().getBaseDirectory(), IntroduceConstants.INTRODUCE_XML_FILE);
        LOG.debug("Persisting changes to service model (" + serviceModelFile.getAbsolutePath() + ")");
        FileWriter writer = new FileWriter(serviceModelFile);
        Utils.serializeObject(getServiceInformation().getServiceDescriptor(), IntroduceConstants.INTRODUCE_SKELETON_QNAME, writer);
        writer.flush();
        writer.close();
    }
    
    
    private AbstractStyleConfigurationStep getInitialConfiguration() throws Exception {
        SDK4InitialConfigurationStep configuration = 
            new SDK4InitialConfigurationStep(getServiceInformation());
        configuration.setQueryProcessorClassName(SDK4QueryProcessor.class.getName());
        File styleLibDir = new File(ExtensionsLoader.getInstance().getExtensionsDir().getAbsolutePath()
            + File.separator + "data" + File.separator + "styles" + File.separator + "cacore4" + File.separator + "lib");
        configuration.setStyleLibDirectory(styleLibDir);
        return configuration;
    }
    
    
    private AbstractStyleConfigurationStep getQueryProcessorConfiguration() throws Exception {
        QueryProcessorBaseConfigurationStep configuration = 
            new QueryProcessorBaseConfigurationStep(getServiceInformation());
        File remoteClientDir = new File(EXT_SDK_DIR);
        File remoteClientLibDir = new File(remoteClientDir, "lib");
        File remoteClientConfDir = new File(remoteClientDir, "conf");
        configuration.setApplicationName("example40");
        configuration.setBeansJarLocation(new File(remoteClientLibDir, "example40-beans.jar").getAbsolutePath());
        configuration.setCaseInsensitiveQueries(false);
        configuration.setRemoteConfigDir(remoteClientConfDir.getAbsolutePath());
        configuration.setUseLocalApi(false);
        String hostName = System.getProperty(PROPERTY_REMOTE_HOST_NAME, DEFAULT_REMOTE_HOST_NAME_VALUE);
        Integer hostPort = Integer.valueOf(System.getProperty(PROPERTY_REMOTE_HOST_PORT, DEFAULT_REMOTE_HOST_PORT_VALUE));
        LOG.debug("Setting caCORE Application host name to " + hostName);
        LOG.debug("Setting caCORE Application host port to " + hostPort);
        configuration.setHostName(hostName);
        configuration.setHostPort(hostPort);
        return configuration;
    }
    
    
    private AbstractStyleConfigurationStep getQueryProcessorSecurityConfiguration() throws Exception {
        QueryProcessorSecurityConfigurationStep configuration =
            new QueryProcessorSecurityConfigurationStep(getServiceInformation());
        configuration.setUseLogin(false);
        return configuration;
    }
    
    
    private void applyDomainModelConfiguration() throws Exception {
        File domainModelFile = new File(DOMAIN_MODEL_FILE);
        setSelectedDomainModelFilename(domainModelFile);
    }

    
    private AbstractStyleConfigurationStep getSchemaMappingConfiguration() throws Exception {
        SchemaMappingConfigurationStep configuration = 
            new SchemaMappingConfigurationStep(getServiceInformation());
        File schemaDir = new File(getServiceInformation().getBaseDirectory(),
            "schema" + File.separator + getServiceInformation().getIntroduceServiceProperties()
                .getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));
        // the sdk's config dir jar will have the schemas in it
        String applicationName = CommonTools.getServicePropertyValue(
            getServiceInformation().getServiceDescriptor(), 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX 
                + SDK4QueryProcessor.PROPERTY_APPLICATION_NAME);
        String configJarFilename = getServiceInformation().getBaseDirectory().getAbsolutePath()
            + File.separator + "lib" + File.separator + applicationName + "-config.jar";
        // get the package names from the domain model
        File domainModelFile = new File(DOMAIN_MODEL_FILE);
        FileReader modelReader = new FileReader(domainModelFile);
        DomainModel model = (DomainModel) Utils.deserializeObject(modelReader, DomainModel.class);
        UMLClass[] classes = model.getExposedUMLClassCollection().getUMLClass();
        // extract a set of package names
        Set<String> packages = new HashSet<String>();
        for (UMLClass c : classes) {
            String name = c.getPackageName();
            if (!packages.contains(name)) {
                packages.add(name);
            }
        }
        // walk jar entries, looking for xsds
        JarFile configJar = new JarFile(configJarFilename);
        Enumeration<JarEntry> entries = configJar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".xsd")) {
                // found a schema, what package does it go with?
                String schemaPackageName = new File(entry.getName()).getName();
                schemaPackageName = schemaPackageName.substring(0, schemaPackageName.length() - 4);
                for (String packageName : packages) {
                    if (packageName.equals(schemaPackageName)) {
                        // create a schema file and namespace type
                        StringBuffer schemaText = JarUtilities.getFileContents(configJar, entry.getName());
                        File schemaFile = new File(schemaDir, new File(entry.getName()).getName());
                        Utils.stringBufferToFile(schemaText, schemaFile.getAbsolutePath());
                        
                        // add the namespace to the configuration for later
                        // incorperation in the service
                        LOG.debug("Mapping package " + packageName + " to schema " + schemaFile.getName());
                        configuration.mapPackageToSchema(packageName, schemaFile);                            
                        break;
                    }
                }
            }
        }
        return configuration;
    }
    
    
    private ServiceInformation getServiceInformation() throws Exception {
        if (serviceInformation == null) {
            serviceInformation = new ServiceInformation(serviceBaseDirectory);
        }
        return serviceInformation;
    }
    
    
    private void setSelectedDomainModelFilename(File domainModelFile) throws Exception {
        // set the selected file on the data extension's info
        Data extensionData = getExtensionData();
        CadsrInformation info = extensionData.getCadsrInformation();
        if (info == null) {
            info = new CadsrInformation();
        }

        ResourcePropertyType dmResourceProp = getDomainModelResourceProperty();

        File localDomainFile = new File(getServiceInformation().getBaseDirectory().getAbsolutePath() 
            + File.separator + "etc" + File.separator + domainModelFile.getName());
        Utils.copyFile(domainModelFile, localDomainFile);

        dmResourceProp.setPopulateFromFile(true);
        dmResourceProp.setFileLocation(localDomainFile.getName());
        info.setUseSuppliedModel(true);

        extensionData.setCadsrInformation(info);
        storeExtensionData(extensionData);

        loadDomainModelFile(domainModelFile);
    }
    
    
    private void loadDomainModelFile(File domainModelFile) throws Exception {
        // get the domain model
        FileReader modelReader = new FileReader(domainModelFile);
        DomainModel model = (DomainModel) Utils.deserializeObject(modelReader, DomainModel.class);
        modelReader.close();
        // get extension data
        Data extensionData = getExtensionData();
        CadsrInformation info = extensionData.getCadsrInformation();
        // set cadsr project information
        info.setProjectLongName(model.getProjectLongName());
        info.setProjectVersion(model.getProjectVersion());
        // walk classes, creating package groupings as needed
        Map<String, List<String>> packageClasses = new HashMap<String, List<String>>();
        UMLClass[] modelClasses = model.getExposedUMLClassCollection().getUMLClass(); 
        for (int i = 0; i < modelClasses.length; i++) {
            String packageName = modelClasses[i].getPackageName();
            List<String> classList = null;
            if (packageClasses.containsKey(packageName)) {
                classList = packageClasses.get(packageName);
            } else {
                classList = new ArrayList<String>();
                packageClasses.put(packageName, classList);
            }
            classList.add(modelClasses[i].getClassName());
        }
        // create cadsr packages
        CadsrPackage[] packages = new CadsrPackage[packageClasses.keySet().size()];
        String[] packageNames = new String[packages.length];
        int packIndex = 0;
        Iterator packageNameIter = packageClasses.keySet().iterator();
        while (packageNameIter.hasNext()) {
            String packName = (String) packageNameIter.next();
            String mappedNamespace = suggestNamespaceString(
                model.getProjectShortName(), model.getProjectVersion(), packName);
            CadsrPackage pack = new CadsrPackage();
            pack.setName(packName);
            pack.setMappedNamespace(mappedNamespace);
            // create ClassMappings for the package's classes
            List<String> classNameList = packageClasses.get(packName);
            ClassMapping[] mappings = new ClassMapping[classNameList.size()];
            for (int i = 0; i < classNameList.size(); i++) {
                ClassMapping mapping = new ClassMapping();
                String className = classNameList.get(i);
                mapping.setClassName(className);
                mapping.setElementName(className);
                mapping.setSelected(true);
                mapping.setTargetable(true);
                mappings[i] = mapping;
            }
            pack.setCadsrClass(mappings);
            packages[packIndex] = pack;
            packageNames[packIndex] = pack.getName();
            packIndex++;
        }
        info.setPackages(packages);
        extensionData.setCadsrInformation(info);
        storeExtensionData(extensionData);
    }
    
    
    private String suggestNamespaceString(String projectShortName, String projectVersion, String packName) {
        String modVersion = projectVersion;
        if (modVersion.indexOf('.') == -1) {
            modVersion += ".0";
        }
        return "gme://" + projectShortName + ".caBIG/" + modVersion + "/" + packName;
    }
    
    
    private Data getExtensionData() throws Exception {
        ServiceDescription serviceDesc = getServiceInformation().getServiceDescriptor();
        ExtensionType[] extensions = serviceDesc.getExtensions().getExtension();
        ExtensionType dataExtension = null;
        for (int i = 0; i < extensions.length; i++) {
            if (extensions[i].getName().equals("data")) {
                dataExtension = extensions[i];
                break;
            }
        }
        if (dataExtension.getExtensionData() == null) {
            dataExtension.setExtensionData(new ExtensionTypeExtensionData());
        }
        Data extensionData = ExtensionDataUtils.getExtensionData(dataExtension.getExtensionData());
        return extensionData;
    }
    
    
    private void storeExtensionData(Data data) throws Exception {
        ExtensionType[] extensions = getServiceInformation().getServiceDescriptor()
            .getExtensions().getExtension();
        ExtensionType dataExtension = null;
        for (int i = 0; i < extensions.length; i++) {
            if (extensions[i].getName().equals("data")) {
                dataExtension = extensions[i];
                break;
            }
        }
        if (dataExtension.getExtensionData() == null) {
            dataExtension.setExtensionData(new ExtensionTypeExtensionData());
        }
        ExtensionDataUtils.storeExtensionData(dataExtension.getExtensionData(), data);
    }
    
    
    private ResourcePropertyType getDomainModelResourceProperty() throws Exception {
        ServiceType baseService = getServiceInformation().getServices().getService(0);

        ResourcePropertyType[] typedProps = CommonTools.getResourcePropertiesOfType(
            getServiceInformation().getServices().getService(0), DataServiceConstants.DOMAIN_MODEL_QNAME);
        if (typedProps == null || typedProps.length == 0) {
            ResourcePropertyType dmProp = new ResourcePropertyType();
            dmProp.setQName(DataServiceConstants.DOMAIN_MODEL_QNAME);
            dmProp.setRegister(true);
            CommonTools.addResourcePropety(baseService, dmProp);
            return dmProp;
        } else {
            return typedProps[0];
        }
    }
}
