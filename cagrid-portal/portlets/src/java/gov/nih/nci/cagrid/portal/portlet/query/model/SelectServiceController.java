/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.model;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import gov.nih.nci.cagrid.portal.portlet.util.XSSFilterEditor;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectServiceController extends
		AbstractActionResponseHandlerCommandController {

	private GridServiceDao gridServiceDao;
	private UserModel userModel;

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

	protected void initBinder(PortletRequest request,
			PortletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(String.class, "dataServiceUrl",
				new XSSFilterEditor(binder.getBindingResult(), "dataServiceUrl"));
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
		if (errors.hasErrors()) {
			return;
		}

		SelectServiceCommand command = (SelectServiceCommand) obj;

		GridDataService selectedService = (GridDataService) getGridServiceDao()
				.getByUrl(command.getDataServiceUrl());
		logger.debug("Selecting service " + selectedService.getUrl());
		getUserModel().setSelectedService(selectedService);
	}

	@Required
	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	@Required
	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}
}
