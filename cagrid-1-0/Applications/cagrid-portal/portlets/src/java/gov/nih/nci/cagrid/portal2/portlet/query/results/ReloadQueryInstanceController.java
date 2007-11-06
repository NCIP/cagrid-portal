/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.results;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal2.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.CQLQueryCommand;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ReloadQueryInstanceController extends AbstractQueryActionController {

	/**
	 * 
	 */
	public ReloadQueryInstanceController() {

	}

	/**
	 * @param commandClass
	 */
	public ReloadQueryInstanceController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public ReloadQueryInstanceController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		SelectQueryInstanceCommand command = (SelectQueryInstanceCommand)obj;
		CQLQueryInstance instance = getQueryModel().getQueryInstance(command.getInstanceId());
		CQLQueryCommand workingQuery = new CQLQueryCommand();
		workingQuery.setCqlQuery(instance.getQuery().getXml());
		workingQuery.setDataServiceUrl(instance.getDataService().getUrl());
		getQueryModel().setWorkingQuery(workingQuery);
	}

}
