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
package gov.nih.nci.cagrid.portal.liferay.security;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AutoLogin;
import com.liferay.portal.security.auth.AutoLoginException;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.liferay.websso.WebSSOAutoLogin;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Locale;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class AbstractAutoLogin implements AutoLogin {
    private PortalUserDao portalUserDao;
    private PersonDao personDao;
    private String omniUserEmail;
    private String companyWebId;
    private String authnErrorMessageAttributeName;
    private String userIdAttributeName;
    private String portalAdminEmailAddress;
    protected static Log logger = LogFactory.getLog(WebSSOAutoLogin.class);

    protected User addLiferayUser(PortalUser portalUser) throws Exception {
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
        String openId = StringPool.BLANK;
        long[] organizationIds = new long[0];
        long[] groupIds = new long[0];
        long[] roleIds = new long[0];
        long[] userGroupIds = new long[0];
        boolean sendEmail = false;
        ServiceContext serviceContext = new ServiceContext();

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
                emailAddress, openId, locale, firstName, middleName, lastName,
                prefixId, suffixId, male, birthdayMonth, birthdayDay,
                birthdayYear, jobTitle, groupIds, organizationIds,
                roleIds, userGroupIds, sendEmail, serviceContext);
        
//        ResourceLocalServiceUtil.addResources(user.getCompanyId(), 0, user
//				.getUserId(), CatalogEntry.class.getName(), String
//				.valueOf(portalUser.getCatalog().getId()), false, false, false);
        
        //TODO: Set up default privileges: 

        return user;
    }


    @Required
    public String getOmniUserEmail() {
        return omniUserEmail;
    }

    public void setOmniUserEmail(String omniUserEmail) {
        this.omniUserEmail = omniUserEmail;
    }

    @Required
    public String getCompanyWebId() {
        return companyWebId;
    }

    public void setCompanyWebId(String companyWebId) {
        this.companyWebId = companyWebId;
    }

    @Required
    public String getPortalAdminEmailAddress() {
        return portalAdminEmailAddress;
    }

    public void setPortalAdminEmailAddress(String portalAdminEmailAddress) {
        this.portalAdminEmailAddress = portalAdminEmailAddress;
    }

    @Required
    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    public void setPortalUserDao(PortalUserDao portalUserDao) {
        this.portalUserDao = portalUserDao;
    }

    @Required
    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Required
    public String getAuthnErrorMessageAttributeName() {
        return authnErrorMessageAttributeName;
    }

    public void setAuthnErrorMessageAttributeName(String authnErrorMessageAttributeName) {
        this.authnErrorMessageAttributeName = authnErrorMessageAttributeName;
    }

    @Required
    public String getUserIdAttributeName() {
        return userIdAttributeName;
    }

    public void setUserIdAttributeName(String userIdAttributeName) {
        this.userIdAttributeName = userIdAttributeName;
    }
}
