package gov.nih.nci.cagrid.browser.navigation;

//~--- non-JDK imports --------------------------------------------------------

import gov.nih.nci.cagrid.browser.beans.GridLoginServices;
import gov.nih.nci.cagrid.browser.util.ApplicationCtx;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Aug 3, 2005
 * Time: 1:53:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginNavigationHandler extends NavigationHandler {
    NavigationHandler _baseHandler;

    //~--- constructors -------------------------------------------------------

    public LoginNavigationHandler(NavigationHandler base) {
        super();

        _baseHandler = base;
    }

    //~--- methods ------------------------------------------------------------

    public void handleNavigation(FacesContext facesContext,
                                 String actionMethod, String actionName) {

        /** Check if use has valid login */
        GridLoginServices loginBean =
                (GridLoginServices) ApplicationCtx.getBean(
                        "#{loginBean}");

        /*
         * Make sure user is not trying to login or logout or Register
         */
        if (actionMethod != null) {
            if ((actionMethod
                    .indexOf(GridLoginServices
                            .GLOBAL_LOGIN_ACTION_NAME) < 0) && (actionMethod
                    .indexOf(GridLoginServices
                            .GLOBAL_LOGOUT_ACTION_NAME) < 0) && (actionMethod
                    .indexOf(GridLoginServices
                            .GLOBAL_USER_REGISTER_ACTION_NAME) < 0)) {

                /** If user is not logged in then redirect to login page */
                if (!loginBean.isUserLoggedIn()) {
                    //properly logout if user not logged out properly

                    actionName = GridLoginServices.GLOBAL_LOGIN_ACTION_NAME;
                    actionMethod =
                            GridLoginServices.GLOBAL_LOGIN_ACTION_FAILED_METHOD;
                }
            }
        }
        _baseHandler.handleNavigation(facesContext, actionMethod, actionName);
    }
}

//~ Formatted by Jindent --- http://www.jindent.com
