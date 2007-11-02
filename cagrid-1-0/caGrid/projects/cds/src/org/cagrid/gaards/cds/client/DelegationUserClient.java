package org.cagrid.gaards.cds.client;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.security.ProxyUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.gridca.common.ProxyCreator;

import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.axis.types.URI;
import org.cagrid.gaards.cds.common.CertificateChain;
import org.cagrid.gaards.cds.common.DelegationPolicy;
import org.cagrid.gaards.cds.common.DelegationRecord;
import org.cagrid.gaards.cds.common.DelegationRecordFilter;
import org.cagrid.gaards.cds.common.DelegationRequest;
import org.cagrid.gaards.cds.common.DelegationSigningRequest;
import org.cagrid.gaards.cds.common.DelegationSigningResponse;
import org.cagrid.gaards.cds.common.ProxyLifetime;
import org.cagrid.gaards.cds.common.Utils;
import org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault;
import org.globus.gsi.GlobusCredential;

/**
 * @author langella
 * 
 */
public class DelegationUserClient {

	private GlobusCredential cred;
	private CredentialDelegationServiceClient client;

	public DelegationUserClient(String url) throws Exception {
		this(url, ProxyUtil.getDefaultProxy());
	}

	public DelegationUserClient(String url, GlobusCredential cred)
			throws Exception {
		this.cred = cred;
		this.client = new CredentialDelegationServiceClient(url, cred);
	}

	
	/**
	 * This method allows a user to delegated their credential to the Credential
	 * Delegation Service
	 * 
	 * @param policy
	 *            The policy specifying who may request this delegated
	 *            credential from the Credential Delegation Service
	 * @param delegatedCredentialsLifetime
	 *            The life time of the credentials delegated to entities by the
	 *            Credential Delegation Service on you behalf.
	 * @return A reference to the delegated credential, this reference may be
	 *         used by entites to request a credential.
	 * @throws RemoteException 
	 * @throws CDSInternalFault 
	 * @throws DelegationFault 
	 * @throws PermissionDeniedFault
	 * @throws URI.MalformedURIException
	 */
	public DelegatedCredentialReference delegateCredential(
			DelegationPolicy policy, ProxyLifetime delegatedCredentialsLifetime)
			throws RemoteException, CDSInternalFault, DelegationFault,
			PermissionDeniedFault, URI.MalformedURIException {
		return this.delegateCredential(null, policy,
				delegatedCredentialsLifetime);
	}

	/**
	 * This method allows a user to delegated their credential to the Credential
	 * Delegation Service
	 * 
	 * @param delegationLifetime
	 *            The life time of the credential being delegated to the
	 *            Credential Delegation Service. This lifetime specifies how
	 *            long the Credential Delegation Service may delegate this
	 *            credential.
	 * @param policy
	 *            The policy specifying who may request this delegated
	 *            credential from the Credential Delegation Service
	 * @param delegatedCredentialsLifetime
	 *            The life time of the credentials delegated to entities by the
	 *            Credential Delegation Service on you behalf.
	 * @return A reference to the delegated credential, this reference may be
	 *         used by entites to request a credential.
	 * @throws RemoteException 
	 * @throws CDSInternalFault 
	 * @throws DelegationFault 
	 * @throws PermissionDeniedFault
	 * @throws URI.MalformedURIException
	 */
	public DelegatedCredentialReference delegateCredential(
			ProxyLifetime delegationLifetime, DelegationPolicy policy,
			ProxyLifetime delegatedCredentialsLifetime) throws RemoteException,
			CDSInternalFault, DelegationFault, PermissionDeniedFault,
			URI.MalformedURIException {
		return this.delegateCredential(delegationLifetime, 1, policy,
				delegatedCredentialsLifetime, 0,
				ClientConstants.DEFAULT_KEY_SIZE);
	}

	/**
	 * This method allows a user to delegated their credential to the Credential
	 * Delegation Service
	 * 
	 * @param delegationLifetime
	 *            The life time of the credential being delegated to the
	 *            Credential Delegation Service. This lifetime specifies how
	 *            long the Credential Delegation Service may delegate this
	 *            credential.
	 * @param delegationPathLength
	 *            The delegation path length of the credential being delegated
	 *            to the Credential Delegation Service.
	 * @param policy
	 *            The policy specifying who may request this delegated
	 *            credential from the Credential Delegation Service
	 * @param delegatedCredentialsLifetime
	 *            The life time of the credentials delegated to entities by the
	 *            Credential Delegation Service on you behalf.
	 * @param delegatedCredentialsPathLength
	 *            The path length of the credentials delegated to entities by
	 *            the Credential Delegation Service on you behalf. A path length
	 *            of 0 means that entities that can you obtain a delegated
	 *            credential cannot further delegate it.
	 * @param delegatedCredentialsKeyLength
	 *            The key length of the credentials delegated to entities by the
	 *            Credential Delegation Service.
	 * @return A reference to the delegated credential, this reference may be
	 *         used by entites to request a credential.
	 * @throws RemoteException 
	 * @throws CDSInternalFault 
	 * @throws DelegationFault 
	 * @throws PermissionDeniedFault
	 * @throws URI.MalformedURIException
	 */
	public DelegatedCredentialReference delegateCredential(
			ProxyLifetime delegationLifetime, int delegationPathLength,
			DelegationPolicy policy,
			ProxyLifetime delegatedCredentialsLifetime,
			int delegatedCredentialsPathLength,
			int delegatedCredentialsKeyLength) throws RemoteException,
			CDSInternalFault, DelegationFault, PermissionDeniedFault,
			URI.MalformedURIException {

		DelegationRequest req = new DelegationRequest();
		req.setDelegationPolicy(policy);
		req.setDelegatedProxyLifetime(delegatedCredentialsLifetime);
		req.setDelegationPathLength(delegatedCredentialsPathLength);
		req.setKeyLength(delegatedCredentialsKeyLength);
		DelegationSigningRequest dsr = client.initiateDelegation(req);

		int hours = 0;
		int minutes = 0;
		int seconds = 0;

		if (delegationLifetime != null) {
			hours = delegationLifetime.getHours();
			minutes = delegationLifetime.getMinutes();
			seconds = delegationLifetime.getSeconds();
		}
		CertificateChain chain = null;
		try {
			PublicKey publicKey = KeyUtil.loadPublicKey(dsr.getPublicKey()
					.getKeyAsString());

			X509Certificate[] certs = ProxyCreator
					.createImpersonationProxyCertificate(cred
							.getCertificateChain(), cred.getPrivateKey(),
							publicKey, hours, minutes, seconds,
							delegationPathLength);
			chain = Utils.toCertificateChain(certs);

		} catch (Exception e) {
			DelegationFault f = new DelegationFault();
			f.setFaultString("Unexpected error creating proxy: "
					+ e.getMessage() + ".");
			FaultHelper helper = new FaultHelper(f);
			helper.addFaultCause(e);
			f = (DelegationFault) helper.getFault();
			throw f;
		}
		DelegationSigningResponse res = new DelegationSigningResponse();
		res.setDelegationIdentifier(dsr.getDelegationIdentifier());
		res.setCertificateChain(chain);
		return client.approveDelegation(res);
	}

	public List<DelegationRecord> findMyDelegatedCredentials(
			DelegationRecordFilter filter) throws RemoteException,
			CDSInternalFault {
		DelegationRecord[] records = client.findMyDelegatedCredentials(filter);
		List<DelegationRecord> list = Arrays.asList(records);
		return list;
	}

}
