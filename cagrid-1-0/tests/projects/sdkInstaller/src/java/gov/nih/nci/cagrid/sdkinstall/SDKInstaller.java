package gov.nih.nci.cagrid.sdkinstall;

import gov.nih.nci.cagrid.common.ZipUtilities;
import gov.nih.nci.cagrid.sdkinstall.description.InstallationDescription;
import gov.nih.nci.cagrid.sdkinstall.description.JBossDescription;

import java.io.File;
import java.io.IOException;

/** 
 *  SDKInstaller
 *  Base "driver" class for installing the caCORE sdk
 * 
 * @author David Ervin
 * 
 * @created Jun 13, 2007 10:45:43 AM
 * @version $Id: SDKInstaller.java,v 1.5 2007-06-18 17:23:23 dervin Exp $ 
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
        
        // invoke the build process
        invokeBuildProcess(version, description, installDir);
        
        // invoke the deploy process
        invokeDeployProcess(version, description, installDir);
    }
    
    
    private static void runDeployPropertiesManager(
        SdkVersion version, InstallationDescription description, File sdkDir) 
        throws DeploymentConfigurationException {
        DeployPropertiesManager deployManager = null;
        switch (version) {
            case VERSION_3_2_1:
                deployManager = new Version321DeployPropertiesManager(description, sdkDir);
        }
        deployManager.configureDeployment();
    }
    
    
    private static void invokeBuildProcess(SdkVersion version, 
        InstallationDescription description, File sdkDir) 
        throws BuildInvocationException {
        BuildInvoker builder = null;
        switch (version) {
            case VERSION_3_2_1:
                builder = new Version321BuildInvoker(description, sdkDir);
        }
        builder.invokeBuildProcess();
    }
    
    
    private static void invokeDeployProcess(SdkVersion version,
        InstallationDescription description, File sdkDir) 
        throws DeployInvocationException {
        DeployInvoker invoker = null;
        switch (version) {
            case VERSION_3_2_1:
                invoker = new Version321DeployInvoker(description, sdkDir);
        }
        invoker.invokeDeployProcess();
    }
    
    
    private static void unpackSdk(SdkVersion version, File dir) throws IOException {
        String resourcesDir = "etc" + File.separator + "resources" + File.separator;
        File sdkZip = new File(resourcesDir + version.getZipFileName());
        ZipUtilities.unzip(sdkZip, dir);
    }
}
