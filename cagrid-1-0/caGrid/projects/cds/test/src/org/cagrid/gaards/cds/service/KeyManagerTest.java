package org.cagrid.gaards.cds.service;

import java.lang.reflect.Constructor;

import gov.nih.nci.cagrid.common.FaultUtil;
import junit.framework.TestCase;

import org.cagrid.gaards.cds.conf.CDSConfiguration;
import org.cagrid.gaards.cds.conf.KeyManagerDescription;
import org.cagrid.gaards.cds.testutils.Utils;
import org.cagrid.tools.database.Database;
import org.cagrid.tools.events.EventManager;

public class KeyManagerTest extends TestCase {

	private Database db;
	private EventManager em;

	public void testKeyManagerCreateDestroy() {
		try {
			KeyManager km = getKeyManager();
			km.deleteAll();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private KeyManager getKeyManager() throws Exception {
		CDSConfiguration conf = Utils.getConfiguration();
		KeyManagerDescription des = conf.getKeyManagerDescription();
		if (des == null) {
			fail("Not KeyManager description found.");
		}
		Class kmc = Class.forName(des.getClassName());
		Constructor c = kmc.getConstructor(new Class[] {
				KeyManagerDescription.class, Database.class });
		KeyManager km = (KeyManager) c.newInstance(new Object[] { des,
				Utils.getDB() });
		return km;
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
