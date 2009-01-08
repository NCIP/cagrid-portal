package org.cagrid.data.sdkquery41.style.wizard.config;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.extension.ClassMapping;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.metadata.xmi.XMIParser;
import gov.nih.nci.cagrid.metadata.xmi.XmiFileType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cagrid.cadsr.UMLModelService;
import org.cagrid.cadsr.client.CaDSRUMLModelService;
import org.cagrid.data.sdkquery41.style.common.SDK41StyleConstants;

/**
 * DomainModelConfigurationStep
 * Provides a central point for configuring the domain model.
 * 
 * This bean will actually store up information from all three means of
 * getting a domain model into a service (file system provided, cadsr, and SDK's XMI)
 * and on applyConfiguration() implement the method defined by setModelSource() 
 * 
 * @author David
 */
public class DomainModelConfigurationStep extends AbstractStyleConfigurationStep {
    
    private DomainModelConfigurationSource configurationSource = null;
    
    // from local file system
    private File domainModelFile = null;
    
    // from SDK's config
    private String projectShortName = null;
    private String projectVersion = null;
    private File xmiFile = null;
    private XmiFileType xmiType = null;
    
    // from cadsr
    // TODO: from mms??
    private String cadsrUrl = null;
    private Project selectedProject = null;
    private List<UMLPackageMetadata> selectedPackages = null;

    public DomainModelConfigurationStep(ServiceInformation serviceInfo) {
        super(serviceInfo);
        selectedPackages = new ArrayList<UMLPackageMetadata>();
    }


