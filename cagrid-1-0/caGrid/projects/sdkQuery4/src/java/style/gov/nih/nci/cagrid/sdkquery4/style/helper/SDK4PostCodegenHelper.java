package gov.nih.nci.cagrid.sdkquery4.style.helper;

import gov.nih.nci.cagrid.common.JarUtilities;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.style.sdkstyle.helpers.PostCodegenHelper;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.sdkquery4.processor.SDK4QueryProcessor;

import java.io.File;
import java.util.jar.JarFile;

/** 
 *  SDK4PostCodegenHelper
 * 
 * @author David Ervin
 * 
 * @created Jan 8, 2008 12:54:15 PM
 * @version $Id: SDK4PostCodegenHelper.java,v 1.1 2008-01-11 20:34:12 dervin Exp $ 
 */
public class SDK4PostCodegenHelper extends PostCodegenHelper {
    
    public static final String CONFIG_FILENAME = "application-config-client.xml";
    public static final String SDK_PROXY_HELPER = "gov.nih.nci.system.client.proxy.ProxyHelperImpl";
    public static final String POJO_PROXY_HELPER = "gov.nih.nci.cagrid.sdkquery4.processor.PojoProxyHelperImpl";

    public void codegenPostProcessStyle(ServiceExtensionDescriptionType desc, ServiceInformation info) throws Exception {
        super.codegenPostProcessStyle(desc, info);
        editApplicationSpringConfigFile(info);
    }

    
    private void editApplicationSpringConfigFile(ServiceInformation info) throws Exception {
        // the config file is in the configured jar file
        String applicationName = CommonTools.getServicePropertyValue(info.getServiceDescriptor(), 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK4QueryProcessor.PROPERTY_APPLICATION_NAME);
        File configJar = new File(info.getBaseDirectory().getAbsolutePath() + File.separator + "lib" 
            + File.separator + applicationName + "-config.jar");
        
        // extract the configuration
        StringBuffer configContents = JarUtilities.getFileContents(new JarFile(configJar), CONFIG_FILENAME);
        
        // replace the default bean proxy class with mine
        int start = -1;
        while ((start = configContents.indexOf(SDK_PROXY_HELPER)) != -1) {
            configContents.replace(start, start + SDK_PROXY_HELPER.length(), POJO_PROXY_HELPER);
        }
        
        // add the edited config to the config jar file
        byte[] configData = configContents.toString().getBytes();
        JarUtilities.insertEntry(configJar, CONFIG_FILENAME, configData);
    }
}
