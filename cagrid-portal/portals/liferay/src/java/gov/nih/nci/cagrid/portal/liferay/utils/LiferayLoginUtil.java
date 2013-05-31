/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.liferay.utils;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AutoLoginException;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Locale;

import gov.nih.nci.cagrid.portal.authn.domain.IdPAuthnInfo;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class LiferayLoginUtil {
    protected String omniUserEmail;
    protected String companyWebId;

    private Log log = LogFactory.getLog(getClass());

    public void masqueradeOmniUser() throws Exception {
        // masquerade admin user
        Company company = getCompany();
        User omniUser = getOnmiUserId();
        PrincipalThreadLocal.setName(omniUser.getUserId());
        PermissionChecker permissionChecker =
                PermissionCheckerFactoryUtil.create(omniUser, true);
        PermissionThreadLocal.setPermissionChecker(permissionChecker);

        CompanyThreadLocal.setCompanyId(company.getPrimaryKey());
    }

    public Company getCompany() throws PortalException, SystemException {
        Company company = CompanyLocalServiceUtil
                .getCompanyByWebId(getCompanyWebId());
        if (company == null) {
            throw new PortalException("No Company found for webid: "
                    + getCompanyWebId());
        }
        return company;
    }

    public User getOnmiUserId() throws PortalException, SystemException {
        User omniuser = UserLocalServiceUtil.getUserByEmailAddress(getCompany().getCompanyId(),
                getOmniUserEmail());
        if (omniuser == null) {
            throw new PortalException("No omniuser found for email: "
                    + getOmniUserEmail());
        }
        return omniuser;
    }

    public User addLiferayUser(IdPAuthnInfo authnInfo) throws Exception {
        User user = null;

        log.debug("Helper will create user in Liferay");

        long creatorUserId = -1;
        long companyId = -1;
        boolean autoPassword = true;
        String password1 = "";
        String password2 = "";
        boolean autoScreenName = true;
        String screenName = "";
        String emailAddress = authnInfo.getEmail();
        Locale locale = Locale.US;
        String firstName = authnInfo.getFirstName();
        String middleName = null;
        String lastName = authnInfo.getLastName();
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
        creatorUserId = getOnmiUserId().getUserId();

        user = UserLocalServiceUtil.addUser(creatorUserId, companyId,
                autoPassword, password1, password2, autoScreenName, screenName,
                emailAddress, openId, locale, firstName, middleName, lastName,
                prefixId, suffixId, male, birthdayMonth, birthdayDay,
                birthdayYear, jobTitle, groupIds, organizationIds,
                roleIds, userGroupIds, sendEmail, serviceContext);

        log.debug("Sucessfully created user in Liferay");

        return user;
    }

    @Required
    public String getOmniUserEmail() {
        return omniUserEmail;
    }

    @Required
    public String getCompanyWebId() {
        return companyWebId;
    }

    public void setOmniUserEmail(String omniUserEmail) {
        this.omniUserEmail = omniUserEmail;
    }

    public void setCompanyWebId(String companyWebId) {
        this.companyWebId = companyWebId;
    }
}
