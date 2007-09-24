/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.beans;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.client.AuthenticationServiceClient;
import gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.browser.util.AppUtils;
import gov.nih.nci.cagrid.dorian.bean.SAMLAssertion;
import gov.nih.nci.cagrid.dorian.client.DorianClient;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.StateCode;
import gov.nih.nci.cagrid.dorian.ifs.bean.DelegationPathLength;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.PublicKey;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class LoginBean {

	private static Logger logger = Logger.getLogger(LoginBean.class);

	private static final String LOGIN_FAILURE_BAD_IDP_URL = "loginFailed.badIdpUrl";

	private static final String LOGIN_FAILURE_INVALID_CREDENTIALS = "loginFailed.invalidCredentials";

	private static final String LOGIN_FAILURE_INSUFFICIENT_CREDENTIALS = "loginFailed.insufficientCredentials";

	private static final String LOGIN_FAILURE_IDP_ERROR = "loginFailed.idpError";

	private static final String LOGIN_FAILURE_INTERNAL_ERROR = "loginFailed.internalError";

	private static final String LOGIN_FAILURE_IFS_ERROR = "loginFailed.ifsError";

	private static final String LOGIN_FAILURE_BAD_IFS_URL = "loginFailed.badIfsUrl";

	private static final String REGISTRATION_FAILURE_BAD_IDP_URL = "registrationFailed.badIdpUrl";

	private static final String REGISTRATION_FAILURE_IDP_ERROR = "registrationFailed.idpError";

	private static final String REGISTRATION_FAILURE_INTERNAL_ERROR = "registrationFailed.internalError";

	private static final String INVALID_STATE_CODE = null;

	private GlobusCredential credentials;

	private String username;

	private String password;

	private Application newUserInfo = new Application();

	private int proxyValidTime;

	private int delegationPathLength;

	private String idpUrl1;

	private String idpUrl2;

	private String ifsUrl;

	private String loginFailureMessage;

	private String registrationFailureMessage;

	private String registrationSuccessMessage;

	public String doLogin() {

		logger.debug("Logging in.");

		String result = AppUtils.FAILED_METHOD;
		SAMLAssertion saml = null;
		GlobusCredential proxy = null;

		try {
			try {
				saml = authenticate(getIdpUrl1(), getUsername(), getPassword());
			} catch (Exception ex) {
				logger.debug("First authenticate attempt failed: "
						+ ex.getMessage());
			}
			if (saml == null) {
				saml = authenticate(getIdpUrl2(), getUsername(), getPassword());
			}
		} catch (MalformedURIException ex) {
			setLoginFailureMessage(AppUtils
					.getMessage(LOGIN_FAILURE_BAD_IDP_URL));
			logger.error(getLoginFailureMessage(), ex);
		} catch (InvalidCredentialFault ex) {
			setLoginFailureMessage(AppUtils
					.getMessage(LOGIN_FAILURE_INVALID_CREDENTIALS));
			logger.error(getLoginFailureMessage(), ex);
		} catch (InsufficientAttributeFault ex) {
			setLoginFailureMessage(AppUtils
					.getMessage(LOGIN_FAILURE_INSUFFICIENT_CREDENTIALS));
			logger.error(getLoginFailureMessage(), ex);
		} catch (AuthenticationProviderFault ex) {
			setLoginFailureMessage(AppUtils.getMessage(LOGIN_FAILURE_IDP_ERROR));
			logger.error(getLoginFailureMessage(), ex);
		} catch (RemoteException ex) {
			setLoginFailureMessage(AppUtils.getMessage(LOGIN_FAILURE_IDP_ERROR));
			logger.error(getLoginFailureMessage(), ex);
		} catch (Exception ex) {
			setLoginFailureMessage(AppUtils
					.getMessage(LOGIN_FAILURE_INTERNAL_ERROR));
			logger.error(getLoginFailureMessage(), ex);
		}

		if (saml != null) {
			try {
				proxy = getProxy(getIfsUrl(), saml, getProxyValidTime(),
						getDelegationPathLength());
			} catch (MalformedURIException ex) {
				setLoginFailureMessage(AppUtils
						.getMessage(LOGIN_FAILURE_BAD_IFS_URL));
				logger.error(getLoginFailureMessage(), ex);
			} catch (RemoteException ex) {
				setLoginFailureMessage(AppUtils
						.getMessage(LOGIN_FAILURE_IFS_ERROR));
				logger.error(getLoginFailureMessage(), ex);
			} catch (Exception ex) {
				setLoginFailureMessage(AppUtils
						.getMessage(LOGIN_FAILURE_INTERNAL_ERROR));
				logger.error(getLoginFailureMessage(), ex);
			}
		}
		if (proxy != null) {
			result = AppUtils.SUCCESS_METHOD;
			setCredentials(proxy);
		}

		return result;

	}

	public String doLogout() {
		logger.debug("Logging out.");

		clearUserInfo();
		return AppUtils.SUCCESS_METHOD;
	}

	public String doRegister() {
		logger.debug("Registering.");

		String result = AppUtils.FAILED_METHOD;

		try {
			setRegistrationSuccessMessage(register(getIfsUrl(),
					getNewUserInfo()));
			result = AppUtils.SUCCESS_METHOD;
		} catch (MalformedURIException ex) {
			setRegistrationFailureMessage(AppUtils
					.getMessage(REGISTRATION_FAILURE_BAD_IDP_URL));
			logger.error(getRegistrationFailureMessage(), ex);
		} catch (InvalidUserPropertyFault ex){
			setRegistrationFailureMessage(ex.getFaultString());
			logger.error(ex.getFaultString(), ex);
		} catch (RemoteException ex) {
			setRegistrationFailureMessage(AppUtils
					.getMessage(REGISTRATION_FAILURE_IDP_ERROR));
			logger.error(getRegistrationFailureMessage(), ex);
		} catch (Exception ex) {
			setLoginFailureMessage(AppUtils
					.getMessage(REGISTRATION_FAILURE_INTERNAL_ERROR));
			logger.error(getRegistrationFailureMessage(), ex);
		}

		return result;
	}

	public boolean isUserLoggedIn() {
		return this.credentials != null;
	}

	public static String register(String ifsUrl, Application newUserInfo)
			throws MalformedURIException, DorianInternalFault, RemoteException {

		StateCode state = newUserInfo.getState();
		if(state == null){
			newUserInfo.setState(StateCode.fromValue("Outside_US"));
		}
		DorianClient client = new DorianClient(ifsUrl);
		return client.registerWithIdP(newUserInfo);
	}

	public static GlobusCredential getProxy(String ifsUrl, SAMLAssertion saml,
			int proxyValidTime, int delegationPathLength)
			throws MalformedURIException, RemoteException, IOException,
			Exception {

		// Prepare the call to dorian
		ProxyLifetime lifetime = new ProxyLifetime();
		lifetime.setHours(proxyValidTime);
		lifetime.setMinutes(0);
		lifetime.setSeconds(0);
		DelegationPathLength delegationPath = new DelegationPathLength(
				delegationPathLength);
		KeyPair pair = KeyUtil.generateRSAKeyPair512();
		PublicKey key = new PublicKey(KeyUtil.writePublicKey(pair.getPublic()));

		// Make the call
		DorianClient dClient = new DorianClient(ifsUrl);
		gov.nih.nci.cagrid.dorian.bean.X509Certificate list[] = dClient
				.createProxy(saml, key, lifetime, delegationPath);

		// Set up the GridCredential
		X509Certificate[] certs = new X509Certificate[list.length];
		for (int i = 0; i < list.length; i++) {
			certs[i] = CertUtil.loadCertificate(list[i]
					.getCertificateAsString());
		}
		return new GlobusCredential(pair.getPrivate(), certs);

	}

	public void setLoginFailureMessage(String msg) {
		this.loginFailureMessage = msg;
	}

	public String getLoginFailureMessage() {
		return this.loginFailureMessage;
	}

	public static SAMLAssertion authenticate(String idpUrl, String usr,
			String pwd) throws MalformedURIException, InvalidCredentialFault,
			InsufficientAttributeFault, AuthenticationProviderFault,
			RemoteException {
		AuthenticationServiceClient client = new AuthenticationServiceClient(
				idpUrl);
		BasicAuthenticationCredential bac = new BasicAuthenticationCredential();
		bac.setUserId(usr);
		bac.setPassword(pwd);
		Credential cred = new Credential();
		cred.setBasicAuthenticationCredential(bac);
		String xml = client.authenticate(cred).getXml();
		return new SAMLAssertion(xml);
	}

	private void clearUserInfo() {
		setCredentials(null);
		setUsername(null);
		setPassword(null);
		setNewUserInfo(new Application());
		setLoginFailureMessage(null);
		setRegistrationFailureMessage(null);
		setRegistrationSuccessMessage(null);
	}

	public String getIdpUrl1() {
		return idpUrl1;
	}

	public void setIdpUrl1(String idpUrl) {
		this.idpUrl1 = idpUrl;
	}

	public String getIfsUrl() {
		return ifsUrl;
	}

	public void setIfsUrl(String ifsUrl) {
		this.ifsUrl = ifsUrl;
	}

	public GlobusCredential getCredentials() {
		return credentials;
	}

	public void setCredentials(GlobusCredential credentials) {
		this.credentials = credentials;
	}

	public int getDelegationPathLength() {
		return delegationPathLength;
	}

	public void setDelegationPathLength(int delegationPathLength) {
		this.delegationPathLength = delegationPathLength;
	}

	public Application getNewUserInfo() {
		return newUserInfo;
	}

	public void setNewUserInfo(Application newUserInfo) {
		this.newUserInfo = newUserInfo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getProxyValidTime() {
		return proxyValidTime;
	}

	public void setProxyValidTime(int proxyValidTime) {
		this.proxyValidTime = proxyValidTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRegistrationFailureMessage() {
		return registrationFailureMessage;
	}

	public void setRegistrationFailureMessage(String registrationFailureMessage) {
		this.registrationFailureMessage = registrationFailureMessage;
	}

	public String getRegistrationSuccessMessage() {
		return registrationSuccessMessage;
	}

	public void setRegistrationSuccessMessage(String registrationSuccessMessage) {
		this.registrationSuccessMessage = registrationSuccessMessage;
	}

	public String getIdpUrl2() {
		return idpUrl2;
	}

	public void setIdpUrl2(String idpUrl2) {
		this.idpUrl2 = idpUrl2;
	}

}
