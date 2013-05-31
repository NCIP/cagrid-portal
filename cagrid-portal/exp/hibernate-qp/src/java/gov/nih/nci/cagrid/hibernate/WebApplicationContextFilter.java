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
package gov.nih.nci.cagrid.hibernate;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class WebApplicationContextFilter extends BasicHandler {

	private static final Log logger = LogFactory
			.getLog(WebApplicationContextFilter.class);

	public static final String WEB_APPLICATION_CONTEXT_PROPERTY = "webApplicationContext";

	/**
	 * 
	 */
	public WebApplicationContextFilter() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {
		if (!messageContext.containsProperty(WEB_APPLICATION_CONTEXT_PROPERTY)) {
			try {
				messageContext.setProperty(WEB_APPLICATION_CONTEXT_PROPERTY,
						lookupWebApplicationContext(messageContext));
			} catch (Exception ex) {
				String msg = "Error looking up WebApplicationContext: "
						+ ex.getMessage();
				logger.error(msg, ex);
				throw new AxisFault(msg, ex);
			}
		}
	}

	protected WebApplicationContext lookupWebApplicationContext(
			MessageContext messageContext) {

		HttpServletRequest servletRequest = (HttpServletRequest) messageContext
				.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
		WebApplicationContext wac = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletRequest.getSession()
						.getServletContext());

		return wac;
	}

}
