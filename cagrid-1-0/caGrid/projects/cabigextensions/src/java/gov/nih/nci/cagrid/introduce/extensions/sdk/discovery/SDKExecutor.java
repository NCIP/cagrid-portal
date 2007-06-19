package gov.nih.nci.cagrid.introduce.extensions.sdk.discovery;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author oster
 */
public class SDKExecutor {
    private static final String SDK_CONFIG_MODELS_DIR = "models/xmi";
    private static final String SDK_CONFIG_PROP_EXCLUDE_PACKAGE = "exclude_package";
    private static final String SDK_CONFIG_PROP_INCLUDE_PACKAGE = "include_package";
    private static final String SDK_CONFIG_PROP_FIXED_FILENAME = "fixed_filename";
    private static final String SDK_CONFIG_PROP_MODEL_FILENAME = "model_filename";
    private static final String SDK_CONFIG_PROP_VERSION = "version";
    private static final String SDK_CONFIG_PROP_CONTEXT = "context";
    private static final String SDK_CONFIG_PROP_CLASSIFICATION = "classification";
    private static final String SDK_CONFIG_PROP_PROJECT_NAME = "project_name";

    private static final String SDK_CONFIG_FILE_XML_PROPERTIES = "conf/resources/server/xml.properties";
    private static final String SDK_CONFIG_FILE_DEPLOY_PROPERTIES = "conf/deploy.properties";

    private static final String XSD_ONLY_ANT_TARGETS = "generate-schemas";
    public static final String XSD_CASTOR_ANT_TARGETS = "build-pojo-beans-xsd-xml-mapping";

    protected static Log LOG = LogFactory.getLog(SDKExecutor.class.getName());


    // conf/resources/server/xml.properties:
    // ==========
    // context = caDSR context @CONTEXT@
    // version = caDSR Project version @VERSION@
    // classification = caDSR Project short name @CLASSIFICATION@

    // conf/deploy.properties:
    // ==========
    // model_filename = XMI file from user @MODEL_NAME@
    // fixed_filename = create from model_filename (fixed-$model_filename)
    // include_package = package regex to include
    // exclude_package = package regex to exclude

    public static SDKExecutionResult runSDK(File sdkDirectory, SDKGenerationInformation info)
        throws SDKExecutionException {
        LOG.debug("SDK Directory:" + sdkDirectory.getAbsolutePath());
        LOG.debug("SDK Generation Info:" + info);

        File workDir = null;
        try {
            // create temporary working area
            workDir = File.createTempFile("SDKWorkArea", "");
            workDir.delete();
            workDir.mkdir();
            workDir.deleteOnExit();
            LOG.debug("SDK Work directory:" + workDir.getAbsolutePath());
            // copy sdk to it
            Utils.copyDirectory(sdkDirectory, workDir);
        } catch (IOException e) {
            String error = "Problem creating work area for SDK execution.";
            LOG.error(error, e);
            throw new SDKExecutionException(error, e);
        }

        // copy the model into the model dir
        File modelFile = new File(info.getXmiFile());
        File modelsDir = new File(workDir, SDK_CONFIG_MODELS_DIR);
        if (!(modelsDir.exists() && modelsDir.canWrite())) {
            String error = "Expected to find writable directory for models at:" + modelsDir.getAbsolutePath();
            LOG.error(error);
            throw new SDKExecutionException(error);
        }
        File destModelFile = null;
        try {
            destModelFile = new File(modelsDir, modelFile.getName());
            LOG.debug("Copying model file to: " + destModelFile);
            Utils.copyFile(modelFile, destModelFile);
        } catch (IOException e) {
            String error = "Problem copying model file from: " + modelFile.getAbsolutePath() + " to:"
                + destModelFile.getAbsolutePath();
            LOG.error(error, e);
            throw new SDKExecutionException(error, e);

        }

        // template the SDK config with values from user
        LOG.debug("Applying configuration changes.");
        applyConfigurationChanges(workDir, info);

        // execute the SDK
        String antTargets = info.isXSDOnly() ? XSD_ONLY_ANT_TARGETS : XSD_CASTOR_ANT_TARGETS;
        LOG.debug("Invoking ant targets (" + antTargets + ")");
        invokeAnt(antTargets, workDir);

        // create and validate the result
        SDKExecutionResult result = new SDKExecutionResult(workDir, info);
        LOG.debug("Validating results:" + result);
        result.validate();

        LOG.debug("Returning results.");
        return result;

    }


    private static void applyConfigurationChanges(File workDir, SDKGenerationInformation info)
        throws SDKExecutionException {

        applyXMLConfigurationChanges(workDir, info);
        applyDeployConfigurationChanges(workDir, info);
    }


