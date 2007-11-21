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
public class LoginController extends AbstractController {

	
	
	/**
	 * 
	 */
	public LoginController() {

	}
	
	protected void handleActionRequestInternal(ActionRequest request,
            ActionResponse response)
     throws Exception{
		String loginUrl = request.getPreferences().getValue("loginUrl", null);
		response.sendRedirect(loginUrl);
	}

}
