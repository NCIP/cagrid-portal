/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class OpenSessionInViewInterceptor extends
		org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor {

	private static Log logger = LogFactory.getLog(OpenSessionInViewInterceptor.class);
	
	public void afterCompletion(WebRequest request, Exception ex) throws DataAccessException {
		super.afterCompletion(request, ex);
	}
	public void postHandle(WebRequest request, ModelMap model) throws DataAccessException {
		super.postHandle(request, model);
	}
	public void preHandle(WebRequest request) throws DataAccessException {
		super.preHandle(request);
	}
}
