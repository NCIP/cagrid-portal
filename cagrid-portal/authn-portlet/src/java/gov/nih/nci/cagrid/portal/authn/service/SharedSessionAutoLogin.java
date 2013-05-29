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
package gov.nih.nci.cagrid.portal.authn.service;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserIdMapper;
import com.liferay.portal.security.auth.AutoLogin;
import com.liferay.portal.security.auth.AutoLoginException;
import com.liferay.portal.service.*;
import gov.nih.nci.cagrid.portal.authn.domain.IdPAuthnInfo;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SharedSessionAutoLogin implements AutoLogin {

    private String sharedSessionAuthnAttribute;
    private String gridUserType;
    private String portalSessionAuthnAttribute;

    private String omniUserEmail;
    private String companyWebId;

    private Log logger = LogFactory.getLog(getClass().getSimpleName());

    /**
     * Logs in users based on IdPAuthnInfo object in session.
     *
     * @param request
     * @param response
     * @return
     * @throws com.liferay.portal.security.auth.AutoLoginException
     *
     */
    public String[] login(HttpServletRequest request, HttpServletResponse response) throws AutoLoginException {

        String[] credentials = null;

        HttpSession session = request.getSession();
        IdPAuthnInfo idpAuthnInfo = (IdPAuthnInfo) session.getAttribute(getSharedSessionAuthnAttribute());

        if (idpAuthnInfo != null) {
            logger.debug("Found idp authentication in session");

            User user = null;
            UserIdMapperLocalService service = UserIdMapperLocalServiceUtil.getService();
            try {
                UserIdMapper userIdMapper = service
                        .getUserIdMapperByExternalUserId(getGridUserType(), idpAuthnInfo.getGridIdentity());

                user = UserLocalServiceUtil.getUserById(userIdMapper.getUserId());
            } catch (Exception e) {
                logger.debug("No user found in Liferay for authenticated Grid ID. Will create a new one");

                try {
                    user = addLiferayUser(idpAuthnInfo);
                    service.updateUserIdMapper(user
                            .getUserId(), getGridUserType(), null, idpAuthnInfo.getGridIdentity());
                } catch (Exception e1) {
                    throw new AutoLoginException("Could not create user in Liferay or map the grid identity to the user");
                }
            }

            //THis is done so auto login does not run everytime
            logger.debug("Removing idp authentication from session and putting it in Portal session");

            session.removeAttribute(getSharedSessionAuthnAttribute());
            session.setAttribute(getPortalSessionAuthnAttribute(), idpAuthnInfo);

            credentials = new String[3];
            credentials[0] = String.valueOf(user.getUserId());
            credentials[1] = user.getPassword();
            credentials[2] = Boolean.TRUE.toString();
        }

        return credentials;
    }

    private User addLiferayUser(IdPAuthnInfo idpAuthnInfo) throws Exception {
        User user = null;

        long creatorUserId = -1;
        long companyId = -1;
        boolean autoPassword = true;
        String password1 = "";
        String password2 = "";
        boolean autoScreenName = true;
        String screenName = "";
        String emailAddress = idpAuthnInfo.getEmail();
        Locale locale = Locale.US;
        String firstName = idpAuthnInfo.getFirstName();
        String middleName = null;
        String lastName = idpAuthnInfo.getLastName();
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

    public String getPortalSessionAuthnAttribute() {
        return portalSessionAuthnAttribute;
    }

    public void setPortalSessionAuthnAttribute(String portalSessionAuthnAttribute) {
        this.portalSessionAuthnAttribute = portalSessionAuthnAttribute;
    }

    public String getGridUserType() {
        return gridUserType;
    }

    public void setGridUserType(String gridUserType) {
        this.gridUserType = gridUserType;
    }

    public String getSharedSessionAuthnAttribute() {
        return sharedSessionAuthnAttribute;
    }

    public void setSharedSessionAuthnAttribute(String sharedSessionAuthnAttribute) {
        this.sharedSessionAuthnAttribute = sharedSessionAuthnAttribute;
    }

    public String getOmniUserEmail() {
        return omniUserEmail;
    }

    public void setOmniUserEmail(String omniUserEmail) {
        this.omniUserEmail = omniUserEmail;
    }

    public String getCompanyWebId() {
        return companyWebId;
    }

    public void setCompanyWebId(String companyWebId) {
        this.companyWebId = companyWebId;
    }
}
