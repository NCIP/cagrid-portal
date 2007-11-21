/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

import gov.nih.nci.cagrid.portal.dao.CQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectQueryInstanceController extends
		AbstractQueryActionController {

	private CQLQueryInstanceDao cqlQueryInstanceDao;

	/**
	 * 
	 */
	public SelectQueryInstanceController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectQueryInstanceController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectQueryInstanceController(Class commandClass, String commandName) {
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
		SelectQueryInstanceCommand command = (SelectQueryInstanceCommand) obj;
		CQLQueryInstance instance = getQueryModel().getQueryInstance(
				command.getInstanceId());
		if (instance == null) {
			// Will be null if created in previous http session
			instance = getCqlQueryInstanceDao()
					.getById(command.getInstanceId());
			if (instance == null) {
				throw new Exception("Couldn't find query instance with ID: "
						+ command.getInstanceId());
			}
		}
		getQueryModel().setSelectedQueryInstance(instance);
	}

	public CQLQueryInstanceDao getCqlQueryInstanceDao() {
		return cqlQueryInstanceDao;
	}

	public void setCqlQueryInstanceDao(CQLQueryInstanceDao cqlQueryInstanceDao) {
		this.cqlQueryInstanceDao = cqlQueryInstanceDao;
	}

}
