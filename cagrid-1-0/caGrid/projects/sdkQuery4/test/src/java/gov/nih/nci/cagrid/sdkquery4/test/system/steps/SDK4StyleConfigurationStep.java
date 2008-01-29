package gov.nih.nci.cagrid.sdkquery4.test.system.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.extension.ClassMapping;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.ui.NamespaceUtils;
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
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 
 *  SDK4StyleConfigurationStep
 *  Step to apply configuration to the 
 *  SDK 4 Data Service Style 
 * 
 * @author David Ervin
 * 
 * @created Jan 28, 2008 11:24:21 AM
 * @version $Id: SDK4StyleConfigurationStep.java,v 1.1 2008-01-29 16:06:58 dervin Exp $ 
 */
public class SDK4StyleConfigurationStep extends Step {
    
    public static final String EXT_SDK_DIR = "ext/lib/sdk/remote-client";
    public static final String DOMAIN_MODEL_FILE = "test/resources/sdkExampleDomainModel.xml";
    public static final String PROPERTY_REMOTE_HOST_NAME = "remote.sdk.host.name";
    public static final String PROPERTY_REMOTE_HOST_PORT = "remote.sdk.host.port";
    
    private File serviceBaseDirectory = null;
    private ServiceInformation serviceInformation = null;

    public SDK4StyleConfigurationStep(File serviceBaseDirectory) {
        this.serviceBaseDirectory = serviceBaseDirectory;
    }


    public void runStep() throws Throwable {
        List<AbstractStyleConfigurationStep> configSteps =
            new ArrayList<AbstractStyleConfigurationStep>();
        configSteps.add(getInitialConfiguration());
        configSteps.add(getQueryProcessorConfiguration());
        configSteps.add(getQueryProcessorSecurityConfiguration());
        applyDomainModelConfiguration();
        configSteps.add(getSchemaMappingConfiguration());
        
        for (AbstractStyleConfigurationStep step : configSteps) {
            step.applyConfiguration();
        }
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
        File remoteClientConfDir = new File(remoteClientDir, "config");
        configuration.setApplicationName("example40");
        configuration.setBeansJarLocation(new File(remoteClientLibDir, "example40-client.jar").getAbsolutePath());
        configuration.setCaseInsensitiveQueries(false);
        configuration.setConfigurationDir(remoteClientConfDir.getAbsolutePath());
        configuration.setUseLocalApi(false);
        String hostName = System.getProperty(PROPERTY_REMOTE_HOST_NAME);
        Integer hostPort = Integer.valueOf(System.getProperty(PROPERTY_REMOTE_HOST_PORT));
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
        // iterate packages in the domain model, locate schemas in the sdk config dir
        File sdkConfigDir = new File(EXT_SDK_DIR + File.separator + "config");
        File domainModelFile = new File(DOMAIN_MODEL_FILE);
        FileReader modelReader = new FileReader(domainModelFile);
        DomainModel model = (DomainModel) Utils.deserializeObject(modelReader, DomainModel.class);
        UMLClass[] classes = model.getExposedUMLClassCollection().getUMLClass();
        // extract a set of package names
        Set<String> packages = new HashSet<String>();
        for (UMLClass c : classes) {
            packages.add(c.getPackageName());
        }
        // list schemas in the config dir
        File[] schemas = sdkConfigDir.listFiles(new FileFilter() {
            public boolean accept(File path) {
                return path.getName().toLowerCase().endsWith(".xsd");
            }
        });
        for (String packName : packages) {
            // find the schema with the same name
            for (File schema : schemas) {
                String name = schema.getName();
                if (name.substring(0, name.length() - 4).equals(packName)) {
                    configuration.mapPackageToSchema(packName, schema);
                    break;
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
            String mappedNamespace = NamespaceUtils.createNamespaceString(
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
        File serviceModelFile = new File(getServiceInformation().getBaseDirectory(), IntroduceConstants.INTRODUCE_XML_FILE);
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
        ExtensionDataUtils.storeExtensionData(dataExtension.getExtensionData(), data);
        Utils.serializeDocument(serviceModelFile.getAbsolutePath(), serviceDesc, IntroduceConstants.INTRODUCE_SKELETON_QNAME);
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
