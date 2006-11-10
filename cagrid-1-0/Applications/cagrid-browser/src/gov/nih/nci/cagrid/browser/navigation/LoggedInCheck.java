package gov.nih.nci.cagrid.browser.navigation;

import gov.nih.nci.cagrid.browser.beans.GridLoginServices;
import gov.nih.nci.cagrid.browser.util.ApplicationCtx;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jan 13, 2006
 * Time: 5:34:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoggedInCheck implements PhaseListener {
    public void afterPhase(PhaseEvent phaseEvent) {
        FacesContext fc = phaseEvent.getFacesContext();

        // Check to see if they are on the login page.
        boolean loginPage = fc.getViewRoot().getViewId().lastIndexOf("Login") > -1;
        boolean jsPage = fc.getViewRoot().getViewId().lastIndexOf(".css") > -1;
        boolean cssPage = fc.getViewRoot().getViewId().lastIndexOf(".gif") > -1;
        boolean gifPage = fc.getViewRoot().getViewId().lastIndexOf(".js") > -1;


        GridLoginServices appLogin =
                (GridLoginServices) ApplicationCtx.getBean(
                        "loginBean");

        if (!loginPage && !appLogin.isUserLoggedIn() && !jsPage && !gifPage && !cssPage) {
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "logout");
        }
    }

    public void beforePhase(PhaseEvent phaseEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }


}
