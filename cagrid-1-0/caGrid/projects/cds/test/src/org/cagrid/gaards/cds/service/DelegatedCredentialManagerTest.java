package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.File;
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
import org.cagrid.gaards.cds.common.DelegationSigningResponse;
import org.cagrid.gaards.cds.common.DelegationStatus;
import org.cagrid.gaards.cds.common.IdentityDelegationPolicy;
import org.cagrid.gaards.cds.service.policy.PolicyHandler;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.gaards.cds.stubs.types.InvalidPolicyFault;
import org.cagrid.gaards.cds.testutils.CA;
import org.cagrid.gaards.cds.testutils.Constants;
import org.cagrid.gaards.cds.testutils.Utils;
import org.globus.gsi.GlobusCredential;

public class DelegatedCredentialManagerTest extends TestCase {

	private static String GRID_IDENTITY = "/C=US/O=abc/OU=xyz/OU=caGrid/CN=user";

	private CA ca;
	
	private File caCert;

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

			IdentityDelegationPolicy policy = getSimplePolicy();
			try {
				dcm.initiateDelegation("some user", policy, 1);
				fail("Should not be able to delegate a credential with an invalid Key Length.");
			} catch (DelegationFault e) {
				if (e.getFaultString().indexOf(
						Errors.INVALID_KEY_LENGTH_SPECIFIED) == -1) {
					fail("Should not be able to delegate a credential with an invalid Key Length.");
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

	public void testDelegateCredentialInitiatorDoesNotMatchApprover() {
		DelegatedCredentialManager dcm = null;
		try {
			dcm = Utils.getDelegatedCredentialManager();

			IdentityDelegationPolicy policy = getSimplePolicy();
			String alias = "some user";
			DelegationSigningRequest req = dcm.initiateDelegation(alias,
					policy, Constants.KEY_LENGTH);
			try {
				DelegationSigningResponse res = new DelegationSigningResponse();
				res.setDelegationIdentifier(req.getDelegationIdentifier());
				res.setCertificateChain(org.cagrid.gaards.cds.common.Utils
						.toCertificateChain(ca.createProxy(alias, 0)
								.getCertificateChain()));
				dcm.approveDelegation("some other user", res);
				fail("Should not be able to approve delegation.");
			} catch (DelegationFault e) {
				if (e.getFaultString().indexOf(
						Errors.INITIATOR_DOES_NOT_MATCH_APPROVER) == -1) {
					fail("Should not be able to approve delegation.");
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

	public void testDelegateCredentialNoCertificateChain() {
		DelegatedCredentialManager dcm = null;
		try {
			dcm = Utils.getDelegatedCredentialManager();
			IdentityDelegationPolicy policy = getSimplePolicy();
			String alias = "some user";
			GlobusCredential cred = ca.createCredential(alias);
			DelegationSigningRequest req = dcm.initiateDelegation(cred.getIdentity(),
					policy, Constants.KEY_LENGTH);
			try {
				DelegationSigningResponse res = new DelegationSigningResponse();
				res.setDelegationIdentifier(req.getDelegationIdentifier());
				res.setCertificateChain(null);
				dcm.approveDelegation(cred.getIdentity(), res);
				fail("Should not be able to approve delegation.");
			} catch (DelegationFault e) {
				if (e.getFaultString().indexOf(
						Errors.CERTIFICATE_CHAIN_NOT_SPECIFIED) == -1) {
					fail("Should not be able to approve delegation.");
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

	public void testDelegateCredentialInsufficientCertificateChain() {
		DelegatedCredentialManager dcm = null;
		try {
			dcm = Utils.getDelegatedCredentialManager();
			IdentityDelegationPolicy policy = getSimplePolicy();
			String alias = "some user";
			GlobusCredential cred = ca.createCredential(alias);
			DelegationSigningRequest req = dcm.initiateDelegation(cred.getIdentity(),
					policy, Constants.KEY_LENGTH);
			try {
				
				DelegationSigningResponse res = new DelegationSigningResponse();
				res.setDelegationIdentifier(req.getDelegationIdentifier());
				res.setCertificateChain(org.cagrid.gaards.cds.common.Utils
						.toCertificateChain(cred.getCertificateChain()));
				dcm.approveDelegation(cred.getIdentity(), res);
				fail("Should not be able to approve delegation.");
			} catch (DelegationFault e) {
				if (e.getFaultString().indexOf(
						Errors.INSUFFICIENT_CERTIFICATE_CHAIN_SPECIFIED) == -1) {
					fail("Should not be able to approve delegation.");
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

	public void testDelegateCredentialMismatchingPublicKeys() {
		DelegatedCredentialManager dcm = null;
		try {
			dcm = Utils.getDelegatedCredentialManager();
			IdentityDelegationPolicy policy = getSimplePolicy();
			String alias = "some user";
			GlobusCredential cred = ca.createCredential(alias);
			DelegationSigningRequest req = dcm.initiateDelegation(cred.getIdentity(),
					policy, Constants.KEY_LENGTH);
			try {
				GlobusCredential proxy = ca.createProxy(alias, 1);
				DelegationSigningResponse res = new DelegationSigningResponse();
				res.setDelegationIdentifier(req.getDelegationIdentifier());
				res.setCertificateChain(org.cagrid.gaards.cds.common.Utils
						.toCertificateChain(proxy.getCertificateChain()));
				dcm.approveDelegation(cred.getIdentity(), res);
				fail("Should not be able to approve delegation.");
			} catch (DelegationFault e) {
				if (e.getFaultString()
						.indexOf(Errors.PUBLIC_KEY_DOES_NOT_MATCH) == -1) {
					fail("Should not be able to approve delegation.");
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
	
	public void testDelegateCredentialInvalidProxyIdentity() {
		DelegatedCredentialManager dcm = null;
		try {
			dcm = Utils.getDelegatedCredentialManager();
			IdentityDelegationPolicy policy = getSimplePolicy();
			String alias = "some user";
			GlobusCredential cred = ca.createCredential(alias);
			DelegationSigningRequest req = dcm.initiateDelegation(cred.getIdentity(),
					policy, Constants.KEY_LENGTH);
			try {
				PublicKey publicKey = KeyUtil.loadPublicKey(req.getPublicKey().getKeyAsString());
				X509Certificate[] certs = ca.createProxyCertifcates(alias+"2",publicKey, 1);
				DelegationSigningResponse res = new DelegationSigningResponse();
				res.setDelegationIdentifier(req.getDelegationIdentifier());
				res.setCertificateChain(org.cagrid.gaards.cds.common.Utils
						.toCertificateChain(certs));
				dcm.approveDelegation(cred.getIdentity(), res);
				fail("Should not be able to approve delegation.");
			} catch (DelegationFault e) {
				if (e.getFaultString()
						.indexOf(Errors.IDENTITY_DOES_NOT_MATCH_INITIATOR) == -1) {
					fail("Should not be able to approve delegation.");
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
	
	public void testDelegateCredentialInvalidProxyChain() {
		DelegatedCredentialManager dcm = null;
		try {
			dcm = Utils.getDelegatedCredentialManager();
			IdentityDelegationPolicy policy = getSimplePolicy();
			String alias = "some user";
			GlobusCredential cred = ca.createCredential(alias);
			DelegationSigningRequest req = dcm.initiateDelegation(cred.getIdentity(),
					policy, Constants.KEY_LENGTH);
			try {
				PublicKey publicKey = KeyUtil.loadPublicKey(req.getPublicKey().getKeyAsString());
				X509Certificate[] certs = ca.createProxyCertifcates(alias,publicKey, 1);
				X509Certificate[] certs2 = ca.createProxyCertifcates(alias+2,publicKey, 1);
				certs[1] = certs2[1];
				DelegationSigningResponse res = new DelegationSigningResponse();
				res.setDelegationIdentifier(req.getDelegationIdentifier());
				res.setCertificateChain(org.cagrid.gaards.cds.common.Utils
						.toCertificateChain(certs));
				dcm.approveDelegation(cred.getIdentity(), res);
				fail("Should not be able to approve delegation.");
			} catch (DelegationFault e) {
				if (e.getFaultString()
						.indexOf(Errors.INVALID_CERTIFICATE_CHAIN) == -1) {
					fail("Should not be able to approve delegation.");
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
	
	public void testDelegateCredentialInsufficientDelegationPathLength() {
		DelegatedCredentialManager dcm = null;
		try {
			dcm = Utils.getDelegatedCredentialManager();
			IdentityDelegationPolicy policy = getSimplePolicy();
			String alias = "some user";
			GlobusCredential cred = ca.createCredential(alias);
			DelegationSigningRequest req = dcm.initiateDelegation(cred.getIdentity(),
					policy, Constants.KEY_LENGTH);
			try {
				PublicKey publicKey = KeyUtil.loadPublicKey(req.getPublicKey().getKeyAsString());
				X509Certificate[] certs = ca.createProxyCertifcates(alias,publicKey, 0);
				DelegationSigningResponse res = new DelegationSigningResponse();
				res.setDelegationIdentifier(req.getDelegationIdentifier());
				res.setCertificateChain(org.cagrid.gaards.cds.common.Utils
						.toCertificateChain(certs));
				dcm.approveDelegation(cred.getIdentity(), res);
				fail("Should not be able to approve delegation.");
			} catch (DelegationFault e) {
				if (e.getFaultString()
						.indexOf(Errors.INSUFFICIENT_DELEGATION_PATH_LENGTH) == -1) {
					fail("Should not be able to approve delegation.");
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

	protected IdentityDelegationPolicy getSimplePolicy() {
		IdentityDelegationPolicy policy = new IdentityDelegationPolicy();
		AllowedParties ap = new AllowedParties();
		ap.setGridIdentity(new String[] { GRID_IDENTITY });
		policy.setAllowedParties(ap);
		return policy;
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
		try {
			this.ca = new CA();
			File f = gov.nih.nci.cagrid.common.Utils.getTrustedCerificatesDirectory();
			f.mkdirs();
			caCert = new File(f.getAbsoluteFile()+File.separator+"cds-test-ca.0");
			CertUtil.writeCertificate(this.ca.getCertificate(),caCert);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	protected void tearDown() throws Exception {
		super.setUp();
		caCert.delete();
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
