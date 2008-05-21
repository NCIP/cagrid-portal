package gov.nih.nci.cagrid.introduce.common;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public final class IntroduceEnginePropertiesManager {
    
    private static final Logger logger = Logger.getLogger(IntroduceEnginePropertiesManager.class);
    
    private IntroduceEnginePropertiesManager(){
        
    }


    public static String getIntroduceVersion() {
        return getIntroducePropertyValue(IntroduceConstants.INTRODUCE_VERSION_PROPERTY);
    }


    public static String getIntroducePatchVersion() {
        return getIntroducePropertyValue(IntroduceConstants.INTRODUCE_PATCH_VERSION_PROPERTY);
    }


    public static String getIntroduceUpdateSite() {
        return getIntroducePropertyValue(IntroduceConstants.INTRODUCE_UPDATE_SITE_PROPERTY);
    }


    public static String getIntroduceDefaultIndexService() {
        return getIntroducePropertyValue(IntroduceConstants.INTRODUCE_DEFAULT_INDEX_SERVICE_PROPERTY);
    }


    public static String getStatisticSite() {
        return getIntroducePropertyValue(IntroduceConstants.INTRODUCE_STATS_SITE);
    }


    public static int getStatisticPort() {
        int port = -1;
        String prop = getIntroducePropertyValue(IntroduceConstants.INTRODUCE_STATS_PORT);
        try {
            port = Integer.parseInt(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return port;
    }


    public static boolean getCollectStats() {
        return Boolean.parseBoolean(getIntroducePropertyValue(IntroduceConstants.INTRODUCE_STATS_COLLECT));
    }


    public static String getIntroducePropertyValue(String propertyKey) {
        Properties engineProps = new Properties();
        try {
            engineProps.load(new FileInputStream(IntroduceConstants.INTRODUCE_ENGINE_PROPERTIES));
            return engineProps.getProperty(propertyKey);
        } catch (IOException e) {
            logger.error(e);
            return null;
        }
    }

    
}
