/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.credmgr;

import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.IdPAuthentication;
import gov.nih.nci.cagrid.portal.domain.IdentityProvider;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.security.AuthnService;
import gov.nih.nci.cagrid.portal.security.AuthnServiceException;
import gov.nih.nci.cagrid.portal.security.AuthnTimeoutException;
import gov.nih.nci.cagrid.portal.security.EncryptionService;
import gov.nih.nci.cagrid.portal.security.IdPAuthnInfo;
import gov.nih.nci.cagrid.portal.security.ProxyUtil;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gaards.authentication.client.AuthenticationClient;
import org.cagrid.gaards.dorian.client.GridUserClient;
import org.cagrid.gaards.dorian.federation.TrustedIdentityProvider;
import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.impl.security.authorization.IdentityAuthorization;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 *
 */
@Transactional
public class CredentialManagerFacade {

	private static final Log logger = LogFactory
			.getLog(CredentialManagerFacade.class);

	private static final String BASIC_AUTH_PROFILE = "{http://gaards.cagrid.org/authentication}BasicAuthentication";

	private HibernateTemplate hibernateTemplate;
	private String ifsUrl;
	private AuthnService authnService;
	private EncryptionService encryptionService;
	private PortalUserDao portalUserDao;

	/**
	 * 
	 */
	public CredentialManagerFacade() {

	}

