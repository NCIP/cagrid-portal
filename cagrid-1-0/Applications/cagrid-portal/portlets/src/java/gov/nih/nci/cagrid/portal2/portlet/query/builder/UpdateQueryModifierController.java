/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.builder;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

import gov.nih.nci.cagrid.portal2.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal2.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeFacade;

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

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		EditQueryModifierCommand command = (EditQueryModifierCommand) obj;
		if("cancel".equals(command.getEditOperation())){
			return;
		}
		
		CQLQueryBean cqlQueryBean = (CQLQueryBean) getCqlQueryTreeFacade()
				.getRootNode().getContent();
		if ("update".equals(command.getEditOperation())) {
			cqlQueryBean.setModifierType(command.getModifierType());
			cqlQueryBean.getSelectedAttributes().clear();
			if (cqlQueryBean.getSelectedAttributes() != null) {
				cqlQueryBean.getSelectedAttributes().addAll(
						command.getSelectedAttributes());
			}
		} else if ("delete".equals(command.getEditOperation())) {
			cqlQueryBean.setModifierType(null);
			cqlQueryBean.getSelectedAttributes().clear();
		}else{
			throw new CaGridPortletApplicationException("Invalid edit command: " + command.getEditOperation());
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
