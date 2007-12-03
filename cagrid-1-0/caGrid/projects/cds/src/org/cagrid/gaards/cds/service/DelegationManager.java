package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.Utils;

import org.cagrid.gaards.cds.common.CertificateChain;
import org.cagrid.gaards.cds.common.ClientDelegationFilter;
import org.cagrid.gaards.cds.common.DelegationIdentifier;
import org.cagrid.gaards.cds.common.DelegationRecord;
import org.cagrid.gaards.cds.common.DelegationRecordFilter;
import org.cagrid.gaards.cds.common.DelegationRequest;
import org.cagrid.gaards.cds.common.DelegationSigningRequest;
import org.cagrid.gaards.cds.common.DelegationSigningResponse;
import org.cagrid.gaards.cds.common.DelegationStatus;
import org.cagrid.gaards.cds.common.PublicKey;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.gaards.cds.stubs.types.InvalidPolicyFault;
import org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault;

public class DelegationManager {

	private DelegatedCredentialManager dcm;
	private PropertyManager properties;

	public DelegationManager(PropertyManager properties,
			DelegatedCredentialManager dcm) throws CDSInternalFault {
		this.dcm = dcm;
		this.properties = properties;

	}

	public DelegationSigningRequest initiateDelegation(String callerIdentity,
			DelegationRequest req) throws CDSInternalFault, InvalidPolicyFault,
			DelegationFault, PermissionDeniedFault {
		verifyAuthenticated(callerIdentity);
		return this.dcm.initiateDelegation(callerIdentity, req);
	}

	public DelegationRecord[] findCredentialsDelegatedToClient(
			String callerIdentity, ClientDelegationFilter filter) throws CDSInternalFault,
			PermissionDeniedFault {
		verifyAuthenticated(callerIdentity);
		return this.dcm.findCredentialsDelegatedToClient(callerIdentity, filter);
	}

	public DelegationIdentifier approveDelegation(String callerIdentity,
			DelegationSigningResponse res) throws CDSInternalFault,
			DelegationFault, PermissionDeniedFault {
		verifyAuthenticated(callerIdentity);
		return this.dcm.approveDelegation(callerIdentity, res);
	}

	public CertificateChain getDelegatedCredential(String gridIdentity,
			DelegationIdentifier id, PublicKey publicKey)
			throws CDSInternalFault, DelegationFault, PermissionDeniedFault {
		verifyAuthenticated(gridIdentity);
		return this.dcm.getDelegatedCredential(gridIdentity, id, publicKey);
	}

	public void suspendDelegatedCredential(String callerIdentity,
			DelegationIdentifier id) throws CDSInternalFault, DelegationFault,
			PermissionDeniedFault {
		verifyAuthenticated(callerIdentity);
		DelegationRecord r = this.dcm.getDelegationRecord(id);
		if (r.getGridIdentity().equals(callerIdentity)) {
			this.dcm.updateDelegatedCredentialStatus(id,
					DelegationStatus.Suspended);
		} else {
			throw Errors.getPermissionDeniedFault();
		}
	}

	public void updateDelegatedCredentialStatus(String callerIdentity,
			DelegationIdentifier id, DelegationStatus status)
			throws CDSInternalFault, DelegationFault, PermissionDeniedFault {
		verifyAuthenticated(callerIdentity);
		if (isAdmin(callerIdentity)) {
			this.dcm.updateDelegatedCredentialStatus(id, status);
		} else {
			DelegationRecord r = this.dcm.getDelegationRecord(id);
			if (r.getGridIdentity().equals(callerIdentity)) {
				if ((r.getDelegationStatus().equals(DelegationStatus.Approved))
						&& (status.equals(DelegationStatus.Suspended))) {
					this.dcm.updateDelegatedCredentialStatus(id, status);
				} else {
					throw Errors.getPermissionDeniedFault();
				}
			} else {
				throw Errors.getPermissionDeniedFault();
			}
		}
	}

	public DelegationRecord[] findDelegatedCredentials(String callerIdentity,
			DelegationRecordFilter f) throws CDSInternalFault,
			PermissionDeniedFault {
		verifyAuthenticated(callerIdentity);
		if (f == null) {
			f = new DelegationRecordFilter();
		}
		if (isAdmin(callerIdentity)) {
			return this.dcm.findDelegatedCredentials(f);
		} else {
			f.setGridIdentity(callerIdentity);
			return this.dcm.findDelegatedCredentials(f);
		}
	}

	private void verifyAuthenticated(String callerIdentity)
			throws PermissionDeniedFault {
		if (Utils.clean(callerIdentity) == null) {
			throw Errors
					.getPermissionDeniedFault(Errors.AUTHENTICATION_REQUIRED);
		}
	}

	private boolean isAdmin(String gridIdentity) throws CDSInternalFault {
		return false;
	}

	public void clear() throws CDSInternalFault {
		dcm.clearDatabase();
		properties.clearAllProperties();
	}

	public DelegatedCredentialManager getDelegatedCredentialManager() {
		return this.dcm;
	}
}
