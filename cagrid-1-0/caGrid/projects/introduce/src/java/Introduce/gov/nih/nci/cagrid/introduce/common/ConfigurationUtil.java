package gov.nih.nci.cagrid.introduce.common;

import java.util.Arrays;
import java.util.Collections;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.configuration.IntroducePortalConfiguration;
import gov.nih.nci.cagrid.introduce.beans.configuration.IntroduceServiceDefaults;
import gov.nih.nci.cagrid.introduce.beans.extension.Properties;
import gov.nih.nci.cagrid.introduce.beans.extension.PropertiesProperty;

import org.cagrid.grape.ConfigurationManager;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.model.Application;

import com.sun.tools.javac.code.Attribute.Array;


/**
 * Class for accessing the configuration files.
 * 
 * @author hastings
 */
public class ConfigurationUtil {

    private static ConfigurationUtil util = null;
    private ConfigurationManager configurationManager = null;


    private ConfigurationUtil() throws Exception {

        if (GridApplication.getContext() != null) {
            configurationManager = GridApplication.getContext().getConfigurationManager();
        } else {
            Application app = null;
            app = (Application) Utils.deserializeDocument(IntroducePropertiesManager.getIntroduceConfigurationFile(),
                Application.class);
            configurationManager = new ConfigurationManager(app.getConfiguration());
        }

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
            return (IntroducePortalConfiguration) getInstance().configurationManager
                .getConfigurationObject("introducePortal");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static IntroduceServiceDefaults getIntroduceServiceDefaults() {
        try {
            return (IntroduceServiceDefaults) getInstance().configurationManager
                .getConfigurationObject("introduceServiceDefaults");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Properties getGlobalExtensionProperties() {
        try {
            return (Properties) getInstance().configurationManager
                .getConfigurationObject("introduceGlobalExtensionProperties");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void addGlobalExtensionProperty(PropertiesProperty prop) throws Exception {
        getInstance();
        if (getGlobalExtensionProperties() != null && getGlobalExtensionProperties().getProperty() != null) {
            for(int i = 0; i < getGlobalExtensionProperties().getProperty().length; i++){
                PropertiesProperty checkProp = getGlobalExtensionProperties().getProperty(i);
                if(checkProp.getKey().equals(prop.getKey())){
                    return;
                }
            }
            PropertiesProperty[] props = new PropertiesProperty[getGlobalExtensionProperties().getProperty().length + 1];
            System.arraycopy(getGlobalExtensionProperties().getProperty(), 0, props, 0, getGlobalExtensionProperties()
                .getProperty().length);
            props[getGlobalExtensionProperties().getProperty().length] = prop;
            getGlobalExtensionProperties().setProperty(props);
        } else if (getGlobalExtensionProperties() != null) {
            PropertiesProperty[] props = new PropertiesProperty[1];
            props[0] = prop;
            getGlobalExtensionProperties().setProperty(props);
        }
        getInstance().configurationManager.saveAll();

    }


    public static PropertiesProperty getGlobalExtensionProperty(String key) throws Exception {
        getInstance();
        if (getGlobalExtensionProperties() != null && getGlobalExtensionProperties().getProperty() != null) {
            for (int i = 0; i < getGlobalExtensionProperties().getProperty().length; i++) {
                if (getGlobalExtensionProperties().getProperty()[i].getKey().equals(key)) {
                    return getGlobalExtensionProperties().getProperty(i);
                }
            }
        }
        return null;
    }

}
