package org.cagrid.data.sdkquery41.style.wizard.config;

import gov.nih.nci.cagrid.common.JarUtilities;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.common.ExtensionDataManager;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.ClassMapping;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.cagrid.data.sdkquery41.encoding.SDK41DeserializerFactory;
import org.cagrid.data.sdkquery41.encoding.SDK41SerializerFactory;

public class SchemaMappingConfigStep extends AbstractStyleConfigurationStep {
    
    private ExtensionDataManager dataManager = null;
    
    public SchemaMappingConfigStep(ServiceInformation serviceInfo) {
        super(serviceInfo);
        for (ExtensionType extension : serviceInfo.getExtensions().getExtension()) {
            if (extension.getName().equals("data")) {
                dataManager = new ExtensionDataManager(extension.getExtensionData());
                break;
            }
        }
        if (dataManager == null) {
            throw new IllegalStateException("No data extension found... " +
                    "this really shouldn't be possible, but apparently it happened anyway");
        }
    }


    public void applyConfiguration() throws Exception {
        
    }
    
    
    public CadsrInformation getCurrentCadsrInformation() throws Exception {
        return dataManager.getCadsrInformation();
    }
    
    
    public void setPackageNamespace(String packageName, String namespace) throws Exception {
        dataManager.setMappedNamespaceForPackage(packageName, namespace);
    }
    
    
    public void setClassMapping(String packageName, String className, SchemaElementType element) throws Exception {
        dataManager.setClassElementNameInModel(packageName, className, element.getType());
        element.setClassName(className);
        setSdkSerialization(element);
    }
    
    
    public void unsetClassMapping(String packageName, String className) throws Exception {
        dataManager.setClassElementNameInModel(packageName, className, null);
    }
    
    
    public void mapFromSdkGeneratedSchemas() throws Exception {
        // locate the directory we'll copy schemas in to
        File schemaDir = getServiceSchemaDirectory();
        
        // get cadsr information
        // the config jar will have the xsds in it
        File configJarFile = SharedConfiguration.getInstance().getGeneratedConfigJarFile();
        JarFile configJar = new JarFile(configJarFile);
        
        // copy all the XSDs into a temporary location.  Must do this so 
        // schemas which reference eachother can be resolved
        File tempXsdDir = File.createTempFile("TempCaCoreXSDs", "dir");
        tempXsdDir.delete();
        tempXsdDir.mkdirs();
        Enumeration<JarEntry> entries = configJar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".xsd")) {
                StringBuffer schemaText = JarUtilities.getFileContents(configJar, entry.getName());
                File schemaFile = new File(tempXsdDir, new File(entry.getName()).getName());
                Utils.stringBufferToFile(schemaText, schemaFile.getAbsolutePath());
            }
        }
        configJar.close();
        
        // iterate the schemas and try to map them to domain packages
        for (File xsdFile : tempXsdDir.listFiles()) {
            String schemaPackageName = xsdFile.getName();
            schemaPackageName = schemaPackageName.substring(0, schemaPackageName.length() - 4);
            // find the package of this name
            for (String packageName : dataManager.getCadsrPackageNames()) {
                if (packageName.equals(schemaPackageName)) {
                    System.out.println("Mapping xsd " + xsdFile.getName());
                    // create the namespace type of the XSD
                    NamespaceType nsType = CommonTools.createNamespaceType(xsdFile.getAbsolutePath(), schemaDir);
                    
                    // copy the XSD in to the service's schema dir
                    File xsdOut = new File(schemaDir, xsdFile.getName());
                    Utils.copyFile(xsdFile, xsdOut);
                    nsType.setLocation(xsdOut.getName());
                    
                    // get cadsr package, set namespace, automagic mapping
                    dataManager.setMappedNamespaceForPackage(packageName, nsType.getNamespace());
                    automaticalyMapElementsToClasses(packageName, nsType);
                    
                    // add the namespace to the service definition
                    CommonTools.addNamespace(getServiceInformation().getServiceDescriptor(), nsType);
                    break;
                }
            }
        }
    }
    
    
    private void automaticalyMapElementsToClasses(String packageName, NamespaceType nsType) throws Exception {
        List<ClassMapping> mappings = dataManager.getClassMappingsInPackage(packageName);
        for (ClassMapping mapping : mappings) {
            String className = mapping.getClassName();
            mapping.setElementName(null);
            for (SchemaElementType element : nsType.getSchemaElement()) {
                if (element.getType().equals(className)) {
                    dataManager.setClassElementNameInModel(packageName, className, element.getType());
                    // set the class name and serializers for this element
                    element.setClassName(className);
                    element.setPackageName(packageName);
                    setSdkSerialization(element);
                }
            }
        }
    }
    
    
    private void setSdkSerialization(SchemaElementType element) {
        element.setDeserializer(SDK41DeserializerFactory.class.getName());
        element.setSerializer(SDK41SerializerFactory.class.getName());
    }
    
    
    private File getServiceSchemaDirectory() {
        File schemaDir = new File(getServiceInformation().getBaseDirectory(),
            "schema" + File.separator + getServiceInformation().getIntroduceServiceProperties()
                .getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));
        return schemaDir;
    }
}