	public List<CredentialBean> listCredentials(String userId) {
		logger.debug("Listing credentials for user: " + userId);
		List<CredentialBean> credentials = null;
		try {
			PortalUser portalUser = getPortalUser(userId);

			credentials = new ArrayList<CredentialBean>();
			for (IdPAuthentication idpAuth : portalUser.getAuthentications()) {
				String credStr = this.getEncryptionService().decrypt(
						idpAuth.getGridCredential());
				GlobusCredential globusCred = new GlobusCredential(
						new ByteArrayInputStream(credStr.getBytes()));
				Date validUntil = new Date(System.currentTimeMillis()
						+ globusCred.getTimeLeft());
				CredentialBean credBean = new CredentialBean(idpAuth
						.getIdentity(), validUntil, new IdPBean(idpAuth
						.getIdentityProvider().getLabel(), idpAuth
						.getIdentityProvider().getUrl()));
				credBean.setDefaultCredential(idpAuth.isDefault());
				credentials.add(credBean);
			}
		} catch (Exception ex) {
			String msg = "Error listing credentials: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}
		return credentials;
	}

	private PortalUser getPortalUser(String userId) {

		try {
			String[] parts = userId.split(":");
			Long.valueOf(parts[0]);
			Long.valueOf(parts[1]);
		} catch (Exception ex) {
			throw new RuntimeException("Invalid portal ID: " + userId);
		}

		List l = getHibernateTemplate().find(
				"from PortalUser where portalId = ?", userId);
		if (l.size() != 1) {
			throw new RuntimeException("Found " + l.size()
					+ " used for portal ID: " + userId);
		}

		return (PortalUser) l.iterator().next();
	}

	public List<IdPBean> listIdPs() {
		List<IdPBean> idpBeans = new ArrayList<IdPBean>();
		try {
			List l = getHibernateTemplate().find("from IdentityProvider");
			for (Iterator<IdentityProvider> i = l.iterator(); i.hasNext();) {
				IdentityProvider idp = i.next();
				idpBeans.add(new IdPBean(idp.getLabel(), idp.getUrl()));
			}
		} catch (Exception ex) {
			String msg = "Error listing identity providers: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}
		return idpBeans;
	}

	public List<IdPBean> listIdPsFromDorian() {

		List<IdPBean> idpBeans = new ArrayList<IdPBean>();

		try {
			GridUserClient guc = new GridUserClient(getIfsUrl());
			List<TrustedIdentityProvider> idps = guc
					.getTrustedIdentityProviders();
			for (TrustedIdentityProvider idp : idps) {
				String idpUrl = idp.getAuthenticationServiceURL();

				try {
					AuthenticationClient ac = new AuthenticationClient(idpUrl);
					String idpIdent = idp.getAuthenticationServiceIdentity();
					if (idpIdent != null && idpIdent.trim().length() > 0) {
						ac
								.setAuthorization(new IdentityAuthorization(
										idpIdent));
					}
					try {
						for (QName profileQName : ac
								.getSupportedAuthenticationProfiles()) {
							if (profileQName.toString().trim().equals(BASIC_AUTH_PROFILE)) {
								IdPBean bean = new IdPBean(
										idp.getDisplayName(), idpUrl);
								idpBeans.add(bean);
                                if(getIdentityProvider(idpUrl)==null)
                                    addIdP(idp.getDisplayName(), idpUrl);
							}
						}
					} catch (Exception ex) {
						String msg = "Couldn't get profiles for: " + idpUrl
								+ "\n" + ex.getMessage();
						logger.warn(msg, ex);
					}
				} catch (Exception ex) {
					String msg = "Error connecting to " + idpUrl + ": "
							+ ex.getMessage();
					logger.warn(msg, ex);
				}
			}
		} catch (Exception ex) {
			logger.error("Error getting identity provider list: "
					+ ex.getMessage(), ex);
			throw new RuntimeException("Error getting identity provider list.",
					ex);
		}

		return idpBeans;
	}

	public String addIdP(String label, String url) {
		String message = "Successfully added identity provider.";
		IdentityProvider idp = getIdentityProvider(url);
		if (idp != null) {
			throw new RuntimeException(
					"An identity provider with that URL already exists.");
		}

		try {
			idp = new IdentityProvider();
			idp.setLabel(label);
			idp.setUrl(url);
			getHibernateTemplate().save(idp);
		} catch (Exception ex) {
			String msg = "Error adding identity provider: " + ex.getMessage();
			logger.error(message, ex);
			throw new RuntimeException(msg, ex);
		}
		return message;
	}

	private IdentityProvider getIdentityProvider(String url) {
		IdentityProvider idp = null;
		try {
			new URL(url);
		} catch (Exception ex) {
			throw new RuntimeException("Invalid URL: " + url);
		}

		List l = getHibernateTemplate().find(
				"from IdentityProvider where url = ?", url);
		if (l.size() > 1) {
			throw new RuntimeException("Found " + l.size()
					+ " identity provided for: " + url);
		}
		if (l.size() == 1) {
			idp = (IdentityProvider) l.iterator().next();
		}

		return idp;

	}

	public String deleteIdP(String url) {
		String message = "Successfully deleted identity provider.";
		IdentityProvider idp = getIdentityProvider(url);
		if (idp == null) {
			throw new RuntimeException(
					"No identity provider with that URL found.");
		} else {
			try {
				getHibernateTemplate().delete(idp);
			} catch (Exception ex) {
				String msg = "Error deleting identity provider: "
						+ ex.getMessage();
				logger.error(msg, ex);
				throw new RuntimeException(msg, ex);
			}
		}
		return message;
	}

	public CredentialBean refresh(String userId, String identity) {

		CredentialBean credBean = null;

		logger.debug("Refreshing credential '" + identity + "' for user: "
				+ userId);
		IdPAuthentication idpAuthn = null;
		try {
			PortalUser user = getPortalUser(userId);

			for (IdPAuthentication ia : user.getAuthentications()) {
				if (ia.getIdentity().equals(identity)) {
					idpAuthn = ia;
					break;
				}
			}
			if (idpAuthn == null) {
				throw new RuntimeException(
						"No authentication found for identity: " + identity);
			}
			String username = idpAuthn.getUsername();
			String password = this.getEncryptionService().decrypt(
					idpAuthn.getPassword());
			credBean = authenticate(userId, username, password, idpAuthn
					.getIdentityProvider().getUrl(), 4, 0);

		} catch (RuntimeException ex) {
			String msg = "Error refreshing credential: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}

		return credBean;
	}

	public CredentialBean authenticate(String userId, String username,
			String password, String url, int proxyLifetimeHours,
			int delegationPathLength) {
		CredentialBean credBean = null;
		try {
			if (userId == null) {
				throw new RuntimeException(
						"User is not authenticated to the portal.");
			} else {

				PortalUser user = getPortalUser(userId);

				GlobusCredential globusCred = null;
				try {
					// globusCred = getAuthenticationHelper().authenticate(
					// username, password, url, getIfsUrl(),
					// proxyLifetimeHours, delegationPathLength);

					IdPAuthnInfo authnInfo = getAuthnService()
							.authenticateToIdP(username, password, url);
					globusCred = getAuthnService().authenticateToIFS(
							getIfsUrl(), authnInfo.getSaml());

				} catch (InvalidCredentialFault e) {
					throw new RuntimeException("Invalid credentials: "
							+ e.getMessage());
				} catch (AuthnServiceException e) {
					throw new RuntimeException("Error authenticating: "
							+ e.getMessage());
				} catch (AuthnTimeoutException e) {
					throw new RuntimeException(
							"The identity provider did not respond.");
				}

				IdentityProvider idp = getIdentityProvider(url);

				String identity = globusCred.getIdentity();

				// See if the user already has an authentication for
				// this identity.
				IdPAuthentication idpAuthn = null;
				for (IdPAuthentication ia : user.getAuthentications()) {
					if (ia.getIdentity().equals(identity)) {
						idpAuthn = ia;
						ia.setDefault(true);
					} else {
						ia.setDefault(false);
					}
					getHibernateTemplate().update(ia);
				}
				if (idpAuthn == null) {
					idpAuthn = new IdPAuthentication();
					idpAuthn.setDefault(true);
					idpAuthn.setIdentityProvider(idp);
					idpAuthn.setPortalUser(user);
					idpAuthn.setIdentity(identity);
					getHibernateTemplate().save(idpAuthn);
				}
				try {
					idpAuthn.setUsername(username);
					idpAuthn.setPassword(getEncryptionService().encrypt(
							password));
					idpAuthn.setGridCredential(getEncryptionService().encrypt(
							ProxyUtil.getProxyString(globusCred)));
					getHibernateTemplate().update(idpAuthn);

				} catch (Exception ex) {
					throw new RuntimeException("Error saving credentials: "
							+ ex.getMessage(), ex);
				}
				user.setGridCredential(idpAuthn.getGridCredential());
				Date validUntil = new Date(System.currentTimeMillis()
						+ globusCred.getTimeLeft());
				IdPBean idpBean = new IdPBean(idpAuthn.getIdentityProvider()
						.getLabel(), idpAuthn.getIdentityProvider().getUrl());
				credBean = new CredentialBean(globusCred.getIdentity(),
						validUntil, idpBean);
				credBean.setDefaultCredential(true);
			}
		} catch (RuntimeException ex) {
			String msg = "Error logging in: " + ex.getMessage();
			logger.error(msg, ex);
			throw ex;
		}
		return credBean;
	}

	// TODO: remove this method - replaced by authenticate
	public String login(String userId, String username, String password,
			String url, int proxyLifetimeHours, int delegationPathLength) {
		logger.debug("Logging in user: userId=" + userId + ", username='"
				+ username + "', url='" + url + "'.");
		logger.debug("IFS URL: " + getIfsUrl());

		String message = "Successfully authenticated to " + url;
		authenticate(userId, username, password, url, proxyLifetimeHours,
				delegationPathLength);
		return message;
	}

	//TODO: replace with call to UserService
	public String setDefaultCredential(String userId, String identity) {
		String message = "Successfully set " + identity + " to default.";
		logger.debug("Setting default credential for user: " + userId);
		IdPAuthentication idpAuthn = null;
		try {
			PortalUser user = getPortalUser(userId);

			for (IdPAuthentication ia : user.getAuthentications()) {
				if (ia.getIdentity().equals(identity)) {
					idpAuthn = ia;
					ia.setDefault(true);
				} else {
					ia.setDefault(false);
				}
				getHibernateTemplate().update(ia);
			}
			if (idpAuthn == null) {
				throw new RuntimeException(
						"No authentication found for identity: " + identity);
			}
			user.setGridCredential(idpAuthn.getGridCredential());

		} catch (RuntimeException ex) {
			String msg = "Error setting default credential: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}

		return message;
	}

	public String deleteCredential(String userId, String identity) {
		String message = "Successfully deleted " + identity;

		logger.debug("Deleting credential for user: " + userId);
		try {
			PortalUser user = getPortalUser(userId);
			List<IdPAuthentication> ias = user.getAuthentications();
			for (Iterator<IdPAuthentication> i = ias.iterator(); i.hasNext();) {
				IdPAuthentication ia = i.next();
				if (ia.getIdentity().equals(identity)) {
					i.remove();
					getHibernateTemplate().delete(ia);
					break;
				}
			}
			user.setAuthentications(ias);
			getHibernateTemplate().update(user);
		} catch (RuntimeException ex) {
			String msg = "Error deleting credential: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}
		return message;
	}
	
	public String getGridIdentity(String portalUserId){
		PortalUser portalUser = getPortalUserDao().getByPortalId(portalUserId);
		if(portalUser == null){
			throw new RuntimeException("No portal user found for " + portalUserId);
		}
		return portalUser.getGridIdentity();
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public String getIfsUrl() {
		return ifsUrl;
	}

	public void setIfsUrl(String ifsUrl) {
		this.ifsUrl = ifsUrl;
	}

	public EncryptionService getEncryptionService() {
		return encryptionService;
	}

	public void setEncryptionService(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}

	public AuthnService getAuthnService() {
		return authnService;
	}

	public void setAuthnService(AuthnService authnService) {
		this.authnService = authnService;
	}

	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

}
