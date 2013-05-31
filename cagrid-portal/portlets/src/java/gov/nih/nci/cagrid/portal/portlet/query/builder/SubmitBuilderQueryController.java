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
		
		String xml = cqlQueryBean.toXml();
		logger.debug("XML: " + xml);
		command.setCqlQuery(xml);
		command.setDataServiceUrl(getUserModel().getSelectedService().getUrl());		
		
		super.doHandleAction(request, response, command, errors);
	}
	
	@Override
	protected Object getCommand(PortletRequest request) throws Exception {
		CQLQueryCommand command = getUserModel().getWorkingQuery();
		if (command == null) {
			command = new CQLQueryCommand();
			getUserModel().setWorkingQuery(command);
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
