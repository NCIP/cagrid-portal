package gov.nih.nci.cagrid.introduce.portal.init;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.discoverytools.gme.GMEViewer;
import gov.nih.nci.cagrid.introduce.portal.help.IntroduceHelp;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.cagrid.grape.ApplicationInitializer;


public class IntroducePortalInitializer implements ApplicationInitializer {
    private static final int HELP_MENU = 4;

    private static final int CONFIG_MENU = 3;

    private static final Logger logger = Logger.getLogger(IntroducePortalInitializer.class);
    
    public void intialize() throws Exception {
        PropertyConfigurator.configure("." + File.separator + "conf" + File.separator
            + "log4j.properties");

        logger.info("\n\nStarting up Introduce ... \n\n");
        ExtensionsLoader.getInstance();
        setConfigurationOptions();
    }


    private boolean hasKey(Enumeration keys, String key) {
        while (keys.hasMoreElements()) {
            String testKey = (String) keys.nextElement();
            if (testKey.equals(key)) {
                return true;
            }
        }
        return false;
    }


    private void setConfigurationOptions() {
        try {
            if (!hasKey(ResourceManager.getConfigurationPropertyKeys(),
                IntroduceConstants.NAMESPACE_TYPE_REPLACEMENT_POLICY_PROPERTY)) {
                ResourceManager.setConfigurationProperty(IntroduceConstants.NAMESPACE_TYPE_REPLACEMENT_POLICY_PROPERTY,
                    NamespaceTypeDiscoveryComponent.ERROR_POLICY);
            }
            ResourceManager.setConfigurationProperty(IntroduceConstants.NAMESPACE_TYPE_REPLACEMENT_POLICY_PROPERTY
                + ".options", NamespaceTypeDiscoveryComponent.ERROR_POLICY + "," + NamespaceTypeDiscoveryComponent.REPLACE_POLICY
                + "," + NamespaceTypeDiscoveryComponent.IGNORE_POLICY);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}
