package gov.nih.nci.cagrid.introduce.portal.modification.security;

import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.ConfigurationUtil;
import gov.nih.nci.cagrid.introduce.common.IntroducePropertiesManager;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;


public class GridGrouperURLManager {
    
    private static final Logger logger = Logger.getLogger(GridGrouperURLManager.class);

    public final static String DEFAULT_GRID_GROUPER_URLS_PROP = "introduce.gridgrouper.urls";
    public final static String DEFAULT_LOAD_ON_STARTUP_PROP = "introduce.gridgrouper.load.on.startup";


    public static boolean getLoadOnStartup() {
        boolean loadOnStartup = false;
        try {
            String load = IntroducePropertiesManager.getIntroducePropertyValue(DEFAULT_LOAD_ON_STARTUP_PROP);
            loadOnStartup = Boolean.parseBoolean(load);
        } catch (Exception e) {
            logger.error(e);
        }
        return loadOnStartup;
    }


    public static List getGridGroupers() {
        ArrayList groupers = new ArrayList();
        try {
            String urls = IntroducePropertiesManager.getIntroducePropertyValue(DEFAULT_GRID_GROUPER_URLS_PROP);
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
