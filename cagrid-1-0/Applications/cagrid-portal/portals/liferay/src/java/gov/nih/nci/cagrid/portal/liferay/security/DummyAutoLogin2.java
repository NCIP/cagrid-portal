/**
 * 
 */
package gov.nih.nci.cagrid.portal.liferay.security;

import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gsi.GlobusCredential;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AutoLogin;
import com.liferay.portal.security.auth.AutoLoginException;
import com.liferay.portal.service.CompanyLocalService;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DummyAutoLogin2 implements AutoLogin {

	private static final Log logger = LogFactory.getLog(DummyAutoLogin2.class);

	private PortalUserDao portalUserDao;

	private PersonDao personDao;

	private String proxyPath;

	private String companyWebId;

	private String omniUserEmail;

	/**
	 * 
	 */
	public DummyAutoLogin2() {
		try {
			ApplicationContext ctx = new ClassPathXmlApplicationContext(
					new String[] { "classpath:applicationContext-db.xml",
							"classpath:applicationContext-liferay.xml" });
			setProxyPath((String) ctx.getBean("proxyPath"));
			setCompanyWebId((String) ctx.getBean("companyWebId"));
			setOmniUserEmail((String) ctx.getBean("omniUserEmail"));
			setPortalUserDao((PortalUserDao) ctx.getBean("portalUserDao"));
			setPersonDao((PersonDao) ctx.getBean("personDao"));
		} catch (Exception ex) {
			throw new RuntimeException("Error loading application context: "
					+ ex.getMessage(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.liferay.portal.security.auth.AutoLogin#login(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public String[] login(HttpServletRequest request,
			HttpServletResponse response) throws AutoLoginException {

		try {
			String[] credentials = null;
			if ("true".equals(request.getParameter("doAutoLogin"))) {
				logger.debug("Doing auto login.");

				User user = null;
				PortalUser portalUser = getPortalUser();
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
							.parseInt(portalId[0]), Integer
							.parseInt(portalId[1]));
					if (user == null) {
						throw new AutoLoginException(
								"No User found for portalId = " + portalId);
					}
				}

				String userIdAttName = "CAGRIDPORTAL_ATTS_userId";
				String proxyAttName = "CAGRIDPORTAL_ATTS_gridCredential";
				request.setAttribute(userIdAttName, portalUser.getId());
				request.setAttribute(proxyAttName, portalUser
						.getGridCredential());

				credentials = new String[3];
				credentials[0] = String.valueOf(user.getUserId());
				credentials[1] = user.getPassword();
				credentials[2] = Boolean.TRUE.toString();
			} else {
				logger.debug("Not doing auto login.");
			}

			return credentials;
		} catch (Exception ex) {
			logger.error("Error doing auto login: " + ex.getMessage(), ex);
			throw new AutoLoginException(ex);
		}
	}

	private User createNewUser(PortalUser portalUser) throws Exception {
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
	    long[] organizationId = new long[0];
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
				birthdayYear, jobTitle, organizationId,sendEmail);

		return user;
	}

	private PortalUser getPortalUser() {

		PortalUser portalUser = new PortalUser();

		InputStream proxyIn = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(getProxyPath());
		GlobusCredential proxy = null;
		try {
			proxy = new GlobusCredential(proxyIn);
		} catch (Exception ex) {
			throw new RuntimeException("Error reading proxy: "
					+ ex.getMessage(), ex);
		}

		String gridIdentity = proxy.getIdentity();
		portalUser.setGridIdentity(gridIdentity);
		portalUser = getPortalUserDao().getByExample(portalUser);
		if (portalUser == null) {
			logger.debug("Creating PortalUser for identity = " + gridIdentity);
			String userName = gridIdentity.substring(gridIdentity
					.lastIndexOf("=") + 1);

			portalUser = new PortalUser();
			portalUser.setGridIdentity(gridIdentity);

			Person person = new Person();
			person.setEmailAddress(userName + "@somewhere.com");
			person.setFirstName(userName);
			person.setLastName(userName);
			getPersonDao().save(person);

			portalUser.setPerson(person);
			getPortalUserDao().save(portalUser);
		}

		String proxyStr = null;
		try {
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			proxy.save(buf);
			proxyStr = buf.toString();
		} catch (Exception ex) {
			throw new RuntimeException("Error writing proxy to string: "
					+ ex.getMessage(), ex);
		}

		portalUser.setGridCredential(proxyStr);

		return portalUser;
	}

	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

	public String getProxyPath() {
		return proxyPath;
	}

	public void setProxyPath(String proxyPath) {
		this.proxyPath = proxyPath;
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

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

}
