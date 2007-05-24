/**
 * 
 */
package gov.nih.nci.cagrid.portal2.webauthn.common;

import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.authentication.client.AuthenticationServiceClient;
import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.portal2.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal2.dao.RoleDao;
import gov.nih.nci.cagrid.portal2.domain.GridPortalUser;
import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.domain.Role;
import gov.nih.nci.cagrid.portal2.webauthn.types.UserInfoType;
import gov.nih.nci.cagrid.portal2.webauthn.types.UserInfoTypeRoles;
import gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.Credential;
import gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.SAMLAssertion;
import gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.faults.AuthenticationProviderFault;
import gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.faults.InsufficientAttributeFault;
import gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.faults.InvalidCredentialFault;
import gov.nih.nci.cagrid.portal2.webauthn.types.faults.InvalidKeyFault;
import gov.nih.nci.cagrid.portal2.webauthn.types.faults.WebAuthnSvcFault;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis.wsrf.faults.BaseFaultType;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class WebAuthnSvc implements WebAuthnSvcI {

	private static final Log logger = LogFactory.getLog(WebAuthnSvc.class);

	// TODO: persist this to database
	//Also need clean up thread
	private Map<String, UserInfoType> userInfo = new Hashtable<String, UserInfoType>();

	private PortalUserDao portalUserDao;

	private RoleDao roleDao;

	private List<Role> defaultRoles;

	public WebAuthnSvc() {
	}

	public String createLoginKeyForLocalUser(Credential credential)
			throws RemoteException, InvalidCredentialFault,
			InsufficientAttributeFault, AuthenticationProviderFault,
			WebAuthnSvcFault {

		assertBasicAuthn(credential);

		// TODO: replace this with some pluggable authenticator
		PortalUser user = null;
		try {
			PortalUser userEg = new PortalUser();
			userEg.setUsername(credential.getBasicAuthenticationCredential()
					.getUserId());
			user = getPortalUserDao().getByExample(userEg);
		} catch (Exception ex) {
			FaultHelper helper = new FaultHelper(new WebAuthnSvcFault());
			helper.setDescription("Error retrieving user data: "
					+ ex.getMessage());
			helper.addFaultCause(ex);
			throw (WebAuthnSvcFault) helper.getFault();
		}

		if (user == null) {
			logger.info("User "
					+ credential.getBasicAuthenticationCredential().getUserId()
					+ " not found. Throwing InvalidCredentialFault.");
			throw new InvalidCredentialFault();
		}

		// TODO: do some encryption/decryption
		if (!user.getPassword().equals(
				credential.getBasicAuthenticationCredential().getPassword())) {
			logger.info("Invalid password for user '"
					+ credential.getBasicAuthenticationCredential().getUserId()
					+ "'.");
			throw new InvalidCredentialFault();
		}
		String loginKey = generateLoginKey(user.getUsername());
		
		UserInfoType userInfoObj = new UserInfoType();
		userInfoObj.setUsername(user.getUsername());
		populateRoles(userInfoObj);
		this.userInfo.put(loginKey, userInfoObj);
		
		return loginKey;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.webauthn.common.WebAuthnSvcI#createLoginKey(gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.Credential)
	 */
	public String createLoginKeyForGridUser(String idpUrl, Credential credential)
			throws RemoteException, InvalidCredentialFault,
			InsufficientAttributeFault, AuthenticationProviderFault,
			WebAuthnSvcFault {

		assertBasicAuthn(credential);

		AuthenticationServiceClient client = null;
		try {
			client = new AuthenticationServiceClient(idpUrl);
		} catch (Exception ex) {
			WebAuthnSvcFault fault = new WebAuthnSvcFault();
			fault
					.setFaultDetailString("Error instantiating AuthenticationServiceClient: "
							+ ex.getMessage());
		}
		BasicAuthenticationCredential bac = new BasicAuthenticationCredential();
		bac
				.setUserId(credential.getBasicAuthenticationCredential()
						.getUserId());
		bac.setPassword(credential.getBasicAuthenticationCredential()
				.getPassword());
		gov.nih.nci.cagrid.authentication.bean.Credential cred = new gov.nih.nci.cagrid.authentication.bean.Credential();
		cred.setBasicAuthenticationCredential(bac);

		gov.nih.nci.cagrid.authentication.bean.SAMLAssertion saml = null;
		try {
			// TODO: do this in a thread so it can be cancelled if it hangs
			saml = client.authenticate(cred);
		} catch (gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault ex) {
			logger.error(ex);
			FaultHelper helper = new FaultHelper(new InvalidCredentialFault());
			helper.addFaultCause(ex);
			throw helper.getFault();
		} catch (gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault ex) {
			logger.error(ex);
			FaultHelper helper = new FaultHelper(
					new InsufficientAttributeFault());
			helper.addFaultCause(ex);
			throw helper.getFault();
		} catch (Exception ex) {
			logger.error(ex);
			FaultHelper helper = new FaultHelper(new WebAuthnSvcFault());
			helper.addFaultCause(ex);
			throw helper.getFault();
		}

		String xml = saml.getXml();
		if (xml == null) {
			FaultHelper helper = new FaultHelper(new WebAuthnSvcFault());
			helper.setDescription("SAML is null");
			throw helper.getFault();
		}

		String loginKey = generateLoginKey(bac.getUserId());

		UserInfoType userInfoObj = new UserInfoType();

		// Can't use username, since it isn't unique. Have to use email address.
		String email = getEmailAddress(xml);
		userInfoObj.setUsername(email);

		populateRoles(userInfoObj);

		SAMLAssertion newSAML = new SAMLAssertion();
		newSAML.setXml(xml);
		userInfoObj.setSaml(newSAML);
		this.userInfo.put(loginKey, userInfoObj);

		return loginKey;
	}

	private void assertBasicAuthn(Credential credential) throws RemoteException {
		gov.nih.nci.cagrid.portal2.webauthn.types.authenticationservice.BasicAuthenticationCredential givenBac = credential
				.getBasicAuthenticationCredential();
		if (givenBac == null) {
			FaultHelper helper = new FaultHelper(new InvalidCredentialFault());
			helper
					.addDescription("Credentials of type BasicAuthenticationCredential are required");
			throw helper.getFault();
		}
	}

	private void populateRoles(UserInfoType userInfoObj) {
		UserInfoTypeRoles roles = new UserInfoTypeRoles();

		PortalUser userEg = new PortalUser();
		userEg.setUsername(userInfoObj.getUsername());
		PortalUser user = getPortalUserDao().getByExample(
				userEg);

		String[] roleNames = null;
		if (user == null) {
			// Create a new user
			List<Role> dRoles = getDefaultRoles();
			roleNames = new String[dRoles.size()];
			List<Role> userRoles = new ArrayList<Role>();
			for (int i = 0; i < roleNames.length; i++) {
				Role role = dRoles.get(i);
				Role r = getRoleDao().getByExample(role);
				if (r == null) {
					getRoleDao().save(role);
					userRoles.add(role);
				} else {
					userRoles.add(r);
				}
				roleNames[i] = role.getName();
			}
			userEg.setRoles(userRoles);
			getPortalUserDao().save(userEg);

		} else {
			List<Role> uRoles = user.getRoles();
			roleNames = new String[uRoles.size()];
			for (int i = 0; i < roleNames.length; i++) {
				Role role = uRoles.get(i);
				roleNames[i] = role.getName();
			}
		}
		roles.setRole(roleNames);
		userInfoObj.setRoles(roles);
	}

	private String getEmailAddress(String xml) {
		String email = null;
		try {
			String xpath = "/*[local-name()='Assertion']/*[local-name()='AttributeStatement']/*[local-name()='Attribute' and @AttributeName='urn:mace:dir:attribute-def:mail']/*[local-name()='AttributeValue']/text()";
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml
					.getBytes()));
			XPath xpathEngine = XPathFactory.newInstance().newXPath();
			email = (String) xpathEngine.evaluate(xpath, doc,
					XPathConstants.STRING);
		} catch (Exception ex) {
			throw new RuntimeException("Error getting email from SAML: "
					+ ex.getMessage(), ex);
		}
		return email;
	}

	private String generateLoginKey(String base) {
		// TODO: come up with better algorithm
		return base.hashCode() + "-" + System.currentTimeMillis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.webauthn.common.WebAuthnSvcI#getUserInfo(java.lang.String)
	 */
	public UserInfoType getUserInfo(String loginKey) throws RemoteException,
			InvalidKeyFault {

		UserInfoType userInfo = this.userInfo.remove(loginKey);
		if (userInfo == null) {
			throw new InvalidKeyFault();
		}
		return userInfo;
	}

	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

	public List<Role> getDefaultRoles() {
		return defaultRoles;
	}

	public void setDefaultRoles(List<Role> defaultRoles) {
		this.defaultRoles = defaultRoles;
	}

	public RoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

}
