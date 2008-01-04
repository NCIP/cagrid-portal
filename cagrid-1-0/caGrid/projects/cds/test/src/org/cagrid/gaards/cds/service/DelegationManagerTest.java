package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.File;

import junit.framework.TestCase;

import org.cagrid.gaards.cds.common.AllowedParties;
import org.cagrid.gaards.cds.common.DelegatedCredentialAuditFilter;
import org.cagrid.gaards.cds.common.DelegationIdentifier;
import org.cagrid.gaards.cds.common.DelegationPolicy;
import org.cagrid.gaards.cds.common.DelegationRecord;
import org.cagrid.gaards.cds.common.DelegationRecordFilter;
import org.cagrid.gaards.cds.common.DelegationRequest;
import org.cagrid.gaards.cds.common.DelegationSigningRequest;
import org.cagrid.gaards.cds.common.DelegationSigningResponse;
import org.cagrid.gaards.cds.common.DelegationStatus;
import org.cagrid.gaards.cds.common.ExpirationStatus;
import org.cagrid.gaards.cds.common.IdentityDelegationPolicy;
import org.cagrid.gaards.cds.common.ProxyLifetime;
import org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault;
import org.cagrid.gaards.cds.testutils.CA;
import org.cagrid.gaards.cds.testutils.Constants;
import org.cagrid.gaards.cds.testutils.Utils;
import org.globus.gsi.GlobusCredential;

public class DelegationManagerTest extends TestCase {

	private int DEFAULT_PROXY_LIFETIME_SECONDS = 300;

	private CA ca;
	private File caCert;

