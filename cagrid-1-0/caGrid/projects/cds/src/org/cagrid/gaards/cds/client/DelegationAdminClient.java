package org.cagrid.gaards.cds.client;

import gov.nih.nci.cagrid.common.security.ProxyUtil;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cagrid.gaards.cds.common.DelegationIdentifier;
import org.cagrid.gaards.cds.common.DelegationRecord;
import org.cagrid.gaards.cds.common.DelegationRecordFilter;
import org.cagrid.gaards.cds.common.DelegationStatus;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault;
import org.globus.gsi.GlobusCredential;

/**
 * @author langella
 * 
 */
public class DelegationAdminClient {
	private CredentialDelegationServiceClient client;

	public DelegationAdminClient(String url) throws Exception {
		this(url, ProxyUtil.getDefaultProxy());
	}

	public DelegationAdminClient(String url, GlobusCredential cred)
			throws Exception {
		this.client = new CredentialDelegationServiceClient(url, cred);
	}

	/**
	 * This method allows a admin to find credentials that have been delegated.
	 * 
	 * @return A list of records each representing a credential delegated.
	 * @throws RemoteException
	 * @throws CDSInternalFault
	 * @throws DelegationInternalFault
	 * @throws PermissionDeniedFault
	 */

	public List<DelegationRecord> findDelegatedCredentials()
			throws RemoteException, CDSInternalFault, DelegationFault,
			PermissionDeniedFault {
		return findDelegatedCredentials(new DelegationRecordFilter());
	}

	/**
	 * This method allows a admin to find credentials that have been delegated.
	 * 
	 * @param filter
	 *            Search criteria to use in finding delegated credentials
	 * @return A list of records each representing a credential delegated.
	 * @throws RemoteException
	 * @throws CDSInternalFault
	 * @throws DelegationInternalFault
	 * @throws PermissionDeniedFault
	 */

	public List<DelegationRecord> findDelegatedCredentials(
			DelegationRecordFilter filter) throws RemoteException,
			CDSInternalFault, DelegationFault, PermissionDeniedFault {
		if (filter == null) {
			filter = new DelegationRecordFilter();
		}

		DelegationRecord[] records = client.findDelegatedCredentials(filter);
		if (records == null) {
			return new ArrayList<DelegationRecord>();
		} else {
			List<DelegationRecord> list = Arrays.asList(records);
			return list;
		}
	}

	/**
	 * This method allows and admin to update the status of a delegated
	 * credential.
	 * 
	 * @param id
	 *            The delegation identifier of the delegated credentials to
	 *            suspend.
	 * @param status
	 *            The updated delegation status.
	 * @throws RemoteException
	 * @throws CDSInternalFault
	 * @throws DelegationFault
	 * @throws PermissionDeniedFault
	 */

	public void updateDelegationStatus(DelegationIdentifier id,
			DelegationStatus status) throws RemoteException, CDSInternalFault,
			DelegationFault, PermissionDeniedFault {
		client.updateDelegatedCredentialStatus(id, status);
	}

}
