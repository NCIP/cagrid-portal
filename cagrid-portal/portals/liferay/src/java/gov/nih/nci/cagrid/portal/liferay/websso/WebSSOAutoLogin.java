package gov.nih.nci.cagrid.portal.liferay.websso;


import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AutoLoginException;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsKeys;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.liferay.security.AbstractAutoLogin;
import gov.nih.nci.cagrid.portal.liferay.security.PortalUserLoader;
import org.acegisecurity.context.SecurityContextHolder;
import org.cagrid.websso.client.acegi.WebSSOUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * <p/>
 * Liferay portal provides a seperate hooking mechanism to fetch valid credentials from
 * any third party security system.Inside this hook method ,we have to populate current user
 * info into liferay if that's user information doesn't exist before.
 * <p/>
 * <p/>
 * Modified com.cagrid.liferay.websso.client.CaGridLiferayCASAutoLoginHook
 * for Liferay 5.x
 */
public class WebSSOAutoLogin extends AbstractAutoLogin {

    private PortalUserLoader portalUserLoader;

    public String[] login(HttpServletRequest request, HttpServletResponse response)
            throws AutoLoginException {

        String[] credentials = null;
        long companyId = PortalUtil.getCompanyId(request);
        try {

            if (!PrefsPropsUtil.getBoolean(companyId, PropsKeys.CAS_AUTH_ENABLED)) {
                return credentials;
            }
            if (SecurityContextHolder
                    .getContext().getAuthentication() != null) {
                WebSSOUser webssoUser = (WebSSOUser) SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal();

                logger.debug("User logging in with webSSO credentials");

                User user = null;
                // lets check if user already exists
                PortalUser portalUser = portalUserLoader.getPortalUser(webssoUser);
                if (portalUser == null) {
                    logger.debug("No Portal user found. Will create a new Portal user");

                    // create user through Liferay user service (extended version)
                    user = addLiferayUser(webssoUser);

                    // lets load the newly created user
                    portalUser = portalUserLoader.getPortalUser(user);
                    logger.debug("Sucesscfully Loaded Portal user for Liferay user");

                    portalUser.setDelegatedEPR(webssoUser.getDelegatedEPR());
                    portalUser.setGridIdentity(webssoUser.getGridId());
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

                request.setAttribute(getUserIdAttributeName(), portalUser
                        .getId());

                credentials = new String[3];
                credentials[0] = String.valueOf(user.getUserId());
                credentials[1] = user.getPassword();
                credentials[2] = Boolean.TRUE.toString();
            }
        } catch (Exception e) {
            logger.error("Unexpected error while authenticating: "
                    + e.getMessage(), e);
            request
                    .setAttribute(
                            getAuthnErrorMessageAttributeName(),
                            "An error was encountered during authentication. Please contact the adminstrator (" + getPortalAdminEmailAddress() + ").");
            throw new AutoLoginException(e);
        }

        return credentials;

    }

    protected User addLiferayUser(WebSSOUser webSSOUser) throws Exception {
        User user = null;

        long creatorUserId = -1;
        long companyId = -1;
        boolean autoPassword = true;
        String password1 = "";
        String password2 = "";
        boolean autoScreenName = true;
        String screenName = "";
        String emailAddress = webSSOUser.getEmailId();
        Locale locale = Locale.US;
        String firstName = webSSOUser.getFirstName();
        String middleName = null;
        String lastName = webSSOUser.getLastName();
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

    public PortalUserLoader getPortalUserLoader() {
        return portalUserLoader;
    }

    public void setPortalUserLoader(PortalUserLoader portalUserLoader) {
        this.portalUserLoader = portalUserLoader;
    }
}