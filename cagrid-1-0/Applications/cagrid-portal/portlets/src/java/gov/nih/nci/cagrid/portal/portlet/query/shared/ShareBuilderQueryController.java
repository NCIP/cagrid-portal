/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.shared;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.springframework.validation.BindException;

import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ShareBuilderQueryController extends ShareQueryController {
	
	private TreeFacade cqlQueryTreeFacade;

	/**
	 * 
	 */
	public ShareBuilderQueryController() {

	}

	/**
	 * @param commandClass
	 */
	public ShareBuilderQueryController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public ShareBuilderQueryController(Class commandClass, String commandName) {
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

	public TreeFacade getCqlQueryTreeFacade() {
		return cqlQueryTreeFacade;
	}

	public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
		this.cqlQueryTreeFacade = cqlQueryTreeFacade;
	}

}
