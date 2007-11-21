/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet;

import gov.nih.nci.cagrid.portal.domain.PortalUser;

import javax.portlet.PortletSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.portlet.context.PortletWebRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class PortalUserRequestInterceptor implements WebRequestInterceptor {

	private String portalUserAttributeName;

	/**
	 * 
	 */
	public PortalUserRequestInterceptor() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.context.request.WebRequestInterceptor#afterCompletion(org.springframework.web.context.request.WebRequest,
	 *      java.lang.Exception)
	 */
	public void afterCompletion(WebRequest arg0, Exception arg1)
			throws Exception {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.context.request.WebRequestInterceptor#postHandle(org.springframework.web.context.request.WebRequest,
	 *      org.springframework.ui.ModelMap)
	 */
	public void postHandle(WebRequest arg0, ModelMap arg1) throws Exception {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.context.request.WebRequestInterceptor#preHandle(org.springframework.web.context.request.WebRequest)
	 */
	public void preHandle(WebRequest webRequest) throws Exception {
		PortletWebRequest portletWebRequest = (PortletWebRequest) webRequest;
		PortalUser portalUser = (PortalUser) portletWebRequest.getRequest()
				.getPortletSession().getAttribute(getPortalUserAttributeName(),
						PortletSession.APPLICATION_SCOPE);
		if (portalUser != null) {
			portletWebRequest.getRequest().setAttribute(
					getPortalUserAttributeName(), portalUser);
		}
	}

	public String getPortalUserAttributeName() {
		return portalUserAttributeName;
	}

	public void setPortalUserAttributeName(String portalUserAttributeName) {
		this.portalUserAttributeName = portalUserAttributeName;
	}

}
