package gov.nih.nci.cagrid.sdkinstall;

import gov.nih.nci.cagrid.common.ZipUtilities;
import gov.nih.nci.cagrid.sdkinstall.description.InstallationDescription;
import gov.nih.nci.cagrid.sdkinstall.description.JBossDescription;
import gov.nih.nci.cagrid.sdkinstall.description.MySQLDescription;

import java.io.File;
import java.io.IOException;

/** 
 *  SDKInstaller
 *  Base "driver" class for installing the caCORE sdk
 * 
 * @author David Ervin
 * 
 * @created Jun 13, 2007 10:45:43 AM
 * @version $Id: SDKInstaller.java,v 1.2 2007-06-15 16:57:33 dervin Exp $ 
 */
public class SDKInstaller {

    public static void installSdk(
        SdkVersion version, InstallationDescription description) throws Exception {
        installSdk(version, description, 
            File.createTempFile("caCORE", "_" + System.currentTimeMillis()));
    }
    
    
    public static void installSdk(
        SdkVersion version, InstallationDescription description, File installDir) 
        throws Exception {
        // start by unpacking the caCORE SDK
        unpackSdk(version, installDir);
        // deal with JBoss
        JBossDescription jbossDesc = description.getJBossDescription();
        if (jbossDesc.getInstallJboss() != null && jbossDesc.getInstallJboss().booleanValue()) {
            File jbossZip = new File(jbossDesc.getJbossZipFile());
            File jbossDir = new File(jbossDesc.getJbossLocation());
            ZipUtilities.unzip(jbossZip, jbossDir);
        }
        // handle the specialized deployment configuration
        runDeployPropertiesManager(version, description, installDir);
    }
    
    
    private static void runDeployPropertiesManager(
        SdkVersion version, InstallationDescription description, File sdkDir) {
        DeployPropertiesManager deployManager = null;
        switch (version) {
            case VERSION_3_2_1:
                deployManager = new Version321DeployPropertiesManager(description, sdkDir);
        }
        
    }
    
    
    private static void unpackSdk(SdkVersion version, File dir) throws IOException {
        String resourcesDir = "etc" + File.separator + "resources" + File.separator;
        File sdkZip = new File(resourcesDir + version.getZipFileName());
        ZipUtilities.unzip(sdkZip, dir);
    }
}
