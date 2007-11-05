/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.model;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.domain.GridDataService;
import gov.nih.nci.cagrid.portal2.portlet.AbstractActionResponseHandlerCommandController;
import gov.nih.nci.cagrid.portal2.portlet.query.QueryModel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectServiceController extends
		AbstractActionResponseHandlerCommandController {
	
	private GridServiceDao gridServiceDao;
	private QueryModel queryModel;

	/**
	 * 
	 */
	public SelectServiceController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectServiceController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectServiceController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		SelectServiceCommand command = (SelectServiceCommand) obj;
		GridDataService selectedService = null;
		try {
			selectedService = (GridDataService) getGridServiceDao().getByUrl(
					command.getServiceUrl());
		} catch (ClassCastException ex) {
			errors.rejectValue("serviceUrl", "error.serviceUrl.notDataService",
					new String[] { command.getServiceUrl() }, "The URL "
							+ command.getServiceUrl()
							+ " does not point to a data service.");
			logger.error(ex);
			return;
		}
		if (selectedService == null) {
			errors.rejectValue("serviceUrl", "error.serviceUrl.notFound",
					new String[] { command.getServiceUrl() },
					"No service found for URL " + command.getServiceUrl());
			logger.error("No service found for URL " + command.getServiceUrl());
			return;
		}
		if(selectedService != null){
			logger.debug("Selecting service " + selectedService.getUrl());
			getQueryModel().setSelectedService(selectedService);
		}
	}
	
	@Required
	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	@Required
	public QueryModel getQueryModel() {
		return queryModel;
	}

	public void setQueryModel(QueryModel queryModel) {
		this.queryModel = queryModel;
	}
}
