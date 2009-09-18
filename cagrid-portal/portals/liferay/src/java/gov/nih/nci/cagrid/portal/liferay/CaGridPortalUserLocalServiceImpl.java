/**
 * 
 */
package gov.nih.nci.cagrid.portal.liferay;

import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.PersonCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;

import java.util.Locale;

import org.springframework.beans.factory.InitializingBean;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.impl.UserLocalServiceImpl;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class CaGridPortalUserLocalServiceImpl extends UserLocalServiceImpl
		implements InitializingBean {

	private CaGridPortalContext caGridPortalContext;
	private PersonDao personDao;
	private PortalUserDao portalUserDao;
	private PersonCatalogEntryDao personCatalogEntryDao;

	public CaGridPortalUserLocalServiceImpl() {
		super();
	}

	//TODO: need update operation to keep in sync with liferay user management changes.
	
	public User addUser(long creatorUserId, long companyId,
			boolean autoPassword, String password1, String password2,
			boolean autoScreenName, String screenName, String emailAddress,
			String openId, Locale locale, String firstName, String middleName,
			String lastName, int prefixId, int suffixId, boolean male,
			int birthdayMonth, int birthdayDay, int birthdayYear,
			String jobTitle, long[] groupIds, long[] organizationIds,
			long[] roleIds, long[] userGroupIds, boolean sendEmail,
			ServiceContext serviceContext) throws PortalException,
			SystemException {
		User user = super.addUser(creatorUserId, companyId, autoPassword,
				password1, password2, autoScreenName, screenName, emailAddress,
				openId, locale, firstName, middleName, lastName, prefixId,
				suffixId, male, birthdayMonth, birthdayDay, birthdayYear,
				jobTitle, groupIds, organizationIds, roleIds, userGroupIds,
				sendEmail, serviceContext);

		Person person = new Person();
		person.setFirstName(user.getFirstName());
		person.setLastName(user.getLastName());
		person.setEmailAddress(user.getEmailAddress());
		getPersonDao().save(person);

		PortalUser portalUser = new PortalUser();
		portalUser.setPortalId(user.getCompanyId() + ":" + user.getUserId());
		portalUser.setPerson(person);
		getPortalUserDao().save(portalUser);

		PersonCatalogEntry personCatalogEntry = getPersonCatalogEntryDao()
				.createCatalogAbout(portalUser);
		personCatalogEntry.setName(person.getFirstName() + " "
				+ person.getLastName());
		personCatalogEntry.setAuthor(portalUser);
		personCatalogEntry.setPublished(false);
		getPersonCatalogEntryDao().save(personCatalogEntry);

		portalUser.setCatalog(personCatalogEntry);
		getPortalUserDao().save(portalUser);


		ResourceLocalServiceUtil.addResources(user.getCompanyId(), 0, user
				.getUserId(), CatalogEntry.class.getName(), String
				.valueOf(personCatalogEntry.getId()), false, false, false);
		
		//TODO: add user to appropriate groups (e.g. to enable creation of catalog entries)

		return user;
	}

	public void deleteUser(long userId) throws PortalException, SystemException {
		User user = userPersistence.findByPrimaryKey(userId);
		String portalUserId = user.getCompanyId() + ":" + user.getUserId();
		PortalUser portalUser = getPortalUserDao().getByPortalId(portalUserId);
		ResourceLocalServiceUtil.deleteResource(user.getCompanyId(),
				CatalogEntry.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL,
				portalUser.getCatalog().getId());
		super.deleteUser(userId);
		portalUserDao.delete(portalUser);
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

	public void afterPropertiesSet() throws Exception {
		setPortalUserDao((PortalUserDao) getCaGridPortalContext()
				.getApplicationContext().getBean("portalUserDao"));
		setPersonDao((PersonDao) getCaGridPortalContext()
				.getApplicationContext().getBean("personDao"));
		setPersonCatalogEntryDao((PersonCatalogEntryDao) getCaGridPortalContext()
				.getApplicationContext().getBean("personCatalogEntryDao"));
	}

	public CaGridPortalContext getCaGridPortalContext() {
		return caGridPortalContext;
	}

	public void setCaGridPortalContext(CaGridPortalContext caGridPortalContext) {
		this.caGridPortalContext = caGridPortalContext;
	}

	public PersonCatalogEntryDao getPersonCatalogEntryDao() {
		return personCatalogEntryDao;
	}

	public void setPersonCatalogEntryDao(
			PersonCatalogEntryDao personCatalogEntryDao) {
		this.personCatalogEntryDao = personCatalogEntryDao;
	}

}
