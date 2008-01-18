package gov.nih.nci.cagrid.sdkquery4.style.wizard.config;

import java.io.File;
import java.io.IOException;

import org.cagrid.grape.utils.CompositeErrorDialog;

import gov.nih.nci.cagrid.common.JarUtilities;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.common.CastorMappingUtil;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.sdkquery4.processor.SDK4QueryProcessor;
import gov.nih.nci.cagrid.sdkquery4.style.common.SDK4StyleConstants;

/** 
 *  QueryProcessorBaseConfigurationStep
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Jan 18, 2008 3:26:22 PM
 * @version $Id: QueryProcessorBaseConfigurationStep.java,v 1.1 2008-01-18 21:18:57 dervin Exp $ 
 */
public class QueryProcessorBaseConfigurationStep extends AbstractStyleConfigurationStep {
    
    private String applicationName = null;
    private String beansJarLocation = null;
    private String configurationDir = null;
    private boolean useLocalApi;
    private String ormJarLocation = null;
    private boolean caseInsensitiveQueries;
    private String hostName = null;
    private Integer hostPort = null;

    public QueryProcessorBaseConfigurationStep(ServiceInformation serviceInfo) {
        super(serviceInfo);
    }

    
    public void applyConfiguration() throws Exception {
        storeConfigurationProperties();
        performFileOperations();
    }
    
    
    private void storeConfigurationProperties() {
        // store configuration properties for the query processor
        ServiceDescription desc = getServiceInformation().getServiceDescriptor();
        // store the beans jar filename
        File beansJarFile = new File(beansJarLocation);
        CommonTools.setServiceProperty(desc,
            SDK4StyleConstants.BEANS_JAR_FILENAME, beansJarFile.getName(), false);
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_APPLICATION_NAME, 
            applicationName, false);
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_CASE_INSENSITIVE_QUERYING,
            String.valueOf(caseInsensitiveQueries), false);
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_USE_LOCAL_API,
            String.valueOf(useLocalApi), false);
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_ORM_JAR_NAME,
            useLocalApi ? new File(ormJarLocation).getName() : "", false);
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_HOST_NAME,
            useLocalApi ? "" : hostName, false);
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_HOST_PORT,
            useLocalApi ? "" : hostPort.toString(), false);
    }
    
    
    private void performFileOperations() {
        File beansJar = new File(beansJarLocation);
        File beansDest = new File(getServiceInformation().getBaseDirectory(), 
            "lib" + File.separator + beansJar.getName());
        try {
            Utils.copyFile(beansJar, beansDest);
        } catch (IOException ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog("Error copying beans jar", ex.getMessage(), ex);
        }
        // jar up the config dir, then copy it in too
        File configDir = new File(configurationDir);
        File configJar = new File(getServiceInformation().getBaseDirectory(), 
            "lib" + File.separator + applicationName + "-config.jar");
        try {
            JarUtilities.jarDirectory(configDir, configJar);
        } catch (IOException ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog("Error packaging configuration directory", ex.getMessage(), ex);
        }
        // grab the castor marshalling and unmarshalling xml mapping files
        // from the config dir and copy them into the service's package structure
        try {
            StringBuffer marshallingMappingFile = Utils.fileToStringBuffer(
                new File(configDir, CastorMappingUtil.CASTOR_MARSHALLING_MAPPING_FILE));
            StringBuffer unmarshallingMappingFile = Utils.fileToStringBuffer(
                new File(configDir, CastorMappingUtil.CASTOR_UNMARSHALLING_MAPPING_FILE));
            // copy the mapping files to the service's source dir + base package name
            String marshallOut = CastorMappingUtil.getMarshallingCastorMappingFileName(getServiceInformation());
            String unmarshallOut = CastorMappingUtil.getUnmarshallingCastorMappingFileName(getServiceInformation());
            Utils.stringBufferToFile(marshallingMappingFile, marshallOut);
            Utils.stringBufferToFile(unmarshallingMappingFile, unmarshallOut);
        } catch (IOException ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog("Error extracting castor mapping files", ex.getMessage(), ex);
        }
        
        // if local API, copy in the orm jar
        if (useLocalApi) {
            File ormFile = new File(ormJarLocation);
            File ormDest = new File(getServiceInformation().getBaseDirectory(), 
                "lib" + File.separator + ormFile.getName());
            try {
                Utils.copyFile(ormFile, ormDest);
            } catch (IOException ex) {
                ex.printStackTrace();
                CompositeErrorDialog.showErrorDialog("Error copying orm jar", ex.getMessage(), ex);
            }
        }
    }


    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }


    public void setBeansJarLocation(String beansJarLocation) {
        this.beansJarLocation = beansJarLocation;
    }


    public void setCaseInsensitiveQueries(boolean caseInsensitiveQueries) {
        this.caseInsensitiveQueries = caseInsensitiveQueries;
    }


    public void setConfigurationDir(String configurationDir) {
        this.configurationDir = configurationDir;
    }


    public void setHostName(String hostName) {
        this.hostName = hostName;
    }


    public void setHostPort(Integer hostPort) {
        this.hostPort = hostPort;
    }


    public void setOrmJarLocation(String ormJarLocation) {
        this.ormJarLocation = ormJarLocation;
    }


    public void setUseLocalApi(boolean useLocalApi) {
        this.useLocalApi = useLocalApi;
    }
}
