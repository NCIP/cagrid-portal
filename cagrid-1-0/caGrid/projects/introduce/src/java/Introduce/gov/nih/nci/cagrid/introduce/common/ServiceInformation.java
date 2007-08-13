package gov.nih.nci.cagrid.introduce.common;

import gov.nih.nci.cagrid.common.SchemaValidationException;
import gov.nih.nci.cagrid.common.SchemaValidator;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionsType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.property.ServiceProperties;
import gov.nih.nci.cagrid.introduce.beans.service.ServicesType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class ServiceInformation {

    private ServiceDescription introService;

    private Properties introduceServiceProperties;
    
    private Properties deploymentProperties;

    private File baseDirectory;


    public ServiceInformation(File baseDirectory) throws Exception {
        this.baseDirectory = baseDirectory;

        String introduceXML = baseDirectory + File.separator + IntroduceConstants.INTRODUCE_XML_FILE;
        File introduceXMLFile = new File(introduceXML);
        if (!introduceXMLFile.exists() || !introduceXMLFile.canRead()) {
            throw new Exception("Unable to read the Introduce document:" + introduceXML);
        }

        try {
            SchemaValidator.validate(getIntroduceXSD(), introduceXMLFile);
        } catch (SchemaValidationException e) {
            throw new SchemaValidationException("The Introduce XML document does not adhere to the schema:\n"
                + e.getMessage(), e);
        }

        introService = (ServiceDescription) Utils.deserializeDocument(introduceXML, ServiceDescription.class);
        
        File servicePropertiesFile = new File(baseDirectory.getAbsolutePath() + File.separator
            + IntroduceConstants.INTRODUCE_PROPERTIES_FILE);
        introduceServiceProperties = new Properties();
        introduceServiceProperties.load(new FileInputStream(servicePropertiesFile));
        File deployPropertiesFile = new File(baseDirectory.getAbsolutePath() + File.separator
                + IntroduceConstants.DEPLOY_PROPERTIES_FILE);
        deploymentProperties = new Properties();
        deploymentProperties.load(new FileInputStream(deployPropertiesFile));
    
    }


    public ServiceInformation(ServiceDescription service, Properties properties, File baseDirectory) {
        this.introService = service;
        this.introduceServiceProperties = properties;
        this.baseDirectory = baseDirectory;
    }


    public Properties getIntroduceServiceProperties() {
        return introduceServiceProperties;
    }


    public void setIntroduceServiceProperties(Properties serviceProperties) {
        this.introduceServiceProperties = serviceProperties;
    }
    
    public Properties getDeploymentProperties() {
        return deploymentProperties;
    }


    public void setDeplymentProperties(Properties deploymentProperties) {
        this.deploymentProperties = deploymentProperties;
    }


    public ServicesType getServices() {
        return introService.getServices();
    }


    public void setServices(ServicesType services) {
        this.introService.setServices(services);
    }


    public NamespacesType getNamespaces() {
        return introService.getNamespaces();
    }


    public void setNamespaces(NamespacesType namespaces) {
        this.introService.setNamespaces(namespaces);
    }


    public ExtensionsType getExtensions() {
        return introService.getExtensions();
    }


    public void setExtensions(ExtensionsType extensions) {
        this.introService.setExtensions(extensions);
    }


    public ServiceProperties getServiceProperties() {
        return this.introService.getServiceProperties();
    }


    public void setServiceProperties(ServiceProperties serviceProperties) {
        this.introService.setServiceProperties(serviceProperties);
    }


    public File getBaseDirectory() {
        return baseDirectory;
    }


    public ServiceDescription getServiceDescriptor() {
        return introService;
    }


    public void persistInformation() throws Exception {
        Utils.serializeDocument(baseDirectory.getAbsolutePath() + File.separator
            + IntroduceConstants.INTRODUCE_XML_FILE, introService, IntroduceConstants.INTRODUCE_SKELETON_QNAME);
        File servicePropertiesFile = new File(baseDirectory.getAbsolutePath() + File.separator
            + IntroduceConstants.INTRODUCE_PROPERTIES_FILE);
        introduceServiceProperties.store(new FileOutputStream(servicePropertiesFile), "Introduce Properties");
        File deploymentPropertiesFile = new File(baseDirectory.getAbsolutePath() + File.separator
                + IntroduceConstants.DEPLOY_PROPERTIES_FILE);
        FileOutputStream fos = new FileOutputStream(deploymentPropertiesFile);
            deploymentProperties.store(fos, "Service Deployment Properties");
        fos.close();
    }


    /**
     * TODO: requires running directory to be introduce's directory... need a
     * better way
     */
    private String getIntroduceXSD() {
        return new File("schema" + File.separator + IntroduceConstants.INTRODUCE_XML_XSD_FILE).getAbsolutePath();
    }
    
    public void createArchive() throws Exception {
        // create the archive
        long id = System.currentTimeMillis();

        getIntroduceServiceProperties().setProperty(IntroduceConstants.INTRODUCE_SKELETON_TIMESTAMP,
            String.valueOf(id));
        getIntroduceServiceProperties().store(
            new FileOutputStream(getBaseDirectory().getAbsolutePath() + File.separator
                + IntroduceConstants.INTRODUCE_PROPERTIES_FILE), "Introduce Properties");

        ResourceManager.createArchive(String.valueOf(id), getIntroduceServiceProperties().getProperty(
            IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME), getBaseDirectory().getAbsolutePath());
    }


}