	public void testDelegatedCredentialCreateDestroy() {
		try {
			DelegationManager cds = Utils.getCDS();
			cds.clear();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}

	public void testUpdateDelegationStatusNonAdminUser() {
		DelegationManager cds = null;
		try {
			cds = Utils.getCDS();
			String leonardoAlias = "leonardo";
			String donatelloAlias = "donatello";

			GlobusCredential leonardoCred = ca.createCredential(leonardoAlias);
			GlobusCredential donatelloCred = ca
					.createCredential(donatelloAlias);

			DelegationPolicy policy = getSimplePolicy(donatelloCred
					.getIdentity());

			DelegationSigningRequest leonardoReq = cds.initiateDelegation(
					leonardoCred.getIdentity(),
					getSimpleDelegationRequest(policy));
			DelegationSigningResponse leonardoRes = new DelegationSigningResponse();
			leonardoRes.setDelegationIdentifier(leonardoReq
					.getDelegationIdentifier());
			leonardoRes.setCertificateChain(org.cagrid.gaards.cds.common.Utils
					.toCertificateChain(ca.createProxyCertifcates(
							leonardoAlias, KeyUtil.loadPublicKey(leonardoReq
									.getPublicKey().getKeyAsString()), 2)));
			cds.approveDelegation(leonardoCred.getIdentity(), leonardoRes);
			DelegationIdentifier id = leonardoReq.getDelegationIdentifier();
			try {
				cds.updateDelegatedCredentialStatus(
						donatelloCred.getIdentity(), id,
						DelegationStatus.Suspended);
				fail("Should not be able to update the status of the delegated credential.");
			} catch (PermissionDeniedFault e) {

			}

			try {
				cds.updateDelegatedCredentialStatus(leonardoCred.getIdentity(),
						id, DelegationStatus.Approved);
				fail("Should not be able to update the status of the delegated credential.");
			} catch (PermissionDeniedFault e) {

			}

			try {
				cds.updateDelegatedCredentialStatus(leonardoCred.getIdentity(),
						id, DelegationStatus.Pending);
				fail("Should not be able to update the status of the delegated credential.");
			} catch (PermissionDeniedFault e) {

			}

			cds.updateDelegatedCredentialStatus(leonardoCred.getIdentity(), id,
					DelegationStatus.Suspended);

			DelegationRecordFilter f = new DelegationRecordFilter();
			f.setDelegationIdentifier(id);
			DelegationRecord[] records = cds.findDelegatedCredentials(
					leonardoCred.getIdentity(), f);
			assertNotNull(records);
			assertEquals(1, records.length);
			assertEquals(DelegationStatus.Suspended, records[0]
					.getDelegationStatus());

			try {
				cds.updateDelegatedCredentialStatus(leonardoCred.getIdentity(),
						id, DelegationStatus.Suspended);
				fail("Should not be able to update the status of the delegated credential.");
			} catch (PermissionDeniedFault e) {

			}

			try {
				cds.updateDelegatedCredentialStatus(leonardoCred.getIdentity(),
						id, DelegationStatus.Approved);
				fail("Should not be able to update the status of the delegated credential.");
			} catch (PermissionDeniedFault e) {

			}

			try {
				cds.updateDelegatedCredentialStatus(leonardoCred.getIdentity(),
						id, DelegationStatus.Pending);
				fail("Should not be able to update the status of the delegated credential.");
			} catch (PermissionDeniedFault e) {

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			if (cds != null) {
				try {
					cds.clear();
				} catch (Exception e) {
				}
			}
		}
	}

	public void testAuditNonAdminUser() {
		DelegationManager cds = null;
		try {
			cds = Utils.getCDS();
			String leonardoAlias = "leonardo";
			String donatelloAlias = "donatello";

			GlobusCredential leonardoCred = ca.createCredential(leonardoAlias);
			GlobusCredential donatelloCred = ca
					.createCredential(donatelloAlias);

			DelegationPolicy policy = getSimplePolicy(donatelloCred
					.getIdentity());

			DelegationSigningRequest leonardoReq = cds.initiateDelegation(
					leonardoCred.getIdentity(),
					getSimpleDelegationRequest(policy));
			DelegationSigningResponse leonardoRes = new DelegationSigningResponse();
			leonardoRes.setDelegationIdentifier(leonardoReq
					.getDelegationIdentifier());
			leonardoRes.setCertificateChain(org.cagrid.gaards.cds.common.Utils
					.toCertificateChain(ca.createProxyCertifcates(
							leonardoAlias, KeyUtil.loadPublicKey(leonardoReq
									.getPublicKey().getKeyAsString()), 2)));
			cds.approveDelegation(leonardoCred.getIdentity(), leonardoRes);
			DelegationIdentifier id = leonardoReq.getDelegationIdentifier();

			DelegatedCredentialAuditFilter f = null;

			try {
				cds.searchDelegatedCredentialAuditLog(leonardoCred
						.getIdentity(), f);
				fail("Should not be able to search the audit log.");
			} catch (PermissionDeniedFault e) {
				if (!e
						.getFaultString()
						.equals(
								Errors.PERMISSION_DENIED_NO_DELEGATED_CREDENTIAL_SPECIFIED)) {
					fail("Should not be able to search the audit log.");
				}
			}
			f = new DelegatedCredentialAuditFilter();
			try {
				cds.searchDelegatedCredentialAuditLog(leonardoCred
						.getIdentity(), f);
				fail("Should not be able to search the audit log.");
			} catch (PermissionDeniedFault e) {
				if (!e
						.getFaultString()
						.equals(
								Errors.PERMISSION_DENIED_NO_DELEGATED_CREDENTIAL_SPECIFIED)) {
					fail("Should not be able to search the audit log.");
				}
			}

			f.setDelegationIdentifier(id);
			assertEquals(2, cds.searchDelegatedCredentialAuditLog(leonardoCred
					.getIdentity(), f).length);

			try {
				cds.searchDelegatedCredentialAuditLog(donatelloCred
						.getIdentity(), f);
				fail("Should not be able to search the audit log.");
			} catch (PermissionDeniedFault e) {
				if (!e.getFaultString().equals(
						Errors.PERMISSION_DENIED_TO_AUDIT)) {
					fail("Should not be able to search the audit log.");
				}
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			if (cds != null) {
				try {
					cds.clear();
				} catch (Exception e) {
				}
			}
		}
	}

	public void testFindMyDelegatedCredentials() {
		DelegationManager cds = null;
		try {
			cds = Utils.getCDS();
			String leonardoAlias = "leonardo";
			String donatelloAlias = "donatello";
			String michelangeloAlias = "michelangelo";

			GlobusCredential leonardoCred = ca.createCredential(leonardoAlias);
			GlobusCredential donatelloCred = ca
					.createCredential(donatelloAlias);
			GlobusCredential michelangeloCred = ca
					.createCredential(michelangeloAlias);

			DelegationPolicy policy = getSimplePolicy(michelangeloCred
					.getIdentity());

			DelegationSigningRequest leonardoReq = cds.initiateDelegation(
					leonardoCred.getIdentity(),
					getSimpleDelegationRequest(policy));
			DelegationSigningResponse leonardoRes = new DelegationSigningResponse();
			leonardoRes.setDelegationIdentifier(leonardoReq
					.getDelegationIdentifier());
			leonardoRes.setCertificateChain(org.cagrid.gaards.cds.common.Utils
					.toCertificateChain(ca.createProxyCertifcates(
							leonardoAlias, KeyUtil.loadPublicKey(leonardoReq
									.getPublicKey().getKeyAsString()), 2)));
			cds.approveDelegation(leonardoCred.getIdentity(), leonardoRes);

			DelegationSigningRequest donatelloReq = cds.initiateDelegation(
					donatelloCred.getIdentity(),
					getSimpleDelegationRequest(policy));
			DelegationSigningResponse donatelloRes = new DelegationSigningResponse();
			donatelloRes.setDelegationIdentifier(donatelloReq
					.getDelegationIdentifier());
			donatelloRes.setCertificateChain(org.cagrid.gaards.cds.common.Utils
					.toCertificateChain(ca.createProxyCertifcates(
							donatelloAlias, KeyUtil.loadPublicKey(donatelloReq
									.getPublicKey().getKeyAsString()), 2)));
			cds.approveDelegation(donatelloCred.getIdentity(), donatelloRes);

			cds.initiateDelegation(donatelloCred.getIdentity(),
					getSimpleDelegationRequest(policy));

			DelegationRecordFilter f = new DelegationRecordFilter();
			validateFindMy(cds, leonardoCred.getIdentity(), f, 1);
			validateFindMy(cds, donatelloCred.getIdentity(), f, 2);
			validateFindMy(cds, michelangeloCred.getIdentity(), f, 0);

			resetFilter(f);
			f.setDelegationIdentifier(leonardoReq.getDelegationIdentifier());
			validateFindMy(cds, leonardoCred.getIdentity(), f, 1);
			validateFindMy(cds, donatelloCred.getIdentity(), f, 0);
			validateFindMy(cds, michelangeloCred.getIdentity(), f, 0);

			resetFilter(f);
			f.setDelegationIdentifier(donatelloReq.getDelegationIdentifier());
			validateFindMy(cds, leonardoCred.getIdentity(), f, 0);
			validateFindMy(cds, donatelloCred.getIdentity(), f, 1);
			validateFindMy(cds, michelangeloCred.getIdentity(), f, 0);

			resetFilter(f);
			f.setGridIdentity(leonardoCred.getIdentity());
			validateFindMy(cds, leonardoCred.getIdentity(), f, 1);
			validateFindMy(cds, donatelloCred.getIdentity(), f, 2);
			validateFindMy(cds, michelangeloCred.getIdentity(), f, 0);

			resetFilter(f);
			f.setGridIdentity(donatelloCred.getIdentity());
			validateFindMy(cds, leonardoCred.getIdentity(), f, 1);
			validateFindMy(cds, donatelloCred.getIdentity(), f, 2);
			validateFindMy(cds, michelangeloCred.getIdentity(), f, 0);

			resetFilter(f);
			f.setGridIdentity(michelangeloCred.getIdentity());
			validateFindMy(cds, leonardoCred.getIdentity(), f, 1);
			validateFindMy(cds, donatelloCred.getIdentity(), f, 2);
			validateFindMy(cds, michelangeloCred.getIdentity(), f, 0);

			resetFilter(f);
			f.setExpirationStatus(ExpirationStatus.Valid);
			validateFindMy(cds, leonardoCred.getIdentity(), f, 1);
			validateFindMy(cds, donatelloCred.getIdentity(), f, 1);
			validateFindMy(cds, michelangeloCred.getIdentity(), f, 0);

			resetFilter(f);
			f.setExpirationStatus(ExpirationStatus.Expired);
			validateFindMy(cds, leonardoCred.getIdentity(), f, 0);
			validateFindMy(cds, donatelloCred.getIdentity(), f, 0);
			validateFindMy(cds, michelangeloCred.getIdentity(), f, 0);

			resetFilter(f);
			f.setDelegationStatus(DelegationStatus.Approved);
			validateFindMy(cds, leonardoCred.getIdentity(), f, 1);
			validateFindMy(cds, donatelloCred.getIdentity(), f, 1);
			validateFindMy(cds, michelangeloCred.getIdentity(), f, 0);

			resetFilter(f);
			f.setDelegationStatus(DelegationStatus.Pending);
			validateFindMy(cds, leonardoCred.getIdentity(), f, 0);
			validateFindMy(cds, donatelloCred.getIdentity(), f, 1);
			validateFindMy(cds, michelangeloCred.getIdentity(), f, 0);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			if (cds != null) {
				try {
					cds.clear();
				} catch (Exception e) {
				}
			}
		}
	}

	protected void resetFilter(DelegationRecordFilter f) throws Exception {
		f.setDelegationIdentifier(null);
		f.setGridIdentity(null);
		f.setDelegationStatus(null);
		f.setExpirationStatus(null);
	}

	protected void validateFindMy(DelegationManager cds, String gridIdentity,
			DelegationRecordFilter f, int expectedCount) throws Exception {
		DelegationRecord[] records = cds.findDelegatedCredentials(gridIdentity,
				f);
		assertEquals(expectedCount, records.length);
		if (f.getDelegationIdentifier() != null) {
			for (int i = 0; i < records.length; i++) {
				assertEquals(f.getDelegationIdentifier(), records[i]
						.getDelegationIdentifier());
			}
		}

		for (int i = 0; i < records.length; i++) {
			assertEquals(gridIdentity, records[i].getGridIdentity());
		}

		if (f.getDelegationStatus() != null) {
			for (int i = 0; i < records.length; i++) {
				assertEquals(f.getDelegationStatus(), records[i]
						.getDelegationStatus());
			}
		}
	}

	protected IdentityDelegationPolicy getSimplePolicy(String gridIdentity) {
		IdentityDelegationPolicy policy = new IdentityDelegationPolicy();
		AllowedParties ap = new AllowedParties();
		ap.setGridIdentity(new String[] { gridIdentity });
		policy.setAllowedParties(ap);
		return policy;
	}

	protected DelegationRequest getSimpleDelegationRequest(
			DelegationPolicy policy) {
		DelegationRequest req = new DelegationRequest();
		req.setDelegationPolicy(policy);
		req.setKeyLength(Constants.KEY_LENGTH);
		req.setIssuedCredentialPathLength(Constants.DELEGATION_PATH_LENGTH);
		ProxyLifetime lifetime = new ProxyLifetime();
		lifetime.setHours(0);
		lifetime.setMinutes(0);
		lifetime.setSeconds(DEFAULT_PROXY_LIFETIME_SECONDS);
		req.setIssuedCredentialLifetime(lifetime);
		return req;
	}

	protected void setUp() throws Exception {
		super.setUp();
		Utils.getDatabase().createDatabaseIfNeeded();
		try {
			this.ca = new CA();
			File f = gov.nih.nci.cagrid.common.Utils
					.getTrustedCerificatesDirectory();
			f.mkdirs();
			caCert = new File(f.getAbsoluteFile() + File.separator
					+ "cds-test-ca.0");
			CertUtil.writeCertificate(this.ca.getCertificate(), caCert);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	protected void tearDown() throws Exception {
		super.setUp();
		caCert.delete();
	}

}
