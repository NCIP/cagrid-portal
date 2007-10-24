/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.tab;

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
	
	private static final Log logger = LogFactory.getLog(TabModelInterceptor.class);
	
	private TabModel tabModel;
	private String selectedPathParameterName;
	private String tabModelRequestAttributeName;

	/**
	 * 
	 */
	public TabModelInterceptor() {

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.context.request.WebRequestInterceptor#afterCompletion(org.springframework.web.context.request.WebRequest, java.lang.Exception)
	 */
	public void afterCompletion(WebRequest arg0, Exception arg1)
			throws Exception {
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.context.request.WebRequestInterceptor#postHandle(org.springframework.web.context.request.WebRequest, org.springframework.ui.ModelMap)
	 */
	public void postHandle(WebRequest arg0, ModelMap arg1) throws Exception {
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.context.request.WebRequestInterceptor#preHandle(org.springframework.web.context.request.WebRequest)
	 */
	public void preHandle(WebRequest request) throws Exception {

		String selectedPath = request.getParameter(getSelectedPathParameterName());
		if(selectedPath != null){
			logger.debug("selecting path '" + selectedPath + "'");
			getTabModel().select(selectedPath);
		}
		request.setAttribute(getTabModelRequestAttributeName(), getTabModel(), WebRequest.SCOPE_REQUEST);
	}

	@Required
	public TabModel getTabModel() {
		return tabModel;
	}

	public void setTabModel(TabModel tabModel) {
		this.tabModel = tabModel;
	}

	@Required
	public String getSelectedPathParameterName() {
		return selectedPathParameterName;
	}

	public void setSelectedPathParameterName(String selectedPathParamName) {
		this.selectedPathParameterName = selectedPathParamName;
	}

	public String getTabModelRequestAttributeName() {
		return tabModelRequestAttributeName;
	}

	public void setTabModelRequestAttributeName(String tabModelRequestAttributeName) {
		this.tabModelRequestAttributeName = tabModelRequestAttributeName;
	}


}
