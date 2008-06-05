package gov.nih.nci.cagrid.introduce.portal.modification.security;

import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.IntroducePropertiesManager;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;


public class GridGrouperURLManager {
    
    private static final Logger logger = Logger.getLogger(GridGrouperURLManager.class);
    
    public final static String GRID_GROUPER_URL_PROPERTY = "Grid Grouper URLs";
    public final static String GRID_GROUPER_LOAD_PROPERTY = "Grid Grouper Load On Startup";

    public final static String DEFAULT_GRID_GROUPER_URLS_PROP = "introduce.gridgrouper.urls";
    public final static String DEFAULT_LOAD_ON_STARTUP_PROP = "introduce.gridgrouper.load.on.startup";


    public static boolean getLoadOnStartup() {
        boolean loadOnStartup = false;
        try {
            String load = ResourceManager.getConfigurationProperty(GRID_GROUPER_LOAD_PROPERTY);
            if (load == null) {
                String prop = IntroducePropertiesManager.getIntroducePropertyValue(DEFAULT_LOAD_ON_STARTUP_PROP);
                boolean loadPropVal = Boolean.parseBoolean(prop);

                ResourceManager.setConfigurationProperty(GRID_GROUPER_LOAD_PROPERTY, String.valueOf(loadPropVal));
                load = ResourceManager.getConfigurationProperty(GRID_GROUPER_LOAD_PROPERTY);
            }
            ResourceManager.setConfigurationProperty(GRID_GROUPER_LOAD_PROPERTY + ".boolean", "");
            loadOnStartup = Boolean.parseBoolean(load);
        } catch (Exception e) {
            logger.error(e);
        }
        return loadOnStartup;
    }


    public static List getGridGroupers() {
        ArrayList groupers = new ArrayList();
        try {
            String urls = ResourceManager.getServiceURLProperty(GRID_GROUPER_URL_PROPERTY);
            if (urls == null) {
                String prop = IntroducePropertiesManager.getIntroducePropertyValue(DEFAULT_GRID_GROUPER_URLS_PROP);
                ResourceManager.setServiceURLProperty(GRID_GROUPER_URL_PROPERTY, prop);
                urls = ResourceManager.getServiceURLProperty(GRID_GROUPER_URL_PROPERTY);
            }
            if (urls != null) {
                StringTokenizer st = new StringTokenizer(urls, ";");
                while (st.hasMoreElements()) {
                    groupers.add(st.nextToken());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groupers;
    }
}
