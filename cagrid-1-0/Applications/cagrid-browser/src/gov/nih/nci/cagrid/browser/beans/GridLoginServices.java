package gov.nih.nci.cagrid.browser.beans;

//~--- non-JDK imports --------------------------------------------------------

import gov.nih.nci.cagrid.browser.util.ApplicationCtx;
import gov.nih.nci.cagrid.gums.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.gums.bean.RegistrationApplication;
import gov.nih.nci.cagrid.gums.bean.UserInformation;
import gov.nih.nci.cagrid.gums.client.GridUserManagementClient;
import gov.nih.nci.cagrid.gums.common.GridUserManagementService;
import gov.nih.nci.cagrid.gums.common.GumsException;
import org.globus.gsi.GlobusCredential;

import java.net.URL;
import java.util.Calendar;

//~--- classes ----------------------------------------------------------------

/**
 *
 * This class will communicate with the
 * caGums service and retreive and store
 * user credentials
 *
 * Created by the caGrid Team
 * User: kherm
 * Date: Jun 20, 2005
 * Time: 2:46:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridLoginServices {
    public static final String GLOBAL_LOGIN_ACTION_NAME           = "#{loginBean.doLogin}";
    public static final String GLOBAL_LOGOUT_ACTION_NAME          = "doLogout";
    public static final String GLOBAL_USER_REGISTER_ACTION_NAME   =
        "doRegister";
    public static final String GLOBAL_LOGIN_ACTION_SUCCESS_METHOD = "success";
    public static final String GLOBAL_LOGIN_ACTION_FAILED_METHOD  = "failed";

    //~--- fields -------------------------------------------------------------

    private GlobusCredential gridCredential = null;
    private boolean          userLoggedIn   = false;
    private String           guestPassword;
    private String           guestPasswordMapsTo;

    /** Guest username and password that users can use on the cagrid-browser */
    private String guestUsername;

    /** Guest user maps to this grid username and password */
    private String                    guestUsernameMapsTo;
    private GridUserManagementService gumsClient;
    private String                    gumsURL;
    private GridUserInformation       newUserInfo;
    private int                       proxyValidTime;
    private String                    userLogin;
    private String                    userPasswd;

    //~--- constructors -------------------------------------------------------

    public GridLoginServices() {
        newUserInfo = new GridUserInformation();
    }

    //~--- methods ------------------------------------------------------------

    public String doLogin() {

        /**
         * Guest user login. Configured thru faces-config.xml
         */
        if (userLogin.equalsIgnoreCase(this.guestUsername)) {
            userLogin  = this.guestUsernameMapsTo;
            userPasswd = this.guestPasswordMapsTo;
        }

        try {
            URL                 gumsServerURL = new URL(gumsURL);
            BasicAuthCredential auth          = new BasicAuthCredential();

            auth.setUsername(userLogin);
            auth.setPassword(userPasswd);

            gumsClient = new GridUserManagementClient(gumsServerURL, auth);

            try {
                gridCredential = gumsClient.getProxy();
            } catch (GumsException e) {

                /** If proxy not found then create a new one */
                gridCredential = gumsClient.createProxy(this.proxyValidTime);
            }
        } catch (Exception e) {

            ApplicationCtx.logInfo("Login Message: User " + userLogin + " tried unsucessfully to login");
            /* Login failed */
            doLoginFieldCleanup();

            return GLOBAL_LOGIN_ACTION_FAILED_METHOD;
        }

        /** Log succesful sessions */
        ApplicationCtx.logInfo("Login Message: User "
                + userLogin + " logged in at "
                + Calendar.getInstance().getTime());

        return GLOBAL_LOGIN_ACTION_SUCCESS_METHOD;
    }

    private void doLoginFieldCleanup() {
        this.userLogin  = null;
        this.userPasswd = null;
        this.gridCredential = null;
    }

    public String doLogout() {
        try {
            gumsClient.destroyProxy();
        } catch (GumsException e) {
            ApplicationCtx.logError(e.getMessage());
        }

            ApplicationCtx.logInfo("Login Message: User "
                + userLogin + " logged out at "
                + Calendar.getInstance().getTime());
        

       doLoginFieldCleanup();



        return "success";
    }

    public String doRegister() {
        RegistrationApplication app = new RegistrationApplication();

        /** Set username and password information */
        app.setUsername(newUserInfo.getUsername());
        app.setPassword(newUserInfo.getPassword());
        app.setEmail(newUserInfo.getEmail());

        UserInformation[] userInfo = new UserInformation[1];

        /** Create xml for user information */
        UserInformation info = new UserInformation();

        info.setInformationNamespace("cagrid.nci.nih.gov/1/person");
        info.setInformationName("person");

        StringBuffer strXML = new StringBuffer();

        strXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        strXML.append(
            "<person xmlns=\"cagrid.nci.nih.gov/1/person\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
        strXML.append("<first-name>");
        strXML.append(newUserInfo.getFirstName());
        strXML.append("</first-name>");
        strXML.append("<last-name>");
        strXML.append(newUserInfo.getLastName());
        strXML.append("</last-name>");
        strXML.append("<address>");
        strXML.append("<street>");
        strXML.append(newUserInfo.getStreet());
        strXML.append("</street>");
        strXML.append("<city>");
        strXML.append(newUserInfo.getCity());
        strXML.append("</city>");
        strXML.append("<state>");
        strXML.append(newUserInfo.getState());
        strXML.append("</state>");
        strXML.append("<zip>");
        strXML.append(newUserInfo.getZip());
        strXML.append("</zip>");
        strXML.append("</address>");
        strXML.append("<email>");
        strXML.append(newUserInfo.getEmail());
        strXML.append("</email>");
        strXML.append("<phone-number>");
        strXML.append(newUserInfo.getPhone());
        strXML.append("</phone-number>");
        strXML.append("</person>");
        info.setInformationXML(strXML.toString());

        userInfo[0] = info;

        app.setUserInfo(userInfo);

        try {
            URL gumsServerURL = new URL(gumsURL);

            gumsClient = new GridUserManagementClient(gumsServerURL);

            gumsClient.register(app);
        } catch (Exception e) {
            ApplicationCtx.logError(e.getMessage());
            return "failed";
        }

        return "success";
    }

    //~--- get methods --------------------------------------------------------

    public GlobusCredential getGridCredential() {
        return gridCredential;
    }

    public String getGuestPassword() {
        return guestPassword;
    }

    public String getGuestPasswordMapsTo() {
        return guestPasswordMapsTo;
    }

    public String getGuestUsername() {
        return guestUsername;
    }

    public String getGuestUsernameMapsTo() {
        return guestUsernameMapsTo;
    }

    public String getGumsURL() {
        return gumsURL;
    }

    public GridUserInformation getNewUserInfo() {
        return newUserInfo;
    }

    public int getProxyValidTime() {
        return proxyValidTime;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getUserPasswd() {
        return userPasswd;
    }

    public boolean isUserLoggedIn() {

        /* Don't know if this is the best way to check valid login */
        if (gridCredential != null) {
            userLoggedIn = true;
        } else {
            userLoggedIn = false;
        }

        return userLoggedIn;
    }

    //~--- set methods --------------------------------------------------------

    public void setGridCredential(GlobusCredential gridCredential) {
        this.gridCredential = gridCredential;
    }

    public void setGuestPassword(String guestPassword) {
        this.guestPassword = guestPassword;
    }

    public void setGuestPasswordMapsTo(String guestPasswordMapsTo) {
        this.guestPasswordMapsTo = guestPasswordMapsTo;
    }

    public void setGuestUsername(String guestUsername) {
        this.guestUsername = guestUsername;
    }

    public void setGuestUsernameMapsTo(String guestUsernameMapsTo) {
        this.guestUsernameMapsTo = guestUsernameMapsTo;
    }

    public void setGumsURL(String gumsURL) {
        this.gumsURL = gumsURL;
    }

    public void setNewUserInfo(GridUserInformation newUserInfo) {
        this.newUserInfo = newUserInfo;
    }

    public void setProxyValidTime(int proxyValidTime) {
        this.proxyValidTime = proxyValidTime;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
