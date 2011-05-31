/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.authn;

import gov.nih.nci.cagrid.portal.domain.PortalUser;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ViewLoginController extends AbstractController {

	private String portalUserAttributeName;
	private String viewName;
	private String errorsAttributeName;

	/**
	 * 
	 */
	public ViewLoginController() {

	}

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		ModelAndView mav = null;
		BindException errors = null;
		if(getErrorsAttributeName() != null){
			errors = (BindException)request.getAttribute(getErrorsAttributeName());
		}
		if(errors != null){
			mav = new ModelAndView(getViewName(), errors.getModel());
		}else{
			mav = new ModelAndView(getViewName());
		}
		
		logger.debug("Looking for PortalUser under '" + getPortalUserAttributeName() + "'");
		
		PortalUser user = (PortalUser) request.getPortletSession()
				.getAttribute(getPortalUserAttributeName(),
						PortletSession.APPLICATION_SCOPE);
		if (user != null) {
			mav.addObject("portalUser", user);
		} else {
			mav.addObject("registerUrl", request.getPreferences().getValue("registerUrl", ""));
		}

		return mav;
	}

	@Required
	public String getPortalUserAttributeName() {
		return portalUserAttributeName;
	}

	public void setPortalUserAttributeName(String portalUserAttributeName) {
		this.portalUserAttributeName = portalUserAttributeName;
	}

	@Required
	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getErrorsAttributeName() {
		return errorsAttributeName;
	}

	public void setErrorsAttributeName(String errorsAttributeName) {
		this.errorsAttributeName = errorsAttributeName;
	}

	

}
