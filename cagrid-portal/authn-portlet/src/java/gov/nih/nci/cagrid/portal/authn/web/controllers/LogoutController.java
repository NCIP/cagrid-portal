package gov.nih.nci.cagrid.portal.authn.web.controllers;

import org.springframework.web.portlet.mvc.AbstractController;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
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

