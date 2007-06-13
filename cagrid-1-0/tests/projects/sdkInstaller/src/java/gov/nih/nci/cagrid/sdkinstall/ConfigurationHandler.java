package gov.nih.nci.cagrid.sdkinstall;

import gov.nih.nci.cagrid.sdkinstall.description.InstallationDescription;

import java.io.File;

/** 
 *  ConfigurationHandler
 *  Handles configuration of the caCORE SDK based on the installation
 *  description passed in to the creation utility
 * 
 * @author David Ervin
 * 
 * @created Jun 13, 2007 1:59:25 PM
 * @version $Id: ConfigurationHandler.java,v 1.1 2007-06-13 18:35:00 dervin Exp $ 
 */
public class ConfigurationHandler {

    private InstallationDescription description;
    private DeployPropertiesManager deployPropertiesManager;
    
    public ConfigurationHandler(InstallationDescription description, File sdkDirectoy, SdkVersion version) {
        this.description = description;
        switch (version) {
            case VERSION_3_2_1:
                deployPropertiesManager = new Version321DeployPropertiesManager(description, sdkDirectoy);
        }
    }
    
    
    public void configureJBoss() {
        
    }
    
    
    public void configureMySQL() {
        
    }
}
