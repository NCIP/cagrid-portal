package gov.nih.nci.cagrid.introduce.common;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.configuration.IntroducePortalConfiguration;
import gov.nih.nci.cagrid.introduce.beans.configuration.IntroduceServiceDefaults;

import org.cagrid.grape.ConfigurationManager;
import org.cagrid.grape.model.Application;


public class ConfigurationUtil {

    private static ConfigurationUtil util = null;
    private ConfigurationManager configurationManager = null;


    private ConfigurationUtil() throws Exception {
        Application app;
        app = (Application) Utils.deserializeDocument(IntroducePropertiesManager.getIntroduceConfigurationFile(),
            Application.class);
        configurationManager = new ConfigurationManager(app.getConfiguration());
    }


    private static void load() throws Exception {
        if (util == null) {
            util = new ConfigurationUtil();
        }
    }


    public static ConfigurationUtil getInstance() throws Exception {
        load();
        return util;
    }
    
    public static IntroducePortalConfiguration getIntroducePortalConfiguration() {
        try {
            return (IntroducePortalConfiguration)getInstance().configurationManager.getConfigurationObject("introducePortal");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public static IntroduceServiceDefaults getIntroduceServiceDefaults() {
        try {
            return (IntroduceServiceDefaults)getInstance().configurationManager.getConfigurationObject("introduceServiceDefaults");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
