package gov.nih.nci.cagrid.introduce.test.unit;

import gov.nih.nci.cagrid.introduce.upgrade.UpgradeManager;
import gov.nih.nci.cagrid.introduce.upgrade.common.UpgradeUtilities;

import java.io.File;

import junit.framework.TestCase;


public class UpgadeTestCase extends TestCase {

    public void testGetVersion() {

        try {
            String version = UpgradeUtilities.getCurrentServiceVersion(this.getClass().getResource(File.separator +"gold" + File.separator + "introduceServicesExample.xml").getFile());
            assertTrue(version.equals("1.0b"));
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
    
    public void testGetName() {

        try {
            String version = UpgradeUtilities.getServiceName(this.getClass().getResource(File.separator +"gold" + File.separator + "introduceServicesExample.xml").getFile());
            assertTrue(version.equals("HelloWorld"));
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    public void testNotUpgradable01b() {

        UpgradeManager um = new UpgradeManager(this.getClass().getResource(File.separator +"gold"
            + File.separator + "01b").getFile());
        assertFalse(um.canIntroduceBeUpgraded());
    }

    public void testUpgradable10() {

        UpgradeManager um = new UpgradeManager(this.getClass().getResource(File.separator +"gold"
            + File.separator + "10").getFile());
        assertTrue(um.canIntroduceBeUpgraded());
    }
    
    public void testUpgradable11() {

        UpgradeManager um = new UpgradeManager(this.getClass().getResource(File.separator +"gold"
            + File.separator + "11").getFile());
        assertTrue(um.canIntroduceBeUpgraded());
    }
    

}
