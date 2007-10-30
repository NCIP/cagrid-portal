package org.cagrid.gaards.cds.client;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.security.ProxyUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.gridca.common.ProxyCreator;

import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.apache.axis.types.URI;
import org.cagrid.gaards.cds.common.AllowedParties;
import org.cagrid.gaards.cds.common.CertificateChain;
import org.cagrid.gaards.cds.common.DelegationPolicy;
import org.cagrid.gaards.cds.common.DelegationRequest;
import org.cagrid.gaards.cds.common.DelegationSigningRequest;
import org.cagrid.gaards.cds.common.DelegationSigningResponse;
import org.cagrid.gaards.cds.common.IdentityDelegationPolicy;
import org.cagrid.gaards.cds.common.ProxyLifetime;
import org.cagrid.gaards.cds.common.Utils;
import org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault;
import org.globus.gsi.GlobusCredential;

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

	public DelegatedCredentialReference delegateCredential(
			DelegationPolicy policy, ProxyLifetime delegatedCredentialsLifetime)
			throws RemoteException, CDSInternalFault, DelegationFault,
			PermissionDeniedFault, URI.MalformedURIException {
		return this.delegateCredential(null, policy,
				delegatedCredentialsLifetime);
	}

	public DelegatedCredentialReference delegateCredential(
			ProxyLifetime delegationLifetime, DelegationPolicy policy,
			ProxyLifetime delegatedCredentialsLifetime) throws RemoteException,
			CDSInternalFault, DelegationFault, PermissionDeniedFault,
			URI.MalformedURIException {
		return this.delegateCredential(delegationLifetime, 1, policy,
				delegatedCredentialsLifetime, 0,
				ClientConstants.DEFAULT_KEY_SIZE);
	}

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

	public static void main(String[] args) {
		try {
			DelegationUserClient client = new DelegationUserClient(
					"https://localhost:8443/wsrf/services/cagrid/CredentialDelegationService");
			ProxyLifetime dl = new ProxyLifetime();
			dl.setSeconds(500);
			ProxyLifetime dcl = new ProxyLifetime();
			dcl.setSeconds(250);
			IdentityDelegationPolicy policy = new IdentityDelegationPolicy();
			AllowedParties ap = new AllowedParties();
			ap
					.setGridIdentity(new String[] { "/O=caBIG/OU=caGrid/OU=Training/OU=Dorian/CN=langella" });
			policy.setAllowedParties(ap);
			DelegatedCredentialReference ref = client.delegateCredential(dl, 1,
					policy, dcl, 0, 1024);

			DelegatedCredentialUserClient client2 = new DelegatedCredentialUserClient(
					ref);
			GlobusCredential proxy = client2.getDelegatedCredential();
			System.out.println(proxy.getIdentity());
			ProxyUtil.saveProxyAsDefault(proxy);
		} catch (Exception e) {
			FaultUtil.printFault(e);
		}

	}

}
