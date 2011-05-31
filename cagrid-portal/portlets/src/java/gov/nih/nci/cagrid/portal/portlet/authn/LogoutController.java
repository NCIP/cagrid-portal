/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.authn;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class LogoutController extends AbstractController {

	
	
	/**
	 * 
	 */
	public LogoutController() {

	}
	
	protected void handleActionRequestInternal(ActionRequest request,
            ActionResponse response)
     throws Exception{
		String logoutUrl = request.getPreferences().getValue("logoutUrl", null);
		request.getPortletSession().invalidate();
		response.sendRedirect(logoutUrl);
	}

}
