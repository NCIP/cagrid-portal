package gov.nih.nci.cagrid.browser.beans;

//~--- non-JDK imports --------------------------------------------------------

import org.globus.gsi.GlobusCredential;

//~--- classes ----------------------------------------------------------------

/**
 * This class will communicate with the
 * caGums service and retreive and store
 * user credentials
 * <p/>
 * Created by the caGrid Team
 * User: kherm
 * Date: Jun 20, 2005
 * Time: 2:46:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridLoginServices {
    public static final String GLOBAL_LOGIN_ACTION_NAME = "#{loginBean.doLogin}";
    public static final String GLOBAL_LOGOUT_ACTION_NAME = "doLogout";
    public static final String GLOBAL_USER_REGISTER_ACTION_NAME =
            "doRegister";
    public static final String GLOBAL_LOGIN_ACTION_SUCCESS_METHOD = "success";
    public static final String GLOBAL_LOGIN_ACTION_FAILED_METHOD = "failed";

    //~--- fields -------------------------------------------------------------

    private GlobusCredential gridCredential = null;
    private boolean userLoggedIn = false;
    private String guestPassword;
    private String guestPasswordMapsTo;

    /**
     * Guest username and password that users can use on the cagrid-browser
     */
    private String guestUsername;

    /**
     * Guest user maps to this grid username and password
     */
    private String guestUsernameMapsTo;

    private String gumsURL;
    private GridUserInformation newUserInfo;
    private int proxyValidTime;
    private String userLogin;
    private String userPasswd;

    //~--- constructors -------------------------------------------------------

    public GridLoginServices() {
        newUserInfo = new GridUserInformation();
    }

    //~--- methods ------------------------------------------------------------

    public String doLogin() {

        /**
         * Guest user login. Configured thru faces-config.xml

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


         gridCredential = gumsClient.createProxy(this.proxyValidTime);
         }
         } catch (Exception e) {

         ApplicationCtx.logInfo("Login Message: User " + userLogin + " tried unsucessfully to login");

         doLoginFieldCleanup();

         return GLOBAL_LOGIN_ACTION_FAILED_METHOD;
         }


         ApplicationCtx.logInfo("Login Message: User "
         + userLogin + " logged in at "
         + Calendar.getInstance().getTime());
         */
        return GLOBAL_LOGIN_ACTION_SUCCESS_METHOD;
    }

    private void doLoginFieldCleanup() {
        this.userLogin = null;
        this.userPasswd = null;
        this.gridCredential = null;
    }

    public String doLogout() {
        /*
           try {
               gumsClient.destroyProxy();
           } catch (GumsException e) {
               ApplicationCtx.logError(e.getMessage());
           }

               ApplicationCtx.logInfo("Login Message: User "
                   + userLogin + " logged out at "
                   + Calendar.getInstance().getTime());


          doLoginFieldCleanup();

        */

        return "success";
    }

    public String doRegister() {

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

        return true;
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
