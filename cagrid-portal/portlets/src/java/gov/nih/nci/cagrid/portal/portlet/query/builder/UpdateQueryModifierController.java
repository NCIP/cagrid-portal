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
package gov.nih.nci.cagrid.portal.portlet.query.builder;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.QueryModifierType;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class UpdateQueryModifierController extends
		AbstractQueryActionController {

	private TreeFacade cqlQueryTreeFacade;

	/**
	 * 
	 */
	public UpdateQueryModifierController() {

	}

	/**
	 * @param commandClass
	 */
	public UpdateQueryModifierController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public UpdateQueryModifierController(Class commandClass, String commandName) {
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
		EditQueryModifierCommand command = (EditQueryModifierCommand) obj;
		if ("cancel".equals(command.getEditOperation())) {
			return;
		}

		CQLQueryBean cqlQueryBean = (CQLQueryBean) getCqlQueryTreeFacade()
				.getRootNode().getContent();
		if ("update".equals(command.getEditOperation())) {
			cqlQueryBean.setModifierType(command.getModifierType());
			cqlQueryBean.getSelectedAttributes().clear();
			if (QueryModifierType.DISTINCT_ATTRIBUTE.equals(command
					.getModifierType())
					|| QueryModifierType.SELECTED_ATTRIBUTES.equals(command
							.getModifierType())) {
				cqlQueryBean.getSelectedAttributes().addAll(
						command.getSelectedAttributes());
			}
		} else {
			throw new CaGridPortletApplicationException(
					"Invalid edit command: " + command.getEditOperation());
		}

	}

	@Required
	public TreeFacade getCqlQueryTreeFacade() {
		return cqlQueryTreeFacade;
	}

	public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
		this.cqlQueryTreeFacade = cqlQueryTreeFacade;
	}

}
