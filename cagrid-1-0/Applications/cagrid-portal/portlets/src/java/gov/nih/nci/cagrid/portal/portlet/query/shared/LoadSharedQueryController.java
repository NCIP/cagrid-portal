/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.shared;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

import gov.nih.nci.cagrid.portal.dao.SharedCQLQueryDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class LoadSharedQueryController extends AbstractQueryActionController {

	
	private SharedCQLQueryDao sharedCqlQueryDao;
	
	/**
	 * 
	 */
	public LoadSharedQueryController() {

	}

	/**
	 * @param commandClass
	 */
	public LoadSharedQueryController(Class commandClass) {
		super(commandClass);
	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public LoadSharedQueryController(Class commandClass, String commandName) {
		super(commandClass, commandName);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		SelectSharedQueryCommand command = (SelectSharedQueryCommand)obj;
		SharedCQLQuery query = getSharedCqlQueryDao().getById(command.getQueryId());
		CQLQueryCommand workingQuery = new CQLQueryCommand();
		workingQuery.setCqlQuery(query.getCqlQuery().getXml());
		workingQuery.setDataServiceUrl(query.getTargetService().getUrl());
		getQueryModel().setWorkingQuery(workingQuery);
	}
	
	public SharedCQLQueryDao getSharedCqlQueryDao() {
		return sharedCqlQueryDao;
	}

	public void setSharedCqlQueryDao(SharedCQLQueryDao sharedCqlQueryDao) {
		this.sharedCqlQueryDao = sharedCqlQueryDao;
	}

}
