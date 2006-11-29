/**
 * $Id $
 */

package gov.nih.nci.cagrid.browser.navigation;

import gov.nih.nci.cagrid.browser.beans.LoginBean;
import gov.nih.nci.cagrid.browser.util.AppUtils;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:manavkher@hotmail.com">Manav Kher</a>
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class LoggedInCheck implements PhaseListener {
	
	private static Logger logger = Logger.getLogger(LoggedInCheck.class);
	
	
	public void afterPhase(PhaseEvent phaseEvent) {
		FacesContext fc = phaseEvent.getFacesContext();

		// Check to see if they are on the login page.
		boolean loginPage = fc.getViewRoot().getViewId().lastIndexOf("Login") > -1;
		boolean jsPage = fc.getViewRoot().getViewId().lastIndexOf(".css") > -1;
		boolean cssPage = fc.getViewRoot().getViewId().lastIndexOf(".gif") > -1;
		boolean gifPage = fc.getViewRoot().getViewId().lastIndexOf(".js") > -1;

		LoginBean appLogin = (LoginBean) AppUtils.getBean("loginBean");

		if (!loginPage && !appLogin.isUserLoggedIn() && !jsPage && !gifPage
				&& !cssPage) {
			
			logger.debug("Not logged in! Logging out.");
			
			NavigationHandler nh = fc.getApplication().getNavigationHandler();
			nh.handleNavigation(fc, null, "logout");
		}
	}

	public void beforePhase(PhaseEvent phaseEvent) {
	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
