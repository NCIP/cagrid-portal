/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.liferay;

import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;

import java.util.Locale;

import org.springframework.beans.factory.InitializingBean;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.User;
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

	public CaGridPortalUserLocalServiceImpl() {
		super();
	}

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

		return user;
	}

	public void deleteUser(long userId) throws PortalException, SystemException {
		User user = userPersistence.findByPrimaryKey(userId);
		PortalUser portalUser = (PortalUser) portalUserDao
				.getHibernateTemplate().find(
						"from PortalUser where portalId = '"
								+ user.getCompanyId() + ":" + user.getUserId())
				.iterator().next();
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
	}

	public CaGridPortalContext getCaGridPortalContext() {
		return caGridPortalContext;
	}

	public void setCaGridPortalContext(CaGridPortalContext caGridPortalContext) {
		this.caGridPortalContext = caGridPortalContext;
	}

}
