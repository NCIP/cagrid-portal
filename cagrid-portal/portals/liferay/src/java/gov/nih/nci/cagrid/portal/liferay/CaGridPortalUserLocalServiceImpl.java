/**
 *
 */
package gov.nih.nci.cagrid.portal.liferay;

import com.liferay.portal.*;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.PasswordPolicy;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.pwd.PwdToolkitUtil;
import com.liferay.portal.service.PasswordPolicyLocalServiceUtil;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.impl.UserLocalServiceImpl;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsKeys;
import com.liferay.portal.util.PropsValues;
import gov.nih.nci.cagrid.portal.dao.IdentityProviderDao;
import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.PersonCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;
import gov.nih.nci.cagrid.portal.security.AuthnService;
import gov.nih.nci.cagrid.portal.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Locale;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 */

public class CaGridPortalUserLocalServiceImpl extends UserLocalServiceImpl
        implements InitializingBean {

    private static final Log logger = LogFactory
            .getLog(CaGridPortalUserLocalServiceImpl.class);

    private CaGridPortalContext caGridPortalContext;
    private PersonDao personDao;
    private PortalUserDao portalUserDao;
    private PersonCatalogEntryDao personCatalogEntryDao;
    private AuthnService authnService;
    private UserService userService;
    private IdentityProviderDao identityProviderDao;
    private String ifsUrl;
    private String companyWebId;
    private String omniUserEmail;

    public CaGridPortalUserLocalServiceImpl() {
        super();
    }


    // TODO: need update operation to keep in sync with liferay user management
    // changes.
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

        // TODO: add user to appropriate groups (e.g. to enable creation of
        // catalog entries)

        return user;
    }

    public void deleteUser(long userId) throws PortalException, SystemException {
        User user = userPersistence.findByPrimaryKey(userId);
        String portalUserId = user.getCompanyId() + ":" + user.getUserId();
        PortalUser portalUser = getPortalUserDao().getByPortalId(portalUserId);
        ResourceLocalServiceUtil.deleteResource(user.getCompanyId(),
                CatalogEntry.class.getName(),
                ResourceConstants.SCOPE_INDIVIDUAL, portalUser.getCatalog()
                        .getId());
        super.deleteUser(userId);
        portalUserDao.delete(portalUser);
    }

    @Override
    protected void validate(long companyId, long userId, boolean autoPassword,
                            String password1, String password2, boolean autoScreenName,
                            String screenName, String emailAddress, String firstName,
                            String lastName, long[] organizationIds) throws PortalException,
            SystemException {

        logger
                .debug("Using  CaGridUserLocalServiceImpl  for validation of user");

        if (!autoScreenName) {
            validateScreenName(companyId, userId, screenName);
        }

        if (!autoPassword) {
            PasswordPolicy passwordPolicy = PasswordPolicyLocalServiceUtil
                    .getDefaultPasswordPolicy(companyId);

            PwdToolkitUtil.validate(companyId, 0, password1, password2,
                    passwordPolicy);
        }

        if (!Validator.isEmailAddress(emailAddress)) {
            throw new UserEmailAddressException();
        } else {
            // As per caGrid requirements portal can have duplicate email
            // address for a particular company Id.
            /*
                * try { User user = UserUtil.findByC_EA(companyId, emailAddress);
                *
                * if (user != null) { throw new
                * DuplicateUserEmailAddressException(); } } catch
                * (NoSuchUserException nsue) { }
                */

            String[] reservedEmailAddresses = PrefsPropsUtil.getStringArray(
                    companyId, PropsKeys.ADMIN_RESERVED_EMAIL_ADDRESSES,
                    StringPool.NEW_LINE,
                    PropsValues.ADMIN_RESERVED_EMAIL_ADDRESSES);

            for (int i = 0; i < reservedEmailAddresses.length; i++) {
                if (emailAddress.equalsIgnoreCase(reservedEmailAddresses[i])) {
                    throw new ReservedUserEmailAddressException();
                }
            }
        }

        if (Validator.isNull(firstName)) {
            throw new ContactFirstNameException();
        } else if (Validator.isNull(lastName)) {
            throw new ContactLastNameException();
        }
    }

    @Override
    protected void validate(long l, String s, String s1, String s2, String s3,
                            String s4) throws PortalException, SystemException {
        super.validate(l, s, s1, s2, s3, s4); // To change body of overridden
        // methods use File | Settings |
        // File Templates.
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
        setAuthnService((AuthnService) getCaGridPortalContext()
                .getApplicationContext().getBean("authnService"));
        setIdentityProviderDao((IdentityProviderDao) getCaGridPortalContext()
                .getApplicationContext().getBean("identityProviderDao"));

        // Conflicts with Liferay
        // setUserService((UserService) getCaGridPortalContext()
        // .getApplicationContext().getBean("userService"));

        this.userService = (UserService) getCaGridPortalContext()
                .getApplicationContext().getBean("userService");
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

    public AuthnService getAuthnService() {
        return authnService;
    }

    public void setAuthnService(AuthnService authnService) {
        this.authnService = authnService;
    }

    public String getIfsUrl() {
        return ifsUrl;
    }

    public void setIfsUrl(String ifsUrl) {
        this.ifsUrl = ifsUrl;
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

    /*
      * This conflicts with Liferay methods public UserService getUserService() {
      * return userService; }
      *
      * public void setUserService(UserService userService) { this.userService =
      * userService; }
      */

    public IdentityProviderDao getIdentityProviderDao() {
        return identityProviderDao;
    }

    public void setIdentityProviderDao(IdentityProviderDao identityProviderDao) {
        this.identityProviderDao = identityProviderDao;
    }

}
