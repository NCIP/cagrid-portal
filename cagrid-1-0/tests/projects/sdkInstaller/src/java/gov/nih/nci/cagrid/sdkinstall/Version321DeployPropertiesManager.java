package gov.nih.nci.cagrid.sdkinstall;

import gov.nih.nci.cagrid.sdkinstall.description.InstallationDescription;

import java.io.File;
import java.io.IOException;

/** 
 *  Version321DeployPropertiesManager
 *  Manages the deploy.properties file for SDK version 3.2.1
 * 
 * @author David Ervin
 * 
 * @created Jun 13, 2007 2:25:49 PM
 * @version $Id: Version321DeployPropertiesManager.java,v 1.3 2007-06-15 18:49:15 dervin Exp $ 
 */
public class Version321DeployPropertiesManager extends DeployPropertiesManager {
    // general properties
    public static final String PROPERTY_ENABLE_WRITABLE_API = "disable_writable_api_generation";
    public static final String PROPERTY_PROJECT_NAME = "project_name";
    public static final String PROPERTY_WEB_PROJECT_NAME = "webservice_name";
    public static final String JAVA_HOME = "java_home";
    
    // jboss related constants
    public static final String PROPERTY_J2SE_HOME = "j2se_container_home";
    public static final String PROPERTY_WEB_SERVER_PORT = "web_server_port";
    
    // mysql stuff
    public static final String PROPERTY_MYSQL_HOME = "mysql_home";
    public static final String PROPERTY_MYSQL_SERVER_NAME = "db_server_name";
    // db user
    public static final String PROPERTY_MYSQL_USER = "db_user";
    public static final String PROPERTY_MYSQL_PASSWD = "db_password";
    public static final String PROPERTY_MYSQL_CREATE_USER = "create_mysql_user";
    // db schema
    public static final String PROPERTY_MYSQL_SCHEMA = "schema_name";
    public static final String PROPERTY_MYSQL_SCHEMA_FILENAME = "ddl_filename";
    public static final String PROPERTY_MYSQL_CREATE_SCHEMA = "create_schema";
    public static final String PROPERTY_MYSQL_IMPORT_DATA = "import_data";
    public static final String PROPERTY_MYSQL_DATA_FILENAME = "datadump_name";    
    
    private PropertiesPreservingComments deployProperties;

    public Version321DeployPropertiesManager(InstallationDescription description, File sdkDir) {
        super(description, sdkDir);
    }


    public void configureDeployment() throws DeploymentConfigurationException {
        File deployPropertiesFile = new File(getSdkDirectory().getAbsolutePath() 
            + File.separator + "conf" + File.separator + "deploy.properties");
        if (!deployPropertiesFile.exists()) {
            throw new DeploymentConfigurationException(
                "Config file " + deployPropertiesFile.getAbsolutePath() + " does not exist!");
        }
        if (!deployPropertiesFile.canRead()) {
            throw new DeploymentConfigurationException(
                "Config file " + deployPropertiesFile.getAbsolutePath() + " cannot be read!");
        }
        deployProperties = new PropertiesPreservingComments();
        try {
            deployProperties.load(deployPropertiesFile);
        } catch (IOException ex) {
            throw new DeploymentConfigurationException(
                "Error loading deployment properties file: " + ex.getMessage(), ex);
        }
        
        setGeneralParameters();
        setJBossParameters();
    }
    
    
    private void setGeneralParameters() {
        String projectName = getInstallationDescription().getApplicationName();
        String projectWebName = projectName + "Service";
        deployProperties.setProperty(PROPERTY_PROJECT_NAME, projectName);
        deployProperties.setProperty(PROPERTY_WEB_PROJECT_NAME, projectWebName);
        
        // bass-ackwards
        String disableWritable = "no";
        if (getInstallationDescription().getEnableWritableApi() != null) {
            disableWritable = getInstallationDescription()
                .getEnableWritableApi().booleanValue() ? "no" : "yes";
        }
        deployProperties.setProperty(PROPERTY_ENABLE_WRITABLE_API, disableWritable);
    }
    
    
    private void setJBossParameters() {
        
    }
}
