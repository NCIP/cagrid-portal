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
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.authn;

import gov.nih.nci.cagrid.portal.domain.PortalUser;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ViewGreetingController extends AbstractController {

	private String viewName;
	private String portalUserAttributeName;
    private String userGuideUrl;

	/**
	 * 
	 */
	public ViewGreetingController() {

	}

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());
		PortalUser user = (PortalUser) request.getPortletSession()
				.getAttribute(getPortalUserAttributeName(),
						PortletSession.APPLICATION_SCOPE);
		if (user != null) {
			mav.addObject("portalUser", user);
		} else {
			mav.addObject("registerUrl", request.getPreferences().getValue(
					"registerUrl", ""));
			mav.addObject("loginUrl", request.getPreferences().getValue(
					"loginUrl", ""));
            mav.addObject("userGuideUrl",getUserGuideUrl());
		}
		return mav;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getPortalUserAttributeName() {
		return portalUserAttributeName;
	}

	public void setPortalUserAttributeName(String portalUserAttributeName) {
		this.portalUserAttributeName = portalUserAttributeName;
	}

    public String getUserGuideUrl() {
        return userGuideUrl;
    }

    public void setUserGuideUrl(String userGuideUrl) {
        this.userGuideUrl = userGuideUrl;
    }
}
