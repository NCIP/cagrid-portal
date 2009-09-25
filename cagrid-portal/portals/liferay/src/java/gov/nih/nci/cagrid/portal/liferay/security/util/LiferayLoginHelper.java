package gov.nih.nci.cagrid.portal.liferay.security.util;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AutoLoginException;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import gov.nih.nci.cagrid.portal.security.IdPAuthnInfo;
import org.springframework.beans.factory.annotation.Required;

import java.util.Locale;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class LiferayLoginHelper {
    protected String omniUserEmail;
    protected String companyWebId;

    public User addLiferayUser(IdPAuthnInfo authnInfo) throws Exception {
        User user = null;

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
