package gov.nih.nci.cagrid.gums.service.test;

import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.service.GUMSManager;
import gov.nih.nci.cagrid.gums.service.RequiredAttributesManager;
import gov.nih.nci.cagrid.gums.test.TestUtils;

import java.io.File;

import junit.framework.TestCase;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestGUMSManager extends TestCase{
	public static String RESOURCES_DIR = "resources" + File.separator
	+ "general-test";
	
    public void testGUMSManager(){
    	try{
    		TestUtils.getCA();
    		GUMSManager.GUMS_CONFIGURATION_FILE = RESOURCES_DIR+File.separator+"gums-conf.xml";
    		GUMSManager jm = GUMSManager.getInstance();
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		RequiredAttributesManager atts = jm.getUserAttributeManager();
    		assertNotNull(atts);
    		atts.getRequiredAttributes();
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    


}
