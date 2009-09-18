/**
 *
 */
package gov.nih.nci.cagrid.portal.security;

import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.bean.SAMLAssertion;
import gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.dorian.client.DorianClient;
import gov.nih.nci.cagrid.dorian.ifs.bean.DelegationPathLength;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.PublicKey;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.stubs.types.UserPolicyFault;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

import org.cagrid.gaards.authentication.client.AuthenticationServiceClient;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.pki.KeyUtil;
import org.globus.gsi.GlobusCredential;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public class AuthenticationHelper {

	private long timeout = 10000L;

	/**
	 * 
	 */
	public AuthenticationHelper() {

	}

	public GlobusCredential authenticate(String username, String password,
			String idpUrl, String ifsUrl, int proxyLifetimeHours,
			int delegationPathLength) throws AuthnServiceException,
			InvalidCredentialFault, AuthnTimeoutException {

		BasicAuthenticationCredential bac = new BasicAuthenticationCredential();
		bac.setUserId(username);
		bac.setPassword(password);
		Credential credential = new Credential();
		credential.setBasicAuthenticationCredential(bac);
		AuthenticationServiceClient idpClient;
		try {
			idpClient = new AuthenticationServiceClient(idpUrl);
		} catch (Exception ex) {
			throw new AuthnServiceException(
					"Error instantiating AuthenticationServiceClient: "
							+ ex.getMessage(), ex);
		}

		GetSAMLThread getSaml = new GetSAMLThread(idpClient, credential);
		getSaml.start();
		try {
			getSaml.join(getTimeout());
		} catch (Exception ex) {

		}
		if (getSaml.ex != null) {
			if (getSaml.ex instanceof InvalidCredentialFault) {
				throw (InvalidCredentialFault) getSaml.ex;
			} else if (getSaml.ex instanceof InsufficientAttributeFault) {
				throw new AuthnServiceException(
						((InsufficientAttributeFault) getSaml.ex)
								.getFaultString(), getSaml.ex);
			} else if (getSaml.ex instanceof AuthenticationProviderFault) {
				throw new AuthnServiceException(
						((AuthenticationProviderFault) getSaml.ex)
								.getFaultString(), getSaml.ex);
			} else {
				throw new AuthnServiceException("Error getting SAML: "
						+ getSaml.ex.getMessage(), getSaml.ex);
			}
		}
		if (!getSaml.finished) {
			throw new AuthnTimeoutException(
					"Authentication with IDP timed out.");
		}

		GlobusCredential cred = null;

		ProxyLifetime lifetime = new ProxyLifetime();
		lifetime.setHours(proxyLifetimeHours);
		lifetime.setMinutes(0);
		lifetime.setSeconds(0);

		DorianClient ifsClient = null;
		try {
			ifsClient = new DorianClient(ifsUrl);
		} catch (Exception ex) {
			throw new AuthnServiceException(
					"Error instantiating DorianClient: " + ex.getMessage(), ex);
		}
		KeyPair pair = null;
		PublicKey key = null;
		try {
			pair = KeyUtil.generateRSAKeyPair512();
			key = new PublicKey(KeyUtil.writePublicKey(pair.getPublic()));
		} catch (Exception ex) {
			throw new AuthnServiceException("Error generating key pair: "
					+ ex.getMessage(), ex);
		}

		gov.nih.nci.cagrid.dorian.bean.SAMLAssertion saml2 = null;
		try {
			saml2 = new gov.nih.nci.cagrid.dorian.bean.SAMLAssertion(
					getSaml.saml.getXml());
		} catch (Exception ex) {
			throw new AuthnServiceException("Error parsing SAMLAssertion: "
					+ ex.getMessage(), ex);
		}

		GetCertsThread getCerts = new GetCertsThread(ifsClient, saml2, key,
				lifetime, new DelegationPathLength(delegationPathLength));
		getCerts.start();
		try {
			getCerts.join(getTimeout());
		} catch (Exception ex) {

		}

		if (getCerts.ex != null) {
			String msg = null;
			if (getCerts.ex instanceof InvalidAssertionFault) {
				msg = ((InvalidAssertionFault) getCerts.ex).getFaultString();
			} else if (getCerts.ex instanceof InvalidProxyFault) {
				msg = ((InvalidProxyFault) getCerts.ex).getFaultString();
			} else if (getCerts.ex instanceof UserPolicyFault) {
				msg = ((UserPolicyFault) getCerts.ex).getFaultString();
			} else if (getCerts.ex instanceof PermissionDeniedFault) {
				msg = ((PermissionDeniedFault) getCerts.ex).getFaultString();
			} else {
				msg = getCerts.ex.getMessage();
			}
			throw new AuthnServiceException("Error authenticating to IFS: "
					+ msg, getCerts.ex);
		}

		if (!getCerts.finished) {
			throw new AuthnTimeoutException(
					"Authentication with IFS timed out.");
		}

		X509Certificate[] certs = new X509Certificate[getCerts.certs.length];
		for (int i = 0; i < getCerts.certs.length; i++) {
			try {
				certs[i] = CertUtil.loadCertificate(getCerts.certs[i]
						.getCertificateAsString());
			} catch (Exception ex) {
				throw new AuthnServiceException("Error loading certificate: "
						+ ex.getMessage(), ex);
			}
		}

		try {
			cred = new GlobusCredential(pair.getPrivate(), certs);
		} catch (Exception ex) {
			throw new AuthnServiceException(
					"Error instantiating GlobusCredential: " + ex.getMessage(),
					ex);
		}
		return cred;
	}

	private static class GetSAMLThread extends Thread {
		private AuthenticationServiceClient client;
		private Credential credential;
		SAMLAssertion saml;
		Exception ex;
		boolean finished;

		GetSAMLThread(AuthenticationServiceClient client, Credential credential) {
			this.client = client;
			this.credential = credential;
		}

		public void run() {
			try {
				this.saml = this.client.authenticate(this.credential);
				this.finished = true;
			} catch (Exception ex) {
				this.ex = ex;
			}
		}
	}

	private static class GetCertsThread extends Thread {
		private DorianClient client;
		private gov.nih.nci.cagrid.dorian.bean.SAMLAssertion saml;
		private PublicKey key;
		private ProxyLifetime lifetime;
		private DelegationPathLength delegationPathLength;
		Exception ex;
		gov.nih.nci.cagrid.dorian.bean.X509Certificate[] certs;
		boolean finished;

		GetCertsThread(DorianClient client,
				gov.nih.nci.cagrid.dorian.bean.SAMLAssertion saml,
				PublicKey key, ProxyLifetime lifetime,
				DelegationPathLength delegationPathLength) {
			this.client = client;
			this.saml = saml;
			this.key = key;
			this.lifetime = lifetime;
			this.delegationPathLength = delegationPathLength;
		}

		public void run() {
			try {
				this.certs = this.client.createProxy(this.saml, this.key,
						this.lifetime, this.delegationPathLength);
				this.finished = true;
			} catch (Exception ex) {
				this.ex = ex;
			}
		}
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
