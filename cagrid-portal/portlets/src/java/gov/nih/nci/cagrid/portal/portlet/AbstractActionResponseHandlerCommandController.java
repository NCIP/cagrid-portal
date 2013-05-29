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
package gov.nih.nci.cagrid.portal.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractCommandController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractActionResponseHandlerCommandController extends
		AbstractCommandController {
	
	private CommandActionResponseHandler actionResponseHandler;

	/**
	 * 
	 */
	public AbstractActionResponseHandlerCommandController() {

	}

	/**
	 * @param commandClass
	 */
	public AbstractActionResponseHandlerCommandController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public AbstractActionResponseHandlerCommandController(Class commandClass,
			String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void handleAction(ActionRequest request, ActionResponse response,
			Object obj, BindException errors) throws Exception {
		doHandleAction(request, response, obj, errors);
		getActionResponseHandler().handle(request, response, obj, errors);
	}
	
	protected abstract void doHandleAction(ActionRequest request, ActionResponse response, Object obj, BindException errors) throws Exception;
	
	

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleRender(javax.portlet.RenderRequest, javax.portlet.RenderResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView handleRender(RenderRequest arg0,
			RenderResponse arg1, Object arg2, BindException arg3)
			throws Exception {
		throw new IllegalStateException(getClass().getName() + " does not handle render phase.");
	}

	public CommandActionResponseHandler getActionResponseHandler() {
		return actionResponseHandler;
	}

	public void setActionResponseHandler(CommandActionResponseHandler actionResponseHandler) {
		this.actionResponseHandler = actionResponseHandler;
	}

}
