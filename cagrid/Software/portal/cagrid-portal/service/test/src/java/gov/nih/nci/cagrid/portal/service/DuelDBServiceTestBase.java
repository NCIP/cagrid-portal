/**
 *
 */
package gov.nih.nci.cagrid.portal.service;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.*;
import com.liferay.portal.util.InitUtil;
import com.liferay.portal.util.PortalInstances;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.util.PwdGenerator;
import gov.nih.nci.cagrid.portal.AbstractDBTestBase;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class DuelDBServiceTestBase extends AbstractDBTestBase {

    private PermissionChecker permissionChecker = null;
    private Random random = new Random();
    private ClassPathXmlApplicationContext serviceApplicationContext;
    private Role catalogUserRole;
    private Role catalogAdminRole;

    private static final String CATALOG_USER_ROLE = "Catalog User";
    private static final String CATALOG_ADMIN_ROLE = "Catalog Admin";

    public DuelDBServiceTestBase() {
        try {
            if (System.getProperty("external-properties") == null) {
                System.setProperty("external-properties",
                        "portal-test.properties");
            }

            InitUtil.initWithSpring();

            this.serviceApplicationContext = new ClassPathXmlApplicationContext(
                    getConfigLocations(), getApplicationContext());

            switchToAdminUser();
            setUpCatalogUserRole();
            setUpCatalogAdminRole();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(
                    "Error initializing: " + ex.getMessage(), ex);
        }
    }

    public String[] getConfigLocations() {
        return new String[]{"classpath:applicationContext-service.xml"};
    }

    public ApplicationContext getServiceApplicationContext() {
        return serviceApplicationContext;
    }

    public void setUpCatalogUserRole() throws Exception {
        try {
            catalogUserRole = RoleServiceUtil.getRole(
                    TestPropsValues.COMPANY_ID, CATALOG_USER_ROLE);
        } catch (Exception ex) {

        }
        if (catalogUserRole == null) {
            // Create the Catalog User group
            try {
                String roleDesc = "A caGrid Portal catalog user.";
                catalogUserRole = RoleServiceUtil.addRole(CATALOG_USER_ROLE,
                        roleDesc, RoleConstants.TYPE_REGULAR);
            } catch (Exception ex) {
                ex.printStackTrace();
                fail("Error creatinging Catalog User role: " + ex.getMessage());
            }

            // Assign the appropriate privileges
            try {
                long companyId = TestPropsValues.COMPANY_ID;
                int scope = ResourceConstants.SCOPE_COMPANY;
                long roleId = catalogUserRole.getRoleId();
                String name = PortletKeys.PORTAL;
                String actionId = ActionKeys.ADD_COMMUNITY;
                String primKey = String.valueOf(companyId);

                PermissionServiceUtil.setRolePermission(roleId, companyId,
                        name, scope, primKey, actionId);

            } catch (Exception ex) {
                fail("Error assigning permissions to Catalog User role: " + ex.getMessage());
            }
        }
    }

    public void setUpCatalogAdminRole() throws Exception {
        try {
            catalogAdminRole = RoleServiceUtil.getRole(
                    TestPropsValues.COMPANY_ID, CATALOG_ADMIN_ROLE);
        } catch (Exception ex) {

        }
        if (catalogAdminRole == null) {
            // Create the Catalog Admin role
            try {
                String roleDesc = "A caGrid Portal catalog administrtor.";
                catalogAdminRole = RoleServiceUtil.addRole(CATALOG_ADMIN_ROLE,
                        roleDesc, RoleConstants.TYPE_REGULAR);
            } catch (Exception ex) {
                ex.printStackTrace();
                fail("Error creatinging Catalog Admin role: " + ex.getMessage());
            }

            // Assign the appropriate privileges
            try {
                long companyId = TestPropsValues.COMPANY_ID;
                int scope = ResourceConstants.SCOPE_COMPANY;
                long roleId = catalogAdminRole.getRoleId();
                String name = PortletKeys.ENTERPRISE_ADMIN;
                String actionId = ActionKeys.DELETE;
                String primKey = "com.liferay.portal.model.User";

//				PermissionServiceUtil.setR.setRolePermissions(roleId, scope, new String[]{actionId}, primKey);
                PermissionServiceUtil.setRolePermission(roleId, companyId,
                        name, scope, primKey, actionId);

            } catch (Exception ex) {
                fail("Error assigning permissions to Catalog Admin role: " + ex.getMessage());
            }
        }
    }

    @Before
    public void switchToAdminUser() throws Exception {
        PortalInstances.addCompanyId(TestPropsValues.COMPANY_ID);
        PrincipalThreadLocal.setName(TestPropsValues.USER_ID);
        User user = UserLocalServiceUtil.getUserById(TestPropsValues.USER_ID);
        permissionChecker = PermissionCheckerFactoryUtil.create(user, true);
        PermissionThreadLocal.setPermissionChecker(permissionChecker);
    }

    protected Date nextDate() throws Exception {
        return new Date();
    }

    protected double nextDouble() throws Exception {
        return CounterLocalServiceUtil.increment();
    }

    protected int nextInt() throws Exception {
        return (int) CounterLocalServiceUtil.increment();
    }

    protected long nextLong() throws Exception {
        return CounterLocalServiceUtil.increment();
    }

    protected boolean randomBoolean() throws Exception {
        return random.nextBoolean();
    }

    protected String randomString() throws Exception {
        return PwdGenerator.getPassword();
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

        return UserServiceUtil.addUser(TestPropsValues.COMPANY_ID,
                autoPassword, password1, password2, autoScreenName, screenName,
                emailAddress, openId, locale, firstName, middleName, lastName,
                prefixId, suffixId, male, birthdayMonth, birthdayDay,
                birthdayYear, jobTitle, groupIds, organizationIds, roleIds,
                userGroupIds, sendMail, serviceContext);
    }

    public Role getCatalogUserRole() {
        return catalogUserRole;
    }

    public void setCatalogUserRole(Role catalogUserRole) {
        this.catalogUserRole = catalogUserRole;
    }

    public Role getCatalogAdminRole() {
        return catalogAdminRole;
    }

    public void setCatalogAdminRole(Role catalogAdminRole) {
        this.catalogAdminRole = catalogAdminRole;
    }

}
