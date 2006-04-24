package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gts.bean.AuthorityGTS;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityTimeToLive;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.IllegalAuthorityFault;
import gov.nih.nci.cagrid.gts.test.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;

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

			// Add Authority no ttl
			AuthorityGTS a2 = new AuthorityGTS();
			a2.setServiceURI("Service");
			a2.setPriority(1);
			a2.setPerformAuthorization(true);
			a2.setServiceIdentity("Service");
			a2.setSyncTrustLevels(true);
			try {
				am.addAuthority(a2);
				fail("Should not be able to add authority!!!");
			} catch (IllegalAuthorityFault f) {

			}

			// Add Authority no service identity
			AuthorityGTS a3 = new AuthorityGTS();
			a3.setServiceURI("Service");
			a3.setPriority(1);
			a3.setPerformAuthorization(true);
			a3.setSyncTrustLevels(true);
			a3.setTrustedAuthorityTimeToLive(ttl);
			try {
				am.addAuthority(a3);
				fail("Should not be able to add authority!!!");
			} catch (IllegalAuthorityFault f) {

			}

			// Invalid Priority
			AuthorityGTS a4 = new AuthorityGTS();
			a4.setServiceURI("Service");
			a4.setPriority(0);
			a4.setPerformAuthorization(true);
			a4.setSyncTrustLevels(true);
			a4.setServiceIdentity("Service");
			a4.setTrustedAuthorityTimeToLive(ttl);
			try {
				am.addAuthority(a4);
				fail("Should not be able to add authority!!!");
			} catch (IllegalAuthorityFault f) {

			}

			AuthorityGTS a6 = getAuthority("GTS 6", 1);
			am.addAuthority(a6);
			assertTrue(am.doesAuthorityExist(a6.getServiceURI()));
			assertEquals(1, am.getAuthorityCount());
			assertEquals(a6, am.getAuthority(a6.getServiceURI()));

			try {
				am.addAuthority(a6);
				fail("Should not be able to add authority!!!");
			} catch (IllegalAuthorityFault f) {

			}

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


	public void testAddAuthority() {
		GTSAuthorityManager am = new GTSAuthorityManager(db);
		try {
			AuthorityGTS a1 = getAuthority("GTS 1", 1);
			assertFalse(am.doesAuthorityExist(a1.getServiceURI()));
			assertEquals(0, am.getAuthorityCount());
			am.addAuthority(a1);
			assertTrue(am.doesAuthorityExist(a1.getServiceURI()));
			assertEquals(1, am.getAuthorityCount());
			assertEquals(a1, am.getAuthority(a1.getServiceURI()));
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


	public void testUpdateRollback() {
		GTSAuthorityManager am = new GTSAuthorityManager(db);
		int count = 5;
		AuthorityGTS[] a = new AuthorityGTS[count];
		Connection c = null;
		try {
			for (int i = 0; i < count; i++) {
				a[i] = getAuthority("GTS " + i, 1);
				assertFalse(am.doesAuthorityExist(a[i].getServiceURI()));
				assertEquals(i, am.getAuthorityCount());
				am.addAuthority(a[i]);
				assertTrue(am.doesAuthorityExist(a[i].getServiceURI()));
				assertEquals((i + 1), am.getAuthorityCount());
				assertEquals(a[i], am.getAuthority(a[i].getServiceURI()));

				for (int j = 0; j < i; j++) {
					a[j].setPriority(a[j].getPriority() + 1);
					assertEquals(a[j], am.getAuthority(a[j].getServiceURI()));
				}
			}
			c = db.getConnection();
			c.setAutoCommit(false);
			for (int i = 0; i < count; i++) {
				a[i].setPriority(a[i].getPriority() + 1);
				am.updateAuthority(c, a[i]);
			}
			c.commit();

			for (int i = 0; i < count; i++) {
				assertEquals(a[i], am.getAuthority(a[i].getServiceURI()));
			}

			for (int i = 0; i < count; i++) {
				a[i].setPriority(a[i].getPriority() + 1);
				am.updateAuthority(c, a[i]);
			}

			try {
				PreparedStatement bad = c.prepareStatement("INSERT INTO NOTHING SET VALUES(1,2)");
				bad.executeUpdate();
				try {
					c.commit();
				} catch (Exception com) {
					c.rollback();
				}
			} catch (Exception ex) {
				c.rollback();
			}
			for (int i = 0; i < count; i++) {
				a[i].setPriority(a[i].getPriority() - 1);
				assertEquals(a[i], am.getAuthority(a[i].getServiceURI()));

			}
			c.setAutoCommit(true);
			db.releaseConnection(c);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			if (c != null) {
				try {
					c.setAutoCommit(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				db.releaseConnection(c);
			}
			try {
				am.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testAddAuthorityOverwritePriority() {
		GTSAuthorityManager am = new GTSAuthorityManager(db);
		try {
			AuthorityGTS a1 = getAuthority("GTS 1", 1);
			assertFalse(am.doesAuthorityExist(a1.getServiceURI()));
			assertEquals(0, am.getAuthorityCount());
			am.addAuthority(a1);
			assertTrue(am.doesAuthorityExist(a1.getServiceURI()));
			assertEquals(1, am.getAuthorityCount());
			assertEquals(a1, am.getAuthority(a1.getServiceURI()));

			AuthorityGTS a2 = getAuthority("GTS 2", 1);
			am.addAuthority(a2);
			assertTrue(am.doesAuthorityExist(a2.getServiceURI()));
			assertEquals(2, am.getAuthorityCount());
			assertEquals(a2, am.getAuthority(a2.getServiceURI()));

			a1.setPriority(2);
			assertEquals(a1, am.getAuthority(a1.getServiceURI()));

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


	private AuthorityGTS getAuthority(String uri, int priority) {
		TrustedAuthorityTimeToLive ttl = new TrustedAuthorityTimeToLive();
		ttl.setHours(1);
		ttl.setMinutes(1);
		ttl.setSeconds(1);
		AuthorityGTS a1 = new AuthorityGTS();
		a1.setServiceURI(uri);
		a1.setPriority(1);
		a1.setPerformAuthorization(true);
		a1.setServiceIdentity(uri);
		a1.setSyncTrustLevels(true);
		a1.setTrustedAuthorityTimeToLive(ttl);
		return a1;
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
