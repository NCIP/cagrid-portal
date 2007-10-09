/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet;

import gov.nih.nci.cagrid.portal2.domain.PortalUser;

import javax.portlet.PortletSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.portlet.context.PortletWebRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class PortalUserInterceptor implements WebRequestInterceptor {

	private static final Log logger = LogFactory.getLog(PortalUserInterceptor.class);
	
	private SharedApplicationModel sharedApplicationModel;

	private String portalUserAttributeName;

	/**
	 * 
	 */
	public PortalUserInterceptor() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.context.request.WebRequestInterceptor#afterCompletion(org.springframework.web.context.request.WebRequest,
	 *      java.lang.Exception)
	 */
	public void afterCompletion(WebRequest request, Exception ex)
			throws Exception {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.context.request.WebRequestInterceptor#postHandle(org.springframework.web.context.request.WebRequest,
	 *      org.springframework.ui.ModelMap)
	 */
	public void postHandle(WebRequest request, ModelMap model) throws Exception {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.context.request.WebRequestInterceptor#preHandle(org.springframework.web.context.request.WebRequest)
	 */
	public void preHandle(WebRequest request) throws Exception {

		if (getSharedApplicationModel().getPortalUser() == null) {
			
			logger.debug("No PortalUser in SharedApplicationModel. Looking in session.");
			
			PortletWebRequest req = (PortletWebRequest) request;
			PortalUser user = (PortalUser) req.getRequest().getPortletSession()
					.getAttribute(getPortalUserAttributeName(),
							PortletSession.APPLICATION_SCOPE);
			
			if(user != null){
				logger.debug("Found PortalUser:" + user.getId() + " in session.");
				getSharedApplicationModel().setPortalUser(user);
			}
			
		}
	}

	public String getPortalUserAttributeName() {
		return portalUserAttributeName;
	}

	public void setPortalUserAttributeName(String portletUserAttributeName) {
		this.portalUserAttributeName = portletUserAttributeName;
	}

	public SharedApplicationModel getSharedApplicationModel() {
		return sharedApplicationModel;
	}

	public void setSharedApplicationModel(
			SharedApplicationModel sharedApplicationModel) {
		this.sharedApplicationModel = sharedApplicationModel;
	}

}
