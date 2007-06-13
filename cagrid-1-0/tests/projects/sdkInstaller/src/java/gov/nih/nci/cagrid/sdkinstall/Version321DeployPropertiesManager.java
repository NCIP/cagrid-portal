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
 * @version $Id: Version321DeployPropertiesManager.java,v 1.1 2007-06-13 18:35:00 dervin Exp $ 
 */
public class Version321DeployPropertiesManager extends DeployPropertiesManager {

    public Version321DeployPropertiesManager(InstallationDescription description, File sdkDir) {
        super(description, sdkDir);
    }


    public void update() {

    }
}
