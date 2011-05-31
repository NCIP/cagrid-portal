/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.portlet.PortletSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.portlet.context.PortletWebRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 *
 */
public class RequestAttributePropertiesPopulatorInterceptor implements
		WebRequestInterceptor {
	
	private Map<String,String> properties = new HashMap<String,String>();

	/* (non-Javadoc)
	 * @see org.springframework.web.context.request.WebRequestInterceptor#afterCompletion(org.springframework.web.context.request.WebRequest, java.lang.Exception)
	 */
	public void afterCompletion(WebRequest arg0, Exception arg1)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.context.request.WebRequestInterceptor#postHandle(org.springframework.web.context.request.WebRequest, org.springframework.ui.ModelMap)
	 */
	public void postHandle(WebRequest arg0, ModelMap arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.context.request.WebRequestInterceptor#preHandle(org.springframework.web.context.request.WebRequest)
	 */
	public void preHandle(WebRequest webRequest) throws Exception {
        PortletWebRequest portletWebRequest = (PortletWebRequest) webRequest;

		for(String prop : getProperties().keySet()){
			portletWebRequest.getRequest().setAttribute(prop, getProperties().get(prop));
		}
	}

	public Map<String,String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String,String> properties) {
		this.properties = properties;
	}

}
