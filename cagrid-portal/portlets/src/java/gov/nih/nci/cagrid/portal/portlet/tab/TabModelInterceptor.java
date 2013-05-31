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
package gov.nih.nci.cagrid.portal.portlet.tab;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class TabModelInterceptor implements WebRequestInterceptor {

	private static final Log logger = LogFactory
			.getLog(TabModelInterceptor.class);

	private TabControlConfig tabControlConfig;

	/**
	 * 
	 */
	public TabModelInterceptor() {

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
	public void preHandle(WebRequest request) throws Exception {

		String selectedPath = request.getParameter(getTabControlConfig()
				.getSelectedPathParameterName());
		if (selectedPath != null) {
			logger.debug("selecting path '" + selectedPath + "'");
			getTabControlConfig().getTabModel().select(selectedPath);
		} else {
			logger.debug("No selectedPath, using default: "
					+ getTabControlConfig().getTabModel().getCurrentPath());
		}
		logger
				.debug("setting tab model under '"
						+ getTabControlConfig()
								.getTabModelRequestAttributeName() + "'");
		request.setAttribute(getTabControlConfig()
				.getTabModelRequestAttributeName(), getTabControlConfig()
				.getTabModel(), WebRequest.SCOPE_REQUEST);

	}

	public TabControlConfig getTabControlConfig() {
		return tabControlConfig;
	}

	public void setTabControlConfig(TabControlConfig tabControlConfig) {
		this.tabControlConfig = tabControlConfig;
	}

}
