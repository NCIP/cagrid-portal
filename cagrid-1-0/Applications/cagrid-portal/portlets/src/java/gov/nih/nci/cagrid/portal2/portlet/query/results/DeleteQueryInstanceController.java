/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.results;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

import gov.nih.nci.cagrid.portal2.dao.CQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal2.portlet.query.AbstractQueryActionController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DeleteQueryInstanceController extends
		AbstractQueryActionController {

	private CQLQueryInstanceDao cqlQueryInstanceDao;
	
	/**
	 * 
	 */
	public DeleteQueryInstanceController() {

	}

	/**
	 * @param commandClass
	 */
	public DeleteQueryInstanceController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public DeleteQueryInstanceController(Class commandClass, String commandName) {
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
		CQLQueryInstance instance = getQueryModel().deleteQueryInstance(command.getInstanceId());
		getCqlQueryInstanceDao().delete(instance);
	}

	@Required
	public CQLQueryInstanceDao getCqlQueryInstanceDao() {
		return cqlQueryInstanceDao;
	}

	public void setCqlQueryInstanceDao(CQLQueryInstanceDao cqlQueryInstanceDao) {
		this.cqlQueryInstanceDao = cqlQueryInstanceDao;
	}

}
