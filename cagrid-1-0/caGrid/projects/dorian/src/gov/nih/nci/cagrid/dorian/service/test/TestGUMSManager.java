package gov.nih.nci.cagrid.gums.service.test;

import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.service.GUMSManager;
import gov.nih.nci.cagrid.gums.service.RequiredAttributesManager;

import java.io.File;

import junit.framework.TestCase;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: TestGUMSManager.java,v 1.4 2005-09-29 18:21:25 langella Exp $
 */
public class TestGUMSManager extends TestCase{
	public static String RESOURCES_DIR = "resources" + File.separator
	+ "general-test";
	
    public void testJanusManager(){
    	try{
    		GUMSManager.GUMS_CONFIGURATION_FILE = RESOURCES_DIR+File.separator+"gums-conf.xml";
    		GUMSManager jm = GUMSManager.getInstance();
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		RequiredAttributesManager atts = jm.getUserAttributeManager();
    		assertNotNull(atts);
    		atts.getRequiredAttributes();
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }

}
