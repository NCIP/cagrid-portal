/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.filter;

import javax.portlet.PortletSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.portlet.context.PortletWebRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CSRFTokenProviderRequestInterceptor implements
		WebRequestInterceptor {

	private String csrfTokenName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.context.request.WebRequestInterceptor#afterCompletion
	 * (org.springframework.web.context.request.WebRequest, java.lang.Exception)
	 */
	public void afterCompletion(WebRequest arg0, Exception arg1)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.context.request.WebRequestInterceptor#postHandle
	 * (org.springframework.web.context.request.WebRequest,
	 * org.springframework.ui.ModelMap)
	 */
	public void postHandle(WebRequest arg0, ModelMap arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.context.request.WebRequestInterceptor#preHandle
	 * (org.springframework.web.context.request.WebRequest)
	 */
	public void preHandle(WebRequest req) throws Exception {
		if (req instanceof PortletWebRequest) {
			PortletWebRequest pReq = (PortletWebRequest) req;
			String token = (String) pReq.getRequest().getPortletSession().getAttribute(
					getCsrfTokenName(), PortletSession.APPLICATION_SCOPE);
			pReq.getRequest().setAttribute(getCsrfTokenName(), token);
		}
	}

	public String getCsrfTokenName() {
		return csrfTokenName;
	}

	public void setCsrfTokenName(String csrfTokenName) {
		this.csrfTokenName = csrfTokenName;
	}

}
