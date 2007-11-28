/**
 * 
 */
package gov.nih.nci.cagrid.portal.liferay.security;

import java.util.Date;
import java.util.Locale;

import gov.nih.nci.cagrid.portal.dao.AuthnTicketDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.AuthnTicket;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.security.EncryptionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AutoLogin;
import com.liferay.portal.security.auth.AutoLoginException;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DirectAutoLogin implements AutoLogin {

	private static final Log logger = LogFactory.getLog(DirectAutoLogin.class);

	private EncryptionService encryptionService;
	private String ticketParameterName;
	private PortalUserDao portalUserDao;
	private AuthnTicketDao authnTicketDao;
	private String userIdAttributeName;
	private String proxyAttributeName;
	private String companyWebId;
	private String omniUserEmail;

	/**
	 * 
	 */
	public DirectAutoLogin() {

	}

	public String[] login(HttpServletRequest request, HttpServletResponse response)
			throws AutoLoginException {

		String[] credentials = null;
		String ticket = request.getParameter(getTicketParameterName());
		if (ticket == null) {
			return null;
		}
		try {
			ticket = getEncryptionService().decrypt(ticket);
			AuthnTicket authnTicket = new AuthnTicket();
			authnTicket.setTicket(ticket);
			authnTicket = getAuthnTicketDao().getByExample(authnTicket);
			if (authnTicket == null) {
				logger.warn("Didn't find AuthnTicket for ticket '" + ticket
						+ "'");
				return null;
			}
			if(new Date().after(authnTicket.getNotAfter())){
				logger.warn("Authentication ticket is expired.");
				return null;
			}

			PortalUser portalUser = authnTicket.getPortalUser();
			User user = null;
			if (portalUser.getPortalId() == null) {
				logger.debug("creating new User for "
						+ portalUser.getGridIdentity());

				user = createNewUser(portalUser);
				portalUser.setPortalId(String.valueOf(user.getCompanyId())
						+ ":" + String.valueOf(user.getUserId()));
				getPortalUserDao().save(portalUser);
			} else {
				String[] portalId = portalUser.getPortalId().split(":");
				user = UserLocalServiceUtil.getUserById(Integer
						.parseInt(portalId[0]), Integer.parseInt(portalId[1]));
				if (user == null) {
					throw new AutoLoginException(
							"No User found for portalId = " + portalId);
				}
			}
			getAuthnTicketDao().delete(authnTicket);

			request.setAttribute(getUserIdAttributeName(), portalUser.getId());
			request.setAttribute(getProxyAttributeName(), portalUser
					.getGridCredential());

			credentials = new String[3];
			credentials[0] = String.valueOf(user.getUserId());
			credentials[1] = user.getPassword();
			credentials[2] = Boolean.TRUE.toString();

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AutoLoginException(
					"Error logging in: " + ex.getMessage(), ex);
		}
		return credentials;
	}

	protected User createNewUser(PortalUser portalUser) throws Exception {
		User user = null;

		long creatorUserId = -1;
		long companyId = -1;
		boolean autoPassword = true;
		String password1 = "";
		String password2 = "";
		boolean autoScreenName = true;
		String screenName = "";
		String emailAddress = portalUser.getPerson().getEmailAddress();
		Locale locale = Locale.US;
		String firstName = portalUser.getPerson().getFirstName();
		String middleName = null;
		String lastName = portalUser.getPerson().getLastName();
		int prefixId = -1;
		int suffixId = -1;
		boolean male = true;
		int birthdayMonth = 1;
		int birthdayDay = 1;
		int birthdayYear = 1;
		String jobTitle = null;
		long organizationId = -1;
		long locationId = -1;
		boolean sendEmail = false;

		Company company = CompanyLocalServiceUtil
				.getCompanyByWebId(getCompanyWebId());
		if (company == null) {
			throw new AutoLoginException("No Company found for webid: "
					+ getCompanyWebId());
		}
		companyId = company.getCompanyId();
		User omniuser = UserLocalServiceUtil.getUserByEmailAddress(companyId,
				getOmniUserEmail());
		if (omniuser == null) {
			throw new AutoLoginException("No omniuser found for email: "
					+ getOmniUserEmail());
		}
		creatorUserId = omniuser.getUserId();
		user = UserLocalServiceUtil.addUser(creatorUserId, companyId,
				autoPassword, password1, password2, autoScreenName, screenName,
				emailAddress, locale, firstName, middleName, lastName,
				prefixId, suffixId, male, birthdayMonth, birthdayDay,
				birthdayYear, jobTitle, organizationId, locationId, sendEmail);

		return user;
	}

	public EncryptionService getEncryptionService() {
		return encryptionService;
	}

	public void setEncryptionService(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}

	public String getTicketParameterName() {
		return ticketParameterName;
	}

	public void setTicketParameterName(String ticketParameterName) {
		this.ticketParameterName = ticketParameterName;
	}

	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

	public AuthnTicketDao getAuthnTicketDao() {
		return authnTicketDao;
	}

	public void setAuthnTicketDao(AuthnTicketDao authnTicketDao) {
		this.authnTicketDao = authnTicketDao;
	}

	public String getUserIdAttributeName() {
		return userIdAttributeName;
	}

	public void setUserIdAttributeName(String userIdAttributeName) {
		this.userIdAttributeName = userIdAttributeName;
	}

	public String getProxyAttributeName() {
		return proxyAttributeName;
	}

	public void setProxyAttributeName(String proxyAttributeName) {
		this.proxyAttributeName = proxyAttributeName;
	}

	public String getCompanyWebId() {
		return companyWebId;
	}

	public void setCompanyWebId(String companyWebId) {
		this.companyWebId = companyWebId;
	}

	public String getOmniUserEmail() {
		return omniUserEmail;
	}

	public void setOmniUserEmail(String omniUserEmail) {
		this.omniUserEmail = omniUserEmail;
	}

}
