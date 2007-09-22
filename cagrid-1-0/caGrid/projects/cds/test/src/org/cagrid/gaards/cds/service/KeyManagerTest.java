package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultUtil;

import java.lang.reflect.Constructor;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

import junit.framework.TestCase;

import org.cagrid.gaards.cds.conf.CDSConfiguration;
import org.cagrid.gaards.cds.conf.KeyManagerDescription;
import org.cagrid.gaards.cds.testutils.CA;
import org.cagrid.gaards.cds.testutils.Utils;
import org.cagrid.tools.database.Database;

public class KeyManagerTest extends TestCase {

	private Database db;
	private CA ca;

	public void testKeyManagerCreateDestroy() {
		try {
			KeyManager km = getKeyManager();
			km.deleteAll();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testKeyManager() {
		KeyManager km = null;
		try {
			km = getKeyManager();
			int size = 3;
			for (int i = 0; i < size; i++) {
				String alias = String.valueOf(i);
				assertFalse(km.exists(alias));
				KeyPair pair = km.createAndStoreKeyPair(alias, 1024);
				assertTrue(km.exists(alias));
				assertEquals(pair.getPublic(), km.getPublicKey(alias));
				assertEquals(pair.getPrivate(), km.getPrivateKey(alias));
				X509Certificate cert = ca.createIdentityCertificate(km.getPublicKey(alias), alias);
				km.storeCertificate(alias, cert);
				assertEquals(cert, km.getCertificate(alias));
			}

			for (int i = 0; i < size; i++) {
				String alias = String.valueOf(i);
				assertTrue(km.exists(alias));
				km.delete(alias);
				assertFalse(km.exists(alias));
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			try {
				km.deleteAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			ca = new CA();
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
