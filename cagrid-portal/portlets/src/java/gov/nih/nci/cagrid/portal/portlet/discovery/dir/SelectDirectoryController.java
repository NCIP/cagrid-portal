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
package gov.nih.nci.cagrid.portal.portlet.discovery.dir;

import gov.nih.nci.cagrid.portal.portlet.ActionResponseHandler;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractCommandController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectDirectoryController extends AbstractCommandController {
	
	private DiscoveryModel discoveryModel;
	private ActionResponseHandler actionResponseHandler;

	/**
	 * 
	 */
	public SelectDirectoryController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectDirectoryController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectDirectoryController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void handleAction(ActionRequest request, ActionResponse response,
			Object obj, BindException errors) throws Exception {
		AbstractDirectoryBean dirBean = (AbstractDirectoryBean)obj;
		getDiscoveryModel().selectDirectory(dirBean.getSelectedDirectory());
		getActionResponseHandler().handle(request, response);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleRender(javax.portlet.RenderRequest, javax.portlet.RenderResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView handleRender(RenderRequest arg0,
			RenderResponse arg1, Object arg2, BindException arg3)
			throws Exception {
		throw new IllegalStateException("This method should not be called");
	}

	@Required
	public DiscoveryModel getDiscoveryModel() {
		return discoveryModel;
	}

	public void setDiscoveryModel(DiscoveryModel discoveryModel) {
		this.discoveryModel = discoveryModel;
	}

	@Required
	public ActionResponseHandler getActionResponseHandler() {
		return actionResponseHandler;
	}

	public void setActionResponseHandler(ActionResponseHandler actionResponseHandler) {
		this.actionResponseHandler = actionResponseHandler;
	}

}
