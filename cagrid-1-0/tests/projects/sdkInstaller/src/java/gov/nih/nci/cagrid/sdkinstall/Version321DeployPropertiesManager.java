package gov.nih.nci.cagrid.sdkinstall;

import gov.nih.nci.cagrid.sdkinstall.description.InstallationDescription;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

/** 
 *  Version321DeployPropertiesManager
 *  Manages the deploy.properties file for SDK version 3.2.1
 * 
 * @author David Ervin
 * 
 * @created Jun 13, 2007 2:25:49 PM
 * @version $Id: Version321DeployPropertiesManager.java,v 1.4 2007-06-18 14:24:41 dervin Exp $ 
 */
public class Version321DeployPropertiesManager extends DeployPropertiesManager {
    // general properties
    public static final String PROPERTY_ENABLE_WRITABLE_API = "disable_writable_api_generation";
    public static final String PROPERTY_PROJECT_NAME = "project_name";
    public static final String PROPERTY_WEB_PROJECT_NAME = "webservice_name";
    public static final String PROPERTY_JAVA_HOME = "java_home";
    
    // jboss related constants
    public static final String PROPERTY_J2SE_CONTAINER_HOME = "j2se_container_home";
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
        setMysqlParameters();
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
        
        String javaHome = System.getenv("JAVA_HOME");
        deployProperties.setProperty(PROPERTY_JAVA_HOME, javaHome);
    }
    
    
    private void setJBossParameters() {
        String jbossHome = getInstallationDescription().getJBossDescription().getJbossLocation();
        deployProperties.setProperty(PROPERTY_J2SE_CONTAINER_HOME, jbossHome);
        BigInteger jbossWebPort = getInstallationDescription().getJBossDescription().getJbossPort();
        if (jbossWebPort != null) {
            deployProperties.setProperty(PROPERTY_WEB_SERVER_PORT, jbossWebPort.toString());
        }
    }
    
    
    private void setMysqlParameters() {
        String mysqlHome = getInstallationDescription().getMySQLDescription().getMysqlLocation();
        String mysqlHost = getInstallationDescription().getMySQLDescription().getServerInformation().getHostname();
        deployProperties.setProperty(PROPERTY_MYSQL_HOME, mysqlHome);
        deployProperties.setProperty(PROPERTY_MYSQL_SERVER_NAME, mysqlHost);
        
        String dbUser = getInstallationDescription().getMySQLDescription().getServerInformation().getUsername();
        String passwd = getInstallationDescription().getMySQLDescription().getServerInformation().getPassword();
        String createUserValue = "no";
        Boolean createUser = getInstallationDescription().getMySQLDescription().getServerInformation().getCreateUser();
        if (createUser != null) {
            createUserValue = createUser.booleanValue() ? "yes" : "no";
        }
        deployProperties.setProperty(PROPERTY_MYSQL_USER, dbUser);
        deployProperties.setProperty(PROPERTY_MYSQL_PASSWD, passwd);
        deployProperties.setProperty(PROPERTY_MYSQL_CREATE_USER, createUserValue);
        
        String schemaName = getInstallationDescription().getMySQLDescription().getDataOptions().getSchemaName();
        String createSchemaValue = "no";
        Boolean createSchema = getInstallationDescription().getMySQLDescription().getDataOptions().getCreateSchema();
        if (createSchema != null) {
            createSchemaValue = createSchema.booleanValue() ? "yes" : "no";
        }
        String schemaFileName = getInstallationDescription().getMySQLDescription().getDataOptions().getSchemaFilename();
        String importDataValue = "no";
        Boolean importData = getInstallationDescription().getMySQLDescription().getDataOptions().getLoadData();
        if (importData != null) {
            importDataValue = importData.booleanValue() ? "yes" : "no";
        }
        String dataDumpFile = getInstallationDescription().getMySQLDescription().getDataOptions().getDataFilename();
        deployProperties.setProperty(PROPERTY_MYSQL_SCHEMA, schemaName);
        deployProperties.setProperty(PROPERTY_MYSQL_SCHEMA_FILENAME, schemaFileName);
        deployProperties.setProperty(PROPERTY_MYSQL_CREATE_SCHEMA, createSchemaValue);
        deployProperties.setProperty(PROPERTY_MYSQL_IMPORT_DATA, importDataValue);
        deployProperties.setProperty(PROPERTY_MYSQL_DATA_FILENAME, dataDumpFile);
    }
}
