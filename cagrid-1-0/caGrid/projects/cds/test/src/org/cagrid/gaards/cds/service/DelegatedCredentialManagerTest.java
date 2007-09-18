package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import junit.framework.TestCase;

import org.cagrid.gaards.cds.testutils.Utils;
import org.cagrid.tools.database.Database;
import org.cagrid.tools.events.EventManager;


public class DelegatedCredentialManagerTest extends TestCase {

	private Database db;
	private EventManager em;


	public void testDelegatedCredentialCreateDestroy() {
		try {
			DelegatedCredentialManager dcm = new DelegatedCredentialManager(Utils.getConfiguration(), em, db);
			dcm.clearDatabase();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
			em = new EventManager(null);
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
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
}
