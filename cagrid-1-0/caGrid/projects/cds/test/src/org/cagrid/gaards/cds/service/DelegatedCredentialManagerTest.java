package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.cagrid.gaards.cds.common.AllowedParties;
import org.cagrid.gaards.cds.common.DelegationIdentifier;
import org.cagrid.gaards.cds.common.DelegationPolicy;
import org.cagrid.gaards.cds.common.DelegationRecord;
import org.cagrid.gaards.cds.common.DelegationSigningRequest;
import org.cagrid.gaards.cds.common.DelegationStatus;
import org.cagrid.gaards.cds.common.IdentityDelegationPolicy;
import org.cagrid.gaards.cds.service.policy.PolicyHandler;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.gaards.cds.stubs.types.InvalidPolicyFault;
import org.cagrid.gaards.cds.testutils.Constants;
import org.cagrid.gaards.cds.testutils.Utils;

public class DelegatedCredentialManagerTest extends TestCase {

	private static String GRID_IDENTITY = "/C=US/O=abc/OU=xyz/OU=caGrid/CN=user";

	public void testDelegatedCredentialCreateDestroy() {
		try {
			DelegatedCredentialManager dcm = Utils
					.getDelegatedCredentialManager();
			dcm.clearDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}

	public void testChangeKeyManager() {
		DelegatedCredentialManager dcm = null;
		try {
			dcm = Utils.getDelegatedCredentialManager();

			try {
				new DelegatedCredentialManager(Utils.getDatabase(), Utils
						.getPropertyManager(), new InvalidKeyManager(),
						new ArrayList<PolicyHandler>(), new ProxyPolicy());
				fail("Should not be able to change Key Manager.");
			} catch (CDSInternalFault e) {
				if (e.getFaultString().indexOf(Errors.KEY_MANAGER_CHANGED) == -1) {
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
			dcm = Utils.getDelegatedCredentialManager();
			try {
				dcm.initiateDelegation("some user",
						new InvalidDelegationPolicy(), Constants.KEY_LENGTH);
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
			dcm = Utils.getDelegatedCredentialManager();

			IdentityDelegationPolicy policy = new IdentityDelegationPolicy();
			try {
				dcm.initiateDelegation("some user", policy, 1);
				fail("Should not be able to delegate a credential with an invalid Key Length.");
			} catch (DelegationFault e) {

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

	public void testDelegateCredentialsWithIdentityDelegationPolicy() {
		DelegatedCredentialManager dcm = null;
		try {
			dcm = Utils.getDelegatedCredentialManager();
			List<DelegationIdentifier> list = new ArrayList<DelegationIdentifier>();
			int size = 3;

			String gridIdentity = GRID_IDENTITY + 0;
			for (int i = 0; i < size; i++) {
				IdentityDelegationPolicy policy = new IdentityDelegationPolicy();
				String[] users = new String[i + 1];
				for (int j = 0; j <= i; j++) {
					users[j] = GRID_IDENTITY + (j + 1);
				}
				AllowedParties ap = new AllowedParties();
				ap.setGridIdentity(users);
				policy.setAllowedParties(ap);
				list.add(delegateAndValidate(dcm, gridIdentity, policy));
			}
			for (int i = 0; i < list.size(); i++) {
				DelegationIdentifier id = list.get(i);
				assertTrue(dcm.delegationExists(id));
				dcm.delete(id);
				assertFalse(dcm.delegationExists(id));
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

	protected DelegationIdentifier delegateAndValidate(
			DelegatedCredentialManager dcm, String gridIdentity,
			DelegationPolicy policy) throws Exception {
		DelegationSigningRequest req = dcm.initiateDelegation(gridIdentity,
				policy, Constants.KEY_LENGTH);
		DelegationIdentifier id = req.getDelegationIdentifier();
		assertNotNull(id);
		assertTrue(dcm.delegationExists(id));
		DelegationRecord r = dcm.getDelegationRecord(id);
		assertEquals(id, r.getDelegationIdentifier());
		assertEquals(gridIdentity, r.getGridIdentity());
		assertEquals(0, r.getDateApproved());
		assertEquals(0, r.getExpiration());
		assertEquals(DelegationStatus.Pending, r.getDelegationStatus());
		assertTrue((0 < r.getDateInitiated()));
		assertEquals(policy, r.getDelegationPolicy());
		assertNotNull(r.getCertificateChain());
		assertNull(r.getCertificateChain().getX509Certificate());
		return id;
	}

	protected void setUp() throws Exception {
		super.setUp();
		Utils.getDatabase().createDatabaseIfNeeded();
	}

	protected void tearDown() throws Exception {
		super.setUp();

	}

	public class InvalidKeyManager implements KeyManager {

		public KeyPair createAndStoreKeyPair(String alias, int keyLength)
				throws CDSInternalFault {
			return null;
		}

		public void delete(String alias) throws CDSInternalFault {

		}

		public void deleteAll() throws CDSInternalFault {

		}

		public boolean exists(String alias) throws CDSInternalFault {
			return false;
		}

		public X509Certificate[] getCertificates(String alias)
				throws CDSInternalFault {
			return null;
		}

		public PrivateKey getPrivateKey(String alias) throws CDSInternalFault {
			return null;
		}

		public PublicKey getPublicKey(String alias) throws CDSInternalFault {
			return null;
		}

		public void storeCertificates(String alias, X509Certificate[] cert)
				throws CDSInternalFault, DelegationFault {

		}

	}

}
