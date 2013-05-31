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
package gov.nih.nci.cagrid.portal.portlet.discovery.details;

import gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController;
import gov.nih.nci.cagrid.portal.portlet.InterPortletMessageSender;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectServiceForQueryController extends
		AbstractActionResponseHandlerCommandController {

	private InterPortletMessageSender interPortletMessageSender;

	/**
	 * 
	 */
	public SelectServiceForQueryController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectServiceForQueryController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectServiceForQueryController(Class commandClass,
			String commandName) {
		super(commandClass, commandName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {

		SelectDetailsCommand command = (SelectDetailsCommand) obj;
		getInterPortletMessageSender().send(request, command.getSelectedId());
	}

	public InterPortletMessageSender getInterPortletMessageSender() {
		return interPortletMessageSender;
	}

	public void setInterPortletMessageSender(
			InterPortletMessageSender interPortletMessageSender) {
		this.interPortletMessageSender = interPortletMessageSender;
	}

}