    /**
     * @param workDir
     * @throws SDKExecutionException
     */
    private static void applyDeployConfigurationChanges(File workDir, SDKGenerationInformation info)
        throws SDKExecutionException {
        // conf/deploy.properties:
        // ==========
        File deployPropertiesFile = new File(workDir, SDK_CONFIG_FILE_DEPLOY_PROPERTIES);
        if (!(deployPropertiesFile.exists() && deployPropertiesFile.canRead())) {
            String error = "Expected readible properties file at location: " + deployPropertiesFile.getAbsolutePath();
            LOG.error(error);
            throw new SDKExecutionException(error);
        }
        Properties deployProperties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(deployPropertiesFile);
            deployProperties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            String error = "Problem loading properties file at location: " + deployPropertiesFile.getAbsolutePath();
            LOG.error(error, e);
            throw new SDKExecutionException(error, e);
        }

        // project_name = = caDSR Project short name
        deployProperties.setProperty(SDK_CONFIG_PROP_PROJECT_NAME, info.getCaDSRProjectName());
        // model_filename = XMI file from user @MODEL_NAME@
        File modelfile = new File(info.getXmiFile());
        deployProperties.setProperty(SDK_CONFIG_PROP_MODEL_FILENAME, modelfile.getName());
        // fixed_filename = create from model_filename (fixed-$model_filename)
        deployProperties.setProperty(SDK_CONFIG_PROP_FIXED_FILENAME, "fixed-" + modelfile.getName());
        // include_package = package regex to include
        deployProperties.setProperty(SDK_CONFIG_PROP_INCLUDE_PACKAGE, info.getPackageIncludes());
        // exclude_package = package regex to exclude
        deployProperties.setProperty(SDK_CONFIG_PROP_EXCLUDE_PACKAGE, info.getPackageExcludes());

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(deployPropertiesFile);
            deployProperties.store(fileOutputStream, "Generated by:" + SDKExecutor.class.getCanonicalName());
            fileOutputStream.close();
            LOG.debug("Saving properties:" + deployProperties.toString());
        } catch (IOException e) {
            String error = "Problem saving edited properties file at location: "
                + deployPropertiesFile.getAbsolutePath();
            LOG.error(error, e);
            throw new SDKExecutionException(error, e);
        }
    }


    /**
     * @param workDir
     * @param info
     * @return
     * @throws SDKExecutionException
     */
    private static void applyXMLConfigurationChanges(File workDir, SDKGenerationInformation info)
        throws SDKExecutionException {
        // conf/resources/server/xml.properties:
        File xmlPropertiesFile = new File(workDir, SDK_CONFIG_FILE_XML_PROPERTIES);
        if (!(xmlPropertiesFile.exists() && xmlPropertiesFile.canRead())) {
            String errror = "Expected readible properties file at location: " + xmlPropertiesFile.getAbsolutePath();
            LOG.error(errror);
            throw new SDKExecutionException(errror);
        }
        Properties xmlProperties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(xmlPropertiesFile);
            xmlProperties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            String error = "Problem loading properties file at location: " + xmlPropertiesFile.getAbsolutePath();
            LOG.error(error, e);
            throw new SDKExecutionException(error, e);
        }

        // ==========
        // context = caDSR context @CONTEXT@
        xmlProperties.setProperty(SDK_CONFIG_PROP_CONTEXT, info.getCaDSRcontext());
        // version = caDSR Project version @VERSION@
        xmlProperties.setProperty(SDK_CONFIG_PROP_VERSION, info.getCaDSRProjectVersion());
        // classification = caDSR Project short name @CLASSIFICATION@
        xmlProperties.setProperty(SDK_CONFIG_PROP_CLASSIFICATION, info.getCaDSRProjectName());

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(xmlPropertiesFile);
            xmlProperties.store(fileOutputStream, "Generated by:" + SDKExecutor.class.getCanonicalName());
            fileOutputStream.close();
            LOG.debug("Saving properties:" + xmlProperties.toString());
        } catch (IOException e) {
            String error = "Problem saving edited properties file at location: " + xmlPropertiesFile.getAbsolutePath();
            LOG.error(error, e);
            throw new SDKExecutionException(error, e);
        }
    }


    private static void invokeAnt(String targets, File sdkDirectory) throws SDKExecutionException {
        Process p;
        try {
            String cmd = CommonTools.getAntCommand(targets, sdkDirectory.getPath());
            LOG.debug("Running ant command:" + cmd);
            p = CommonTools.createAndOutputProcess(cmd);
            p.waitFor();
        } catch (Exception e) {
            String error = "Problem with SDK invocation:" + e.getMessage();
            LOG.error(error, e);
            throw new SDKExecutionException(error, e);
        }

        if (p.exitValue() != 0) {
            String error = "SDK invocation exited abnormally with exit code:" + p.exitValue()
                + ".  Check error log for details.";
            LOG.error(error);
            throw new SDKExecutionException(error);
        }
    }

}
