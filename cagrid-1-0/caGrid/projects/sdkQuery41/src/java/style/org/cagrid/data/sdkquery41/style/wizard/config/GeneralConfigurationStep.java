package org.cagrid.data.sdkquery41.style.wizard.config;

import gov.nih.nci.cagrid.common.JarUtilities;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.common.CastorMappingUtil;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.data.sdkquery41.style.common.SDK41StyleConstants;
import org.cagrid.grape.utils.CompositeErrorDialog;

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
        LOG.debug("Applying configuration");
        // just in case...
        validateSdkDirectory();
        
        // new data service's lib directory
        File libOutDir = new File(getServiceInformation().getBaseDirectory(), "lib");
        
        String projectName = getDeployPropertiesFromSdkDir().getProperty(
            SDK41StyleConstants.DeployProperties.PROJECT_NAME);
        File remoteClientDir = new File(sdkDirectory, 
            "output" + File.separator + projectName + File.separator + 
            "package" + File.separator + "remote-client");
        File localClientDir = new File(sdkDirectory, 
            "output" + File.separator + projectName + File.separator + 
            "package" + File.separator + "local-client");
        
        // wrap up the remote-client config files as a jar file so it'll be on the classpath
        LOG.debug("Creating a jar to contain the remote configuration of the caCORE SDK system");
        File remoteConfigDir = new File(remoteClientDir, "conf");
        String configJarName = projectName + "-config.jar";
        File configJar = new File(libOutDir, configJarName);
        JarUtilities.jarDirectory(remoteConfigDir, configJar);
        // TODO: if user wants to use the local API, we have to include the local-client/conf files in the config jar
        
        // TODO: local / remote API detection, lib dir, etc

        // set the config jar in the shared configuration
        SharedConfiguration.getInstance().setGeneratedConfigJarFile(configJar);
        
        // copy in libraries from the remote or local lib dir, depending on if user wants local or remote API
        LOG.debug("Copying libraries from remote client directory");
        File[] remoteLibs = new File(remoteClientDir, "lib").listFiles(new FileFilters.JarFileFilter());
        for (File lib : remoteLibs) {
            File libOutput = new File(libOutDir, lib.getName());
            Utils.copyFile(lib, libOutput);
        }
        
        // grab the castor marshalling and unmarshalling xml mapping files
        // from the config dir and copy them into the service's package structure
        try {
            LOG.debug("Extracting castor marshalling and unmarshalling files");
            StringBuffer marshallingMappingFile = Utils.fileToStringBuffer(
                new File(remoteConfigDir, CastorMappingUtil.CASTOR_MARSHALLING_MAPPING_FILE));
            StringBuffer unmarshallingMappingFile = Utils.fileToStringBuffer(
                new File(remoteConfigDir, CastorMappingUtil.CASTOR_UNMARSHALLING_MAPPING_FILE));
            // copy the mapping files to the service's source dir + base package name
            String marshallOut = CastorMappingUtil.getMarshallingCastorMappingFileName(getServiceInformation());
            String unmarshallOut = CastorMappingUtil.getUnmarshallingCastorMappingFileName(getServiceInformation());
            Utils.stringBufferToFile(marshallingMappingFile, marshallOut);
            Utils.stringBufferToFile(unmarshallingMappingFile, unmarshallOut);
        } catch (IOException ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog("Error extracting castor mapping files", ex.getMessage(), ex);
        }
        
        // set up the shared configuration
        SharedConfiguration.getInstance().setSdkDirectory(getSdkDirectory());
        SharedConfiguration.getInstance().setServiceInfo(getServiceInformation());
        SharedConfiguration.getInstance().setSdkDeployProperties(getDeployPropertiesFromSdkDir());
    }
    
    
    public void validateSdkDirectory() throws Exception {
        LOG.debug("Validating the specified caCORE SDK Directory structure");
        if (sdkDirectory == null) {
            throw new NullPointerException("No SDK directory has been set");
        }
        
        // does the directory exist, and is it a directory?
        if (!sdkDirectory.exists()) {
            throw new FileNotFoundException("Selected SDK directory ( " 
                + sdkDirectory.getAbsolutePath() + ") does not exist");
        }
        if (!sdkDirectory.isDirectory()) {
            throw new FileNotFoundException("Selected SDK directory ( " 
                + sdkDirectory.getAbsolutePath() + ") is not actually a directory");
        }
        
        // the deploy.properties file
        Properties sdkProperties = null;
        try {
            sdkProperties = getDeployPropertiesFromSdkDir();
        } catch (IOException ex) {
            IOException exception = new IOException(
                "Error loading deploy.properties file from the selected SDK directory: " 
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
        File packageDirectory = new File(projectOutputDirectory, "package");
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
            File propertiesFile = getDeployPropertiesFile();
            deployProperties = new Properties();
            FileInputStream fis = new FileInputStream(propertiesFile);
            deployProperties.load(fis);
            fis.close();
        }
        return deployProperties;
    }
    
    
    public File getDeployPropertiesFile() {
        if (sdkDirectory != null) {
            File propertiesFile = new File(sdkDirectory, "conf" + File.separator + SDK41StyleConstants.DEPLOY_PROPERTIES_FILENAME);
            return propertiesFile;
        }
        return null;
    }
}
