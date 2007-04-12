package gov.nih.nci.cagrid.introduce.test.unit;

import gov.nih.nci.cagrid.introduce.upgrade.UpgradeManager;
import gov.nih.nci.cagrid.introduce.upgrade.common.UpgradeUtilities;

import java.io.File;

import junit.framework.TestCase;


public class UpgadeTestCase extends TestCase {

    public void testGetVersion() {

        try {
            String version = UpgradeUtilities.getCurrentServiceVersion("test" + File.separator + "resources" + File.separator + "gold" + File.separator + "introduceServicesExample.xml");
            assertTrue(version.equals("1.0b"));
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
    
    public void testGetName() {

        try {
            String version = UpgradeUtilities.getServiceName("test" + File.separator + "resources" + File.separator + "gold" + File.separator + "introduceServicesExample.xml");
            assertTrue(version.equals("HelloWorld"));
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    public void testNotUpgradable() {

        UpgradeManager um = new UpgradeManager("test" + File.separator + "resources" + File.separator + "gold"
            + File.separator + "01b");
        assertTrue(um.canIntroduceBeUpgraded());
        try {
            um.upgrade();
        } catch (Exception e) {
            return;
        }
        fail();
    }
    
    

}
