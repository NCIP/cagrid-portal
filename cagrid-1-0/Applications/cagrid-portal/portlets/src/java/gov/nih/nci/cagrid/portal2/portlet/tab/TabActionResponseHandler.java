/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.tab;

import gov.nih.nci.cagrid.portal2.portlet.ActionResponseHandler;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class TabActionResponseHandler implements ActionResponseHandler {
	
	private static final Log logger = LogFactory.getLog(TabActionResponseHandler.class);

	private TabControlConfig tabControlConfig;
	
	private String newTabPath;

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.portlet.ActionPostHandler#handle(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse)
	 */
	public void handle(ActionRequest request, ActionResponse response) {
		String paramName = getTabControlConfig().getSelectedPathParameterName();
		String path = getTabControlConfig().getTabModel().getCurrentPath();
		if(getNewTabPath() != null){
			logger.debug("old path = " + path);
			getTabControlConfig().getTabModel().select(getNewTabPath());
			path = getTabControlConfig().getTabModel().getCurrentPath();
			logger.debug("new path = " + path);
		}
		logger.debug("Setting " + paramName + " = " + path);
		response.setRenderParameter(paramName, path);
	}

	@Required
	public TabControlConfig getTabControlConfig() {
		return tabControlConfig;
	}

	public void setTabControlConfig(TabControlConfig tabControlConfig) {
		this.tabControlConfig = tabControlConfig;
	}

	public String getNewTabPath() {
		return newTabPath;
	}

	public void setNewTabPath(String newTabPath) {
		this.newTabPath = newTabPath;
	}

}
