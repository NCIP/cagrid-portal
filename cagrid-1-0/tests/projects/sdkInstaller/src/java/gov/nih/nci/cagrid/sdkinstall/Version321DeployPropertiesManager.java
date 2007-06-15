package gov.nih.nci.cagrid.sdkinstall;

import gov.nih.nci.cagrid.sdkinstall.description.InstallationDescription;

import java.io.File;

/** 
 *  Version321DeployPropertiesManager
 *  Manages the deploy.properties file for SDK version 3.2.1
 * 
 * @author David Ervin
 * 
 * @created Jun 13, 2007 2:25:49 PM
 * @version $Id: Version321DeployPropertiesManager.java,v 1.2 2007-06-15 16:57:33 dervin Exp $ 
 */
public class Version321DeployPropertiesManager extends DeployPropertiesManager {

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
        
    }
}
