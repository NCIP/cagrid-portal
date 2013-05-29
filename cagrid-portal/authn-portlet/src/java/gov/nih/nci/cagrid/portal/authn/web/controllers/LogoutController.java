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

