package org.cagrid.data.sdkquery41.style.helpers;

import gov.nih.nci.cagrid.common.JarUtilities;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.style.sdkstyle.helpers.PostCodegenHelper;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.io.File;
import java.util.jar.JarFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.data.sdkquery41.processor.PojoProxyHelperImpl;
import org.cagrid.data.sdkquery41.processor.SDK41QueryProcessor;


/**
 * SDK41PostCodegenHelper Edits castor mapping files to prevent it from
 * following associations when serializing and deserializing SDK beans
 * 
 * @author David Ervin
 * 
 * @created Jan 8, 2008 12:54:15 PM
 * @version $Id: SDK41PostCodegenHelper.java,v 1.1 2008/04/17 15:25:51 dervin
 *          Exp $
 */
public class SDK41PostCodegenHelper extends PostCodegenHelper {

    // configs for local and remote are very different
    public static final String LOCAL_CONFIG_FILENAME = "application-config-client.xml";
    public static final String REMOTE_CONFIG_FILENAME = "application-config-client-info.xml";
    
    // SDK provided proxy helper
    public static final String SDK_PROXY_HELPER = "gov.nih.nci.system.client.proxy.ProxyHelperImpl";
    
    // my proxy helper
    public static final String POJO_PROXY_HELPER = PojoProxyHelperImpl.class.getName();

    private static final Log LOG = LogFactory.getLog(SDK41PostCodegenHelper.class);


    public void codegenPostProcessStyle(ServiceExtensionDescriptionType desc, ServiceInformation info) throws Exception {
        super.codegenPostProcessStyle(desc, info);
        editApplicationSpringConfigFile(info);
    }


    private void editApplicationSpringConfigFile(ServiceInformation info) throws Exception {
        // the config file is in the configured jar file
        LOG.debug("Locating config jar");
        String applicationName = CommonTools.getServicePropertyValue(info.getServiceDescriptor(),
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK41QueryProcessor.PROPERTY_APPLICATION_NAME);
        File configJar = new File(info.getBaseDirectory().getAbsolutePath() + File.separator + "lib" + File.separator
            + applicationName + "-config.jar");
        LOG.debug("Config jar found to be " + configJar.getAbsolutePath());
        
        // determine local or remote API
        String useLocalValue = CommonTools.getServicePropertyValue(info.getServiceDescriptor(),
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK41QueryProcessor.PROPERTY_USE_LOCAL_API);
        boolean localApi = Boolean.parseBoolean(SDK41QueryProcessor.DEFAULT_USE_LOCAL_API);
        if (useLocalValue != null && useLocalValue.length() != 0) {
            localApi = Boolean.parseBoolean(useLocalValue);
        }

        // extract the configuration
        String configFilename = localApi ? LOCAL_CONFIG_FILENAME : REMOTE_CONFIG_FILENAME;
        StringBuffer configContents = JarUtilities.getFileContents(new JarFile(configJar), configFilename);

        // replace the default bean proxy class with mine
        LOG.debug("Replacing references to bean proxy class");
        int start = -1;
        while ((start = configContents.indexOf(SDK_PROXY_HELPER)) != -1) {
            configContents.replace(start, start + SDK_PROXY_HELPER.length(), POJO_PROXY_HELPER);
        }

        // add the edited config to the config jar file
        LOG.debug("Inserting edited config in jar");
        byte[] configData = configContents.toString().getBytes();
        JarUtilities.insertEntry(configJar, configFilename, configData);
    }
}
