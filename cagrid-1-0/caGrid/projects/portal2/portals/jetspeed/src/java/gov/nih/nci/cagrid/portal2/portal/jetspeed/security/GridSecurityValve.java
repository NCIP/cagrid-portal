/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portal.jetspeed.security;

import gov.nih.nci.cagrid.dorian.client.DorianClient;
import gov.nih.nci.cagrid.dorian.ifs.bean.DelegationPathLength;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.PublicKey;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.stubs.types.UserPolicyFault;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.portal2.webauthn.client.WebAuthnSvcClient;
import gov.nih.nci.cagrid.portal2.webauthn.types.UserInfoType;
import gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.SAMLAssertion;
import gov.nih.nci.cagrid.portal2.webauthn.types.faults.InvalidKeyFault;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.KeyPair;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jetspeed.pipeline.PipelineException;
import org.apache.jetspeed.pipeline.valve.ValveContext;
import org.apache.jetspeed.request.RequestContext;
import org.apache.jetspeed.security.SecurityHelper;
import org.apache.jetspeed.security.UserPrincipal;
import org.apache.jetspeed.security.impl.AbstractSecurityValve;
import org.apache.jetspeed.security.impl.RolePrincipalImpl;
import org.apache.jetspeed.security.impl.UserPrincipalImpl;
import org.globus.gsi.GlobusCredential;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class GridSecurityValve extends AbstractSecurityValve {

	private static final Log logger = LogFactory
			.getLog(GridSecurityValve.class);

	private String loginApplicationUrl;

	private String webAuthnSvcUrl;

	private String gridCredentialsSessionKey;

	private String ifsUrl;

	private int proxyLifetimeHours = 12;

	private int proxyLifetimeMinutes = 0;

	private int proxyLifetimeSeconds = 0;

	private int delegationPathLength = 0;

	public GridSecurityValve() {
	}

	public void invoke(RequestContext requestCtx, ValveContext valveCtx)
			throws PipelineException {

		logger.info("# invoking custom security valve #");

		try {
			HttpServletRequest request = requestCtx.getRequest();
			HttpServletResponse response = requestCtx.getResponse();

			Subject subject = getSubjectFromSession(requestCtx);
			if (subject != null) {

				GlobusCredential cred = (GlobusCredential) request.getSession()
						.getAttribute(getGridCredentialsSessionKey());
				if (cred != null && !isValid(cred)) {
					// Then the user's GlobusCredential is no longer valid.
					// So, we need to redirect to login application so that
					// a new SAML assertion can be generated and used to
					// retreive fresh credentials. NOTE: this will not cause
					// the user to have to log in again.
					doLogin(request, response);
				} else {

					// Then the user has already logged in. Just continue
					// processing as normal.
					super.invoke(requestCtx, valveCtx);
				}

			} else {

				// Then the user has either not yet logged in, or
				// is being redirected from the login application.

				// Check for login key in request to see if user
				// is being redirected from login application.
				String loginKey = getLoginKey(request);
				if (loginKey != null) {

					// Then the user is being redirected. The subject
					// will be constructed from the login key by the
					// call to getSubject
					super.invoke(requestCtx, valveCtx);

				} else {
					// This means the user has yet provided credentials to
					// the login application. So, we need to redirect.
					doLogin(requestCtx.getRequest(), requestCtx.getResponse());
				}

			}
		} catch (PipelineException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new PipelineException(
					"Error encountered: " + ex.getMessage(), ex);
		}

	}

	private Subject buildSubjectFromLoginKey(String loginKey,
			HttpServletRequest request) {
		logger.info("Building new subject from login key: " + loginKey);

		// Get userInfo object from WebAuthnSvc.
		UserInfoType userInfo = getUserInfo(loginKey);
		logger.info("Successfully retrieved userInfo for user: "
				+ userInfo.getUsername());

		// TODO: error handling if loginKey doesn't return something

		String username = userInfo.getUsername();
		SAMLAssertion saml = userInfo.getSaml();
		if (saml != null) {
			// Then this is a grid user, so the username
			// will be the grid identity
			GlobusCredential cred = getGridCredentials(saml.getXml());
			logger.info("Successfully retrieved grid credentials for "
					+ cred.getIdentity());
			// TODO: error handling if that failed
			// TODO: validate the credential against the local trust store

			request.getSession().setAttribute(getGridCredentialsSessionKey(),
					cred);
		}

		Set<Principal> principals = new HashSet<Principal>();
		principals.add(new UserPrincipalImpl(username));
		String[] roles = userInfo.getRoles().getRole();
		for (int i = 0; i < roles.length; i++) {
			logger.info("Adding role: " + roles[i]);
			principals.add(new RolePrincipalImpl(roles[i]));
		}

		Subject subject = new Subject(true, principals, Collections.EMPTY_SET,
				Collections.EMPTY_SET);
		return subject;
	}

	private GlobusCredential getGridCredentials(String xml) {

		GlobusCredential cred = null;

		ProxyLifetime lifetime = new ProxyLifetime();
		lifetime.setHours(getProxyLifetimeHours());
		lifetime.setMinutes(getProxyLifetimeMinutes());
		lifetime.setSeconds(getProxyLifetimeSeconds());

		DorianClient dClient = null;
		try {
			dClient = new DorianClient(getIfsUrl());
		} catch (Exception ex) {
			throw new RuntimeException("Error instantiating DorianClient: "
					+ ex.getMessage(), ex);
		}
		KeyPair pair = null;
		PublicKey key = null;
		try {
			pair = KeyUtil.generateRSAKeyPair512();
			key = new PublicKey(KeyUtil.writePublicKey(pair.getPublic()));
		} catch (Exception ex) {
			throw new RuntimeException("Error generating key pair: "
					+ ex.getMessage(), ex);
		}

		gov.nih.nci.cagrid.dorian.bean.SAMLAssertion saml2 = null;
		try {
			saml2 = new gov.nih.nci.cagrid.dorian.bean.SAMLAssertion(xml);
		} catch (Exception ex) {
			throw new RuntimeException("Error parsing SAMLAssertion: "
					+ ex.getMessage(), ex);
		}

		gov.nih.nci.cagrid.dorian.bean.X509Certificate list[] = null;
		try {
			list = dClient.createProxy(saml2, key, lifetime,
					new DelegationPathLength(getDelegationPathLength()));
			// TODO: better error handling
		} catch (InvalidAssertionFault ex) {
			logger.error(ex);
			throw new RuntimeException("The assertion is invalid", ex);
		} catch (InvalidProxyFault ex) {
			logger.error(ex);
			throw new RuntimeException("The proxy is invalid", ex);
		} catch (UserPolicyFault ex) {
			logger.error(ex);
			throw new RuntimeException("User policy error", ex);
		} catch (PermissionDeniedFault ex) {
			logger.error(ex);
			throw new RuntimeException("Permission denied", ex);
		} catch (Exception ex) {
			throw new RuntimeException("Error getting grid credentials: "
					+ ex.getMessage(), ex);
		}
		if (list == null) {
			throw new RuntimeException("Certificate list is null");
		}
		if (list.length == 0) {
			throw new RuntimeException("Certificate list length == 0");
		}
		X509Certificate[] certs = new X509Certificate[list.length];
		for (int i = 0; i < list.length; i++) {
			try {
				certs[i] = CertUtil.loadCertificate(list[i]
						.getCertificateAsString());
			} catch (Exception ex) {
				throw new RuntimeException("Error loading certificate: "
						+ ex.getMessage(), ex);
			}
		}

		try {
			cred = new GlobusCredential(pair.getPrivate(), certs);
		} catch (Exception ex) {
			throw new RuntimeException("Error instantiating GlobusCredential: "
					+ ex.getMessage(), ex);
		}

		return cred;
	}

	private UserInfoType getUserInfo(String loginKey) {

		UserInfoType userInfo = null;
		WebAuthnSvcClient client = null;
		try {
			client = new WebAuthnSvcClient(getWebAuthnSvcUrl());
		} catch (MalformedURIException ex) {
			throw new RuntimeException("Bad URL for WebAuthnSvc: "
					+ ex.getMessage(), ex);
		} catch (RemoteException ex) {
			throw new RuntimeException(
					"Error instantiating WebAuthnSvc client: "
							+ ex.getMessage(), ex);
		}

		try {
			userInfo = client.getUserInfo(loginKey);
		} catch (InvalidKeyFault ex) {
			throw new RuntimeException("The login key is invalid.", ex);
		} catch (RemoteException ex) {
			throw new RuntimeException("Error retrieving user info: "
					+ ex.getMessage(), ex);
		}

		return userInfo;
	}

	private String getLoginKey(HttpServletRequest request) {
		return request.getParameter("cagrid_authn_loginKey");
	}

	private void doLogin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String loginAppUrl = getLoginApplicationUrl();
			logger.info("Redirecting client at " + request.getRemoteAddr()
					+ " to login application: " + loginAppUrl);
			response.sendRedirect(loginAppUrl + "?cagrid_authn_targetUrl="
					+ request.getRequestURL());
		} catch (IOException e) {

			try {
				response.sendError(500, "Unable to redirect to login server:  "
						+ e.toString());
			} catch (IOException e1) {
				throw new PipelineException(
						"Unable to send login error to the client: "
								+ e1.toString());
			}
		}
	}

	private boolean isValid(GlobusCredential cred) {
		// TODO: validate against local trust store
		return true;
	}

	protected Subject getSubject(RequestContext requestCtx) throws Exception {

		Subject subject = getSubjectFromSession(requestCtx);
		if (subject == null) {

			String loginKey = getLoginKey(requestCtx.getRequest());
			if (loginKey == null) {
				// This shouldn't happen
				throw new IllegalStateException(
						"Subject is null and there is no login key");
			}
			subject = buildSubjectFromLoginKey(loginKey, requestCtx
					.getRequest());

		}
		return subject;
	}

	@Override
	protected Principal getUserPrincipal(RequestContext request)
			throws Exception {
		return SecurityHelper.getBestPrincipal(getSubject(request),
				UserPrincipal.class);
	}

	public int getDelegationPathLength() {
		return delegationPathLength;
	}

	public void setDelegationPathLength(int delegationPathLength) {
		this.delegationPathLength = delegationPathLength;
	}

	public String getIfsUrl() {
		return ifsUrl;
	}

	public void setIfsUrl(String ifsUrl) {
		this.ifsUrl = ifsUrl;
	}

	public int getProxyLifetimeHours() {
		return proxyLifetimeHours;
	}

	public void setProxyLifetimeHours(int proxyLifetimeHours) {
		this.proxyLifetimeHours = proxyLifetimeHours;
	}

	public int getProxyLifetimeMinutes() {
		return proxyLifetimeMinutes;
	}

	public void setProxyLifetimeMinutes(int proxyLifetimeMinutes) {
		this.proxyLifetimeMinutes = proxyLifetimeMinutes;
	}

	public int getProxyLifetimeSeconds() {
		return proxyLifetimeSeconds;
	}

	public void setProxyLifetimeSeconds(int proxyLifetimeSeconds) {
		this.proxyLifetimeSeconds = proxyLifetimeSeconds;
	}

	public String getGridCredentialsSessionKey() {
		return gridCredentialsSessionKey;
	}

	public void setGridCredentialsSessionKey(String gridCredentialsSessionKey) {
		this.gridCredentialsSessionKey = gridCredentialsSessionKey;
	}

	public String getLoginApplicationUrl() {
		return loginApplicationUrl;
	}

	public void setLoginApplicationUrl(String loginApplicationUrl) {
		this.loginApplicationUrl = loginApplicationUrl;
	}

	public String getWebAuthnSvcUrl() {
		return webAuthnSvcUrl;
	}

	public void setWebAuthnSvcUrl(String webAuthnSvcUrl) {
		this.webAuthnSvcUrl = webAuthnSvcUrl;
	}
}
