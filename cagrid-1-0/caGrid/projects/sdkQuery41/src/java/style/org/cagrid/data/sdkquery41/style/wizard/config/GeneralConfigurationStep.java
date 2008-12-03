package org.cagrid.data.sdkquery41.style.wizard.config;

import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.data.sdkquery41.style.common.SDK41StyleConstants;

public class GeneralConfigurationStep extends AbstractStyleConfigurationStep {
    
    private static final Log LOG = LogFactory.getLog(GeneralConfigurationStep.class);
    
    private File sdkDirectory = null;
    private Properties deployProperties = null;
    
    public GeneralConfigurationStep(ServiceInformation serviceInfo) {
        super(serviceInfo);
    }
    
    
    public File getSdkDirectory() {
        return sdkDirectory;
    }
    
    
    public void setSdkDirectory(File dir) {
        this.sdkDirectory = dir;
        // clear the properties
        this.deployProperties = null;
    }
    

    public void applyConfiguration() throws Exception {
        // TODO Auto-generated method stub

    }
    
    
    public void validateSdkDirectory() throws Exception {
        LOG.debug("Validating the specified caCORE SDK Directory structure");
        if (sdkDirectory == null) {
            throw new NullPointerException("No SDK directory has been set");
        }
        
        // does the directory exist, and is it a directory
        if (!sdkDirectory.exists()) {
            throw new FileNotFoundException("Selected SDK directory ( " + sdkDirectory.getAbsolutePath() + ") does not exist");
        }
        if (!sdkDirectory.isDirectory()) {
            throw new FileNotFoundException("Selected SDK directory ( " + sdkDirectory.getAbsolutePath() + ") is not actually a directory");
        }
        
        // the deploy.properties file
        Properties sdkProperties = null;
        try {
            sdkProperties = getDeployPropertiesFromSdkDir();
        } catch (IOException ex) {
            IOException exception = new IOException("Error loading deploy.properties file from the selected SDK directory: " 
                + ex.getMessage());
            exception.initCause(ex);
            throw exception;
        }
        
        // the XMI file
        File modelsDir = new File(sdkDirectory, "models");
        if (!modelsDir.exists() || !modelsDir.isDirectory()) {
            throw new FileNotFoundException("Models directory (" + modelsDir.getAbsolutePath() + ") not found");
        }
        String modelFileName = sdkProperties.getProperty(SDK41StyleConstants.DeployProperties.MODEL_FILE);
        File modelFile = new File(modelsDir, modelFileName);
        if (!modelFile.exists() || !modelFile.isFile() || !modelFile.canRead()) {
            throw new FileNotFoundException("Could not read model file " + modelFile.getName());
        }
        
        // output directories
        File outputDirectory = new File(sdkDirectory, "output");
        String projectName = sdkProperties.getProperty(SDK41StyleConstants.DeployProperties.PROJECT_NAME);
        File projectOutputDirectory = new File(outputDirectory, projectName);
        if (!projectOutputDirectory.exists() || !projectOutputDirectory.isDirectory()) {
            throw new FileNotFoundException("Project output directory (" + projectOutputDirectory.getAbsolutePath() + ") not found");
        }
        // package directory
        File packageDirectory = new File(projectOutputDirectory, "pacakge");
        if (!packageDirectory.exists() || !packageDirectory.isDirectory()) {
            throw new FileNotFoundException("Project package directory (" + packageDirectory.getAbsolutePath() + ") not found");
        }
        // local-client
        File localClientDir = new File(packageDirectory, "local-client");
        if (!localClientDir.exists() || !localClientDir.isDirectory()) {
            throw new FileNotFoundException("Local-client directory (" + localClientDir.getAbsolutePath() + ") not found");
        }
        File localConfDir = new File(localClientDir, "conf");
        if (!localConfDir.exists() || !localConfDir.isDirectory()) {
            throw new FileNotFoundException("Local-client configuration directory (" + localConfDir.getAbsolutePath() + ") not found");
        }
        File localLibDir = new File(localClientDir, "lib");
        if (!localLibDir.exists() || !localLibDir.isDirectory()) {
            throw new FileNotFoundException("Local-client library directory (" + localLibDir.getAbsolutePath() + ") not found");
        }
        // remote-client
        File remoteClientDir = new File(packageDirectory, "remote-client");
        if (!remoteClientDir.exists() || !remoteClientDir.isDirectory()) {
            throw new FileNotFoundException("Remote-client directory (" + remoteClientDir.getAbsolutePath() + ") not found");
        }
        File remoteConfDir = new File(remoteClientDir, "conf");
        if (!remoteClientDir.exists() || !remoteConfDir.isDirectory()) {
            throw new FileNotFoundException("Remote-client configuration directory (" + remoteConfDir.getAbsolutePath() + ") not found");
        }
        File remoteLibDir = new File(remoteClientDir, "lib");
        if (!remoteLibDir.exists() || !remoteLibDir.exists()) {
            throw new FileNotFoundException("Remote-client library directory (" + remoteLibDir.getAbsolutePath() + ") not found");
        }
    }
    
    
    
    public Properties getDeployPropertiesFromSdkDir() throws IOException {
        if (this.deployProperties == null) {
            LOG.debug("Loading deploy.properties file");
            File propertiesFile = new File(sdkDirectory, "conf" + File.separator + SDK41StyleConstants.DEPLOY_PROPERTIES_FILENAME);
            deployProperties = new Properties();
            FileInputStream fis = new FileInputStream(propertiesFile);
            deployProperties.load(fis);
            fis.close();
        }
        return deployProperties;
    }
}
