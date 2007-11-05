/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.details;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

import gov.nih.nci.cagrid.portal2.portlet.AbstractActionResponseHandlerCommandController;
import gov.nih.nci.cagrid.portal2.portlet.InterPortletMessageManager;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectUmlClassForQueryController extends
		AbstractActionResponseHandlerCommandController {
	
	private InterPortletMessageManager interPortletMessageManager;

	/**
	 * 
	 */
	public SelectUmlClassForQueryController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectUmlClassForQueryController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectUmlClassForQueryController(Class commandClass,
			String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		
		SelectDetailsCommand command = (SelectDetailsCommand)obj;
		String outputQueueName = request.getPreferences().getValue("selectedUmlClassOutputQueueName", "selectedUmlClassId");
		getInterPortletMessageManager().send(request, outputQueueName, command.getSelectedId());

	}

	public InterPortletMessageManager getInterPortletMessageManager() {
		return interPortletMessageManager;
	}

	public void setInterPortletMessageManager(
			InterPortletMessageManager interPortletMessageManager) {
		this.interPortletMessageManager = interPortletMessageManager;
	}

}
