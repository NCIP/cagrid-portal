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
package gov.nih.nci.cagrid.portal.service;

import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserServiceUtil;
import com.liferay.portal.util.TestPropsValues;
import org.junit.Test;

import java.util.Calendar;
import java.util.Locale;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class UserServiceTest extends DuelDBServiceTestBase {

    public UserServiceTest() {
    }

    @Test
    public void testAddUser() throws Exception {
        addUser();
    }

    @Test
    public void testDeleteUser() throws Exception {

        User user = addUser();

        UserServiceUtil.deleteUser(user.getUserId());
    }

    @Test
    public void testGetUser() throws Exception {

        User user = addUser();

        UserServiceUtil.getUserByEmailAddress(
                TestPropsValues.COMPANY_ID, user.getEmailAddress());
    }

    protected User addUser() throws Exception {
        boolean autoPassword = true;
        String password1 = StringPool.BLANK;
        String password2 = StringPool.BLANK;
        boolean autoScreenName = true;
        String screenName = StringPool.BLANK;
        String emailAddress = "UserServiceTest." + nextLong() + "@liferay.com";
        String openId = StringPool.BLANK;
        Locale locale = LocaleUtil.getDefault();
        String firstName = "UserServiceTest";
        String middleName = StringPool.BLANK;
        String lastName = "UserServiceTest";
        int prefixId = 0;
        int suffixId = 0;
        boolean male = true;
        int birthdayMonth = Calendar.JANUARY;
        int birthdayDay = 1;
        int birthdayYear = 1970;
        String jobTitle = StringPool.BLANK;
        long[] groupIds = null;
        long[] organizationIds = null;
        long[] roleIds = null;
        long[] userGroupIds = null;
        boolean sendMail = false;

        ServiceContext serviceContext = new ServiceContext();

        return UserServiceUtil.addUser(
                TestPropsValues.COMPANY_ID, autoPassword, password1, password2,
                autoScreenName, screenName, emailAddress, openId, locale, firstName,
                middleName, lastName, prefixId, suffixId, male, birthdayMonth,
                birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds,
                roleIds, userGroupIds, sendMail, serviceContext);
    }

}
