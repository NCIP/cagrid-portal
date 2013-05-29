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
package gov.nih.nci.cagrid.portal.portlet.tab;

import gov.nih.nci.cagrid.portal.portlet.ActionResponseHandler;
import gov.nih.nci.cagrid.portal.portlet.CommandActionResponseHandler;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class TabActionResponseHandler implements ActionResponseHandler,
		CommandActionResponseHandler {

	private static final Log logger = LogFactory
			.getLog(TabActionResponseHandler.class);

	private TabControlConfig tabControlConfig;

	private String newTabPath;

	private String errorTabPath;
	
	private String errorsAttributeName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.ActionPostHandler#handle(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse)
	 */
	public void handle(ActionRequest request, ActionResponse response) {
		handle(request, response, getNewTabPath());
	}

	public void handle(ActionRequest request, ActionResponse response,
			Object command, BindException errors) {
		if (errors != null && errors.hasErrors()) {
			if(getErrorsAttributeName() != null){
				request.setAttribute(getErrorsAttributeName(), errors);
			}
			handle(request, response, getErrorTabPath());
		} else {
			handle(request, response);
		}
	}

	protected void handle(ActionRequest request, ActionResponse response,
			String newPath) {
		String paramName = getTabControlConfig().getSelectedPathParameterName();
		String path = getTabControlConfig().getTabModel().getCurrentPath();
		if (newPath != null) {
			logger.debug("old path = " + path);
			getTabControlConfig().getTabModel().select(newPath);
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

	public String getErrorTabPath() {
		return errorTabPath;
	}

	public void setErrorTabPath(String errorTabPath) {
		this.errorTabPath = errorTabPath;
	}

	public String getErrorsAttributeName() {
		return errorsAttributeName;
	}

	public void setErrorsAttributeName(String errorsAttributeName) {
		this.errorsAttributeName = errorsAttributeName;
	}

}