    public void applyConfiguration() throws Exception {
        if (configurationSource == null) {
            throw new IllegalStateException(
                "The domain model configuration source has not been defined!!!");
        }
        switch (configurationSource) {
            case CADSR:
                applyCadsrModelConfiguration();
                break;
            case SDK_CONFIG_XMI:
                applySdkModelConfiguration();
                break;
            case FILE_SYSTEM:
                applyFileSystemModelConfiguration();
                break;
            default:
                throw new IllegalStateException("Unknown model configuration source");
        }
    }
    
    
    private void applyCadsrModelConfiguration() throws Exception {
        // TODO: replace this with whatever your new Extension data is  (storing the shortname/version)
        CadsrInformation cadsrInfo = new CadsrInformation();
        cadsrInfo.setNoDomainModel(false);
        cadsrInfo.setUseSuppliedModel(false);
        cadsrInfo.setProjectLongName(selectedProject.getLongName());
        cadsrInfo.setProjectVersion(selectedProject.getVersion());
        
        // packages
        UMLModelService cadsrClient = new CaDSRUMLModelService(cadsrUrl);
        CadsrPackage[] packages = new CadsrPackage[selectedPackages.size()];
        int index = 0;
        for (UMLPackageMetadata umlPackage : selectedPackages) {
            CadsrPackage cadsrPack = new CadsrPackage();
            cadsrPack.setName(umlPackage.getName());
            UMLClassMetadata[] classMetadata = cadsrClient.findClassesInPackage(
                selectedProject, umlPackage.getName());
            ClassMapping[] mappings = new ClassMapping[classMetadata.length];
            for (int j = 0; j < classMetadata.length; j++) {
                ClassMapping mapping = new ClassMapping();
                // NOT setting element name until schema mapping panel
                mapping.setClassName(classMetadata[j].getName());
                mapping.setSelected(true);
                mapping.setTargetable(true);
            }
            cadsrPack.setCadsrClass(mappings);
            packages[index] = cadsrPack;
            index++;
        }
        cadsrInfo.setPackages(packages);
        storeCadsrInformation(cadsrInfo);
    }
    
    
    private void applySdkModelConfiguration() throws Exception {
        // create a domain model from the XMI
        XMIParser parser = new XMIParser(projectShortName, projectVersion);
        DomainModel generatedModel = parser.parse(xmiFile, xmiType);
        // save the domain model to the service's etc dir
        File etcDir = new File(
            SharedConfiguration.getInstance().getServiceInfo().getBaseDirectory(), "etc");
        String applicationName = SharedConfiguration.getInstance()
            .getSdkDeployProperties().getProperty(
                SDK41StyleConstants.DeployProperties.PROJECT_NAME);
        File generatedModelFile = new File(etcDir, applicationName + "_domainModel.xml");
        FileWriter modelWriter = new FileWriter(generatedModelFile);
        MetadataUtils.serializeDomainModel(generatedModel, modelWriter);
        modelWriter.flush();
        modelWriter.close();
        
        // get / create the domain model resource property
        ServiceInformation serviceInfo = SharedConfiguration.getInstance().getServiceInfo();
        ResourcePropertyType domainModelResourceProperty = null;
        ResourcePropertyType[] domainModelProps = CommonTools.getResourcePropertiesOfType(
            serviceInfo.getServices().getService(0), DataServiceConstants.DOMAIN_MODEL_QNAME);
        if (domainModelProps.length != 0) {
            domainModelResourceProperty = domainModelProps[0];
            // if old file exists, delete it
            File oldFile = new File(
                etcDir, domainModelResourceProperty.getFileLocation());
            if (oldFile.exists()) {
                oldFile.delete();
            }
        } else {
            domainModelResourceProperty = new ResourcePropertyType();
            domainModelResourceProperty.setPopulateFromFile(true);
            domainModelResourceProperty.setRegister(true);
            domainModelResourceProperty.setQName(DataServiceConstants.DOMAIN_MODEL_QNAME);
        }
        
        // set value of resource property
        domainModelResourceProperty.setFileLocation(generatedModelFile.getName());
        
        // possibly put the resource property in the service for the first time
        if (domainModelProps.length == 0) {
            CommonTools.addResourcePropety(
                serviceInfo.getServices().getService(0), domainModelResourceProperty);
        }
        
        // deserialize the domain model
        DomainModel model = null;
        FileReader reader = new FileReader(generatedModelFile);
        model = MetadataUtils.deserializeDomainModel(reader);
        reader.close();
        
        // set the cadsr information to NOT generate a new model
        CadsrInformation cadsrInfo = new CadsrInformation();
        cadsrInfo.setNoDomainModel(false);
        cadsrInfo.setUseSuppliedModel(true);
        
        // TODO: change this to the short name + version in the new extension data
        cadsrInfo.setProjectLongName(model.getProjectLongName());
        cadsrInfo.setProjectVersion(model.getProjectVersion());
        
        // map classes by packages
        Map<String, List<UMLClass>> classesByPackage = new HashMap<String, List<UMLClass>>();
        for (UMLClass modelClass : model.getExposedUMLClassCollection().getUMLClass()) {
            List<UMLClass> packageClasses = classesByPackage.get(modelClass.getPackageName());
            if (packageClasses == null) {
                packageClasses = new LinkedList<UMLClass>();
                classesByPackage.put(modelClass.getPackageName(), packageClasses);
            }
            packageClasses.add(modelClass);
        }
        
        List<CadsrPackage> cadsrPackages = new ArrayList<CadsrPackage>();
        for (String packageName : classesByPackage.keySet()) {
            List<UMLClass> classes = classesByPackage.get(packageName);
            CadsrPackage pack = new CadsrPackage();
            pack.setName(packageName);
            List<ClassMapping> classMappings = new ArrayList<ClassMapping>();
            for (UMLClass clazz : classes) {
                ClassMapping mapping = new ClassMapping();
                mapping.setClassName(clazz.getClassName());
                // NOT populating element names until Schema Mapping Panel
                mapping.setSelected(true);
                mapping.setTargetable(true);
                classMappings.add(mapping);
            }
            ClassMapping[] mappingArray = new ClassMapping[classMappings.size()];
            classMappings.toArray(mappingArray);
            pack.setCadsrClass(mappingArray);
            cadsrPackages.add(pack);
        }
        CadsrPackage[] packageArray = new CadsrPackage[cadsrPackages.size()];
        cadsrPackages.toArray(packageArray);
        cadsrInfo.setPackages(packageArray);
        
        storeCadsrInformation(cadsrInfo);
    }
    
    
    private void applyFileSystemModelConfiguration() throws Exception {
        // copy the domain model to the service's etc dir
        File etcDir = new File(
            SharedConfiguration.getInstance().getSdkDirectory(), "etc");
        String applicationName = SharedConfiguration.getInstance()
            .getSdkDeployProperties().getProperty(
                SDK41StyleConstants.DeployProperties.PROJECT_NAME);
        File serviceModelFile = new File(etcDir, applicationName + "_domainModel.xml");
        Utils.copyFile(domainModelFile, serviceModelFile);
        
        // get / create the domain model resource property
        ServiceInformation serviceInfo = SharedConfiguration.getInstance().getServiceInfo();
        ResourcePropertyType domainModelResourceProperty = null;
        ResourcePropertyType[] domainModelProps = CommonTools.getResourcePropertiesOfType(
            serviceInfo.getServices().getService(0), DataServiceConstants.DOMAIN_MODEL_QNAME);
        if (domainModelProps.length != 0) {
            domainModelResourceProperty = domainModelProps[0];
            // if old file exists, delete it
            File oldFile = new File(
                etcDir, domainModelResourceProperty.getFileLocation());
            if (oldFile.exists()) {
                oldFile.delete();
            }
        } else {
            domainModelResourceProperty = new ResourcePropertyType();
            domainModelResourceProperty.setPopulateFromFile(true);
            domainModelResourceProperty.setRegister(true);
            domainModelResourceProperty.setQName(DataServiceConstants.DOMAIN_MODEL_QNAME);
        }
        
        // set value of resource property
        domainModelResourceProperty.setFileLocation(serviceModelFile.getName());
        
        // possibly put the resource property in the service for the first time
        if (domainModelProps.length == 0) {
            CommonTools.addResourcePropety(
                serviceInfo.getServices().getService(0), domainModelResourceProperty);
        }
        
        // deserialize the domain model
        DomainModel model = null;
        FileReader reader = new FileReader(serviceModelFile);
        model = MetadataUtils.deserializeDomainModel(reader);
        reader.close();
        
        // set the cadsr information to NOT generate a new model
        CadsrInformation cadsrInfo = new CadsrInformation();
        cadsrInfo.setNoDomainModel(false);
        cadsrInfo.setUseSuppliedModel(true);
        
        // TODO: shortname + version!
        cadsrInfo.setProjectLongName(model.getProjectLongName());
        cadsrInfo.setProjectVersion(model.getProjectVersion());
        
        // map classes by packages
        Map<String, List<UMLClass>> classesByPackage = new HashMap<String, List<UMLClass>>();
        for (UMLClass modelClass : model.getExposedUMLClassCollection().getUMLClass()) {
            List<UMLClass> packageClasses = classesByPackage.get(modelClass.getPackageName());
            if (packageClasses == null) {
                packageClasses = new LinkedList<UMLClass>();
                classesByPackage.put(modelClass.getPackageName(), packageClasses);
            }
            packageClasses.add(modelClass);
        }
        
        List<CadsrPackage> cadsrPackages = new ArrayList<CadsrPackage>();
        for (String packageName : classesByPackage.keySet()) {
            List<UMLClass> classes = classesByPackage.get(packageName);
            CadsrPackage pack = new CadsrPackage();
            pack.setName(packageName);
            List<ClassMapping> classMappings = new ArrayList<ClassMapping>();
            for (UMLClass clazz : classes) {
                ClassMapping mapping = new ClassMapping();
                mapping.setClassName(clazz.getClassName());
                // NOT populating element names until Schema Mapping Panel
                mapping.setSelected(true);
                mapping.setTargetable(true);
                classMappings.add(mapping);
            }
            ClassMapping[] mappingArray = (ClassMapping[]) classMappings.toArray();
            pack.setCadsrClass(mappingArray);
            cadsrPackages.add(pack);
        }
        CadsrPackage[] packageArray = (CadsrPackage[]) cadsrPackages.toArray();
        cadsrInfo.setPackages(packageArray);
        storeCadsrInformation(cadsrInfo);
    }
    
    
    private void storeCadsrInformation(CadsrInformation cadsrInfo) throws Exception {
        // set the cadsr info on the extension data model
        Data data = getExtensionData();
        data.setCadsrInformation(cadsrInfo);
        storeExtensionData(data);
    }
    
    
    public void setModelSource(DomainModelConfigurationSource source) {
        this.configurationSource = source;
    }
    
    
    public DomainModelConfigurationSource getCurrentModelSource() {
        return configurationSource;
    }
    
    
    public void setDomainModelLocalFile(File file) {
        this.domainModelFile = file;
    }
    
    
    public void setModelFromConfigInformation(String shortName, String version) {
        this.projectShortName = shortName;
        this.projectVersion = version;
    }
    
    
    public void setCadsrUrl(String url) {
        this.cadsrUrl = url;
    }
    
    
    public String getCadsrUrl() {
        return this.cadsrUrl;
    }
    
    
    public void setCadsrProject(Project proj) {
        this.selectedProject = proj;
    }
    
    
    public void addCadsrPackage(UMLPackageMetadata pack) {
        for (UMLPackageMetadata p : selectedPackages) {
            if (p.getName().equals(pack)) {
                throw new IllegalStateException("Package " + p.getName() + " is already selected");
            }
        }
        this.selectedPackages.add(pack);
    }
    
    
    public boolean removeCadsrPackage(String name) {
        Iterator<UMLPackageMetadata> iter = selectedPackages.iterator();
        while (iter.hasNext()) {
            UMLPackageMetadata pack = iter.next();
            if (name.equals(pack.getName())) {
                iter.remove();
                return true;
            }
        }
        return false;
    }
    
    
    public List<UMLPackageMetadata> getCadsrPacakges() {
        return this.selectedPackages;
    }
    

    public String getProjectShortName() {
        return projectShortName;
    }


    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }


    public String getProjectVersion() {
        return projectVersion;
    }


    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }


    public File getXmiFile() {
        return xmiFile;
    }


    public void setXmiFile(File xmiFile) {
        this.xmiFile = xmiFile;
    }


    public XmiFileType getXmiType() {
        return xmiType;
    }


    public void setXmiType(XmiFileType xmiType) {
        this.xmiType = xmiType;
    }
    
    
    public enum DomainModelConfigurationSource {
        CADSR, SDK_CONFIG_XMI, FILE_SYSTEM
    }
}
