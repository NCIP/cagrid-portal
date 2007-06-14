package gov.nih.nci.cagrid.introduce.extensions.sdk.discovery;

import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;


/**
 * @author oster
 */
public class SDKExecutor {

    public static final String ANT_TARGETS = "generate-schemas";


    // public static final String ANT_TARGETS =
    // "build-pojo-beans-xsd-xml-mapping";

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

    // PROCESS
    // ==========
    // template the SDK config with values from user
    // execute the SDK
    // copy the XSD(s) to service schema dir
    // copy castor mapping to service common package
    // add castorMapping property to service and client configs (wsdd)
    // compile beans; build jar containing source and bean classes
    // copy jar to service lib
    // add schema types to introduce namespaces
    // set serializer/deserializer to sdk for all schema types

    public static void runSDK(File baseDirectory, SDKGenerationInformation info) throws SDKExecutionException {
        String extensionDir = baseDirectory.getAbsolutePath() + File.separator + "sdk";

        Process p;
        try {

            String cmd = CommonTools.getAntCommand(ANT_TARGETS, extensionDir);
            p = CommonTools.createAndOutputProcess(cmd);
            p.waitFor();
        } catch (Exception e) {
            throw new SDKExecutionException("Problem with SDK invocation:" + e.getMessage(), e);
        }

        if (p.exitValue() != 0) {
            throw new SDKExecutionException("SDK invocation exited abnormally with exit code:" + p.exitValue());
        }

    }


    public static void main(String[] args) {

    }

}
