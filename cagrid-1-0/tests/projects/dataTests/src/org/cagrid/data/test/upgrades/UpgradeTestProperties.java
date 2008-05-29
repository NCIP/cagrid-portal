package org.cagrid.data.test.upgrades;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 *  UpgradeTestProperties
 *  Controls system properties for the upgrade tests
 * 
 * @author David Ervin
 * 
 * @created May 29, 2008 12:23:04 PM
 * @version $Id: UpgradeTestProperties.java,v 1.1 2008-05-29 16:45:59 dervin Exp $ 
 */
public class UpgradeTestProperties {
    
    private static final Log logger = LogFactory.getLog(UpgradeTestProperties.class);

    // system properties
    public static final String UPGRADE_SERVICES_ZIP_DIR = "upgrade.services.zip.dir";
    public static final String UPGRADE_SERVICES_EXTRACT_DIR = "upgrade.services.extract.dir";
    
    // defaults
    public static final String DEFAULT_UPGRADE_SERVICES_ZIP_DIR = "resources/services";
    public static final String DEFAULT_UPGRADE_SERVICES_EXTRACT_DIR = "test/services";
    
    private UpgradeTestProperties() {
        // prevents instantiation
    }
    
    
    public static String getUpgradeServicesZipDir() {
        return getValue(UPGRADE_SERVICES_ZIP_DIR, DEFAULT_UPGRADE_SERVICES_ZIP_DIR);
    }
    
    
    public static String getUpgradeServicesExtractDir() {
        return getValue(UPGRADE_SERVICES_EXTRACT_DIR, DEFAULT_UPGRADE_SERVICES_EXTRACT_DIR);
    }
    
    
    private static String getValue(String property, String defaultValue) {
        String value = System.getenv(property);
        if (value == null) {
            logger.debug("System property " + property + " not set, using default");
            value = defaultValue;
        }
        return value;
    }
}
