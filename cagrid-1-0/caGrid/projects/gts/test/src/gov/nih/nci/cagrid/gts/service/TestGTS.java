package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gts.test.Utils;
import junit.framework.TestCase;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestGTS extends TestCase {

	public void testCreateAndDestroy() {
		try {
			GTS gts = new GTS(Utils.getGTSConfiguration(), "localhost");
			// Make sure we start fresh
			gts.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
	
	public void testAddTrustedAuthority() {
		try {
			GTS gts = new GTS(Utils.getGTSConfiguration(), "localhost");
			// Make sure we start fresh
			gts.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
	
	

}
