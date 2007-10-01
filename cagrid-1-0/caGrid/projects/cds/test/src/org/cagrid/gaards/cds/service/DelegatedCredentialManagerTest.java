package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import junit.framework.TestCase;

import org.cagrid.gaards.cds.common.DelegationPolicy;
import org.cagrid.gaards.cds.common.IdentityDelegationPolicy;
import org.cagrid.gaards.cds.conf.CDSConfiguration;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.InvalidPolicyFault;
import org.cagrid.gaards.cds.testutils.Utils;
import org.cagrid.tools.database.Database;
import org.cagrid.tools.events.EventManager;

public class DelegatedCredentialManagerTest extends TestCase {

	private Database db;
	private EventManager em;
	private PropertyManager properties;

	public void testDelegatedCredentialCreateDestroy() {
		try {
			DelegatedCredentialManager dcm = new DelegatedCredentialManager(
					Utils.getConfiguration(), this.properties, em, db);
			dcm.clearDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}

	public void testChangeKeyManager() {
		DelegatedCredentialManager dcm = null;
		try {
			CDSConfiguration conf = Utils.getConfiguration();
			dcm = new DelegatedCredentialManager(conf, this.properties, em, db);

			try {

				conf.getKeyManagerDescription().setClassName("foobar");
				new DelegatedCredentialManager(conf, this.properties, em, db);
				fail("Should not be able to change Key Manager.");
			} catch (CDSInternalFault e) {
				if (e.getFaultString().indexOf(
						"The key manager cannot be changed") == -1) {
					fail("Should not be able to change Key Manager.");
				}
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			try {
				dcm.clearDatabase();
			} catch (Exception e) {
			}
		}
	}

	public void testDelegateCredentialInvalidPolicy() {
		DelegatedCredentialManager dcm = null;
		try {
			CDSConfiguration conf = Utils.getConfiguration();
			dcm = new DelegatedCredentialManager(conf, this.properties, em, db);

			try {
				dcm.delegateCredential("some user",
						new InvalidDelegationPolicy());
				fail("Should not be able to delegate a credential with an invalid delegation policy.");
			} catch (InvalidPolicyFault e) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			try {
				dcm.clearDatabase();
			} catch (Exception e) {
			}
		}
	}

	public void testDelegateCredentialInvalidKeyLength() {
		DelegatedCredentialManager dcm = null;
		try {
			CDSConfiguration conf = Utils.getConfiguration();
			dcm = new DelegatedCredentialManager(conf, this.properties, em, db);

			IdentityDelegationPolicy policy = new IdentityDelegationPolicy();
			policy.setKeyLength(1);

			try {
				dcm.delegateCredential("some user", policy);
				fail("Should not be able to delegate a credential with an invalid Key Length.");
			} catch (InvalidPolicyFault e) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			try {
				dcm.clearDatabase();
			} catch (Exception e) {
			}
		}
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
			em = new EventManager(null);
			this.properties = new PropertyManager(db);
			this.properties.clearAllProperties();
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

	public class InvalidDelegationPolicy extends DelegationPolicy {

	}
}
