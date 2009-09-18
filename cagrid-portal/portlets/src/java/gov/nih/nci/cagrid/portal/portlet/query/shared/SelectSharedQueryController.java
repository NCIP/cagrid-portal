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

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectSharedQueryController extends AbstractQueryActionController {

	private SharedCQLQueryDao sharedCqlQueryDao;

	/**
	 * 
	 */
	public SelectSharedQueryController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectSharedQueryController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectSharedQueryController(Class commandClass, String commandName) {
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
		SelectSharedQueryCommand command = (SelectSharedQueryCommand) obj;
		SharedCQLQuery query = getSharedCqlQueryDao().getById(
				command.getQueryId());
		if (query == null) {
			getQueryModel().setWorkingSharedQuery(null);
		} else {
			SharedQueryBean bean = new SharedQueryBean();
			bean.setQuery(query);
			getQueryModel().setWorkingSharedQuery(bean);
		}
	}

	public SharedCQLQueryDao getSharedCqlQueryDao() {
		return sharedCqlQueryDao;
	}

	public void setSharedCqlQueryDao(SharedCQLQueryDao sharedCqlQueryDao) {
		this.sharedCqlQueryDao = sharedCqlQueryDao;
	}

}
