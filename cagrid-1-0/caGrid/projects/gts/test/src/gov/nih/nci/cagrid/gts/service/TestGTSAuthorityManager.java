package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gts.bean.AuthorityGTS;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityTimeToLive;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.IllegalAuthorityFault;
import gov.nih.nci.cagrid.gts.test.Utils;
import junit.framework.TestCase;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestGTSAuthorityManager extends TestCase {

	private Database db;


	public TestGTSAuthorityManager() {

	}


	public void testCreateAndDestroy() {
		GTSAuthorityManager am = new GTSAuthorityManager(db);
		try {
			am.buildDatabase();
			am.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				am.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testAddInvalidAuthority() {
		GTSAuthorityManager am = new GTSAuthorityManager(db);
		try {
			TrustedAuthorityTimeToLive ttl = new TrustedAuthorityTimeToLive();
			ttl.setHours(1);
			ttl.setMinutes(1);
			ttl.setSeconds(1);

			// Add Authority no serviceURI
			AuthorityGTS a1 = new AuthorityGTS();
			a1.setPriority(1);
			a1.setPerformAuthorization(true);
			a1.setServiceIdentity("Service");
			a1.setSyncTrustLevels(true);
			a1.setTrustedAuthorityTimeToLive(ttl);
			try {
				am.addAuthority(a1);
				fail("Should not be able to add authority!!!");
			} catch (IllegalAuthorityFault f) {

			}
			
			//Add Authority no ttl
			AuthorityGTS a2 = new AuthorityGTS();
			a2.setServiceURI("Service");
			a2.setPriority(1);
			a2.setPerformAuthorization(true);
			a2.setServiceIdentity("Service");
			a2.setSyncTrustLevels(true);
			try {
				am.addAuthority(a1);
				fail("Should not be able to add authority!!!");
			} catch (IllegalAuthorityFault f) {

			}
			
			//Add Authority no ttl
			AuthorityGTS a3 = new AuthorityGTS();
			a3.setServiceURI("Service");
			a3.setPriority(1);
			a3.setPerformAuthorization(true);
			a3.setSyncTrustLevels(true);
			a3.setTrustedAuthorityTimeToLive(ttl);
			try {
				am.addAuthority(a1);
				fail("Should not be able to add authority!!!");
			} catch (IllegalAuthorityFault f) {

			}
			
			//TODO: TEST ALREADY EXISTING AUTHORITIES
			
			
			
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				am.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0, db.getUsedConnectionCount());
			db.destroyDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
