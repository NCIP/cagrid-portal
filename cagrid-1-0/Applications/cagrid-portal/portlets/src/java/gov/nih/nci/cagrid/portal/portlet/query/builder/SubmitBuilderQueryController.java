/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SubmitBuilderQueryController extends SubmitQueryController {
	
	private TreeFacade cqlQueryTreeFacade;

	/**
	 * 
	 */
	public SubmitBuilderQueryController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param commandClass
	 */
	public SubmitBuilderQueryController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SubmitBuilderQueryController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}
	
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		CQLQueryCommand command = (CQLQueryCommand)obj;
		CQLQueryBean cqlQueryBean = (CQLQueryBean)getCqlQueryTreeFacade().getRootNode().getContent();
		command.setCqlQuery(cqlQueryBean.toXml());
		command.setDataServiceUrl(getQueryModel().getSelectedService().getUrl());		
		
		super.doHandleAction(request, response, command, errors);
	}
	
	@Override
	protected Object getCommand(PortletRequest request) throws Exception {
		CQLQueryCommand command = getQueryModel().getWorkingQuery();
		if (command == null) {
			command = new CQLQueryCommand();
			getQueryModel().setWorkingQuery(command);
		}
		return command;
	}

	@Required
	public TreeFacade getCqlQueryTreeFacade() {
		return cqlQueryTreeFacade;
	}

	public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
		this.cqlQueryTreeFacade = cqlQueryTreeFacade;
	}

}
