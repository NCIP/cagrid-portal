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
