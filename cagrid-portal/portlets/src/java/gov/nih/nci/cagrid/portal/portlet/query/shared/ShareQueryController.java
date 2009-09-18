/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.shared;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.PortletConstants;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ShareQueryController extends AbstractQueryActionController {

	private GridServiceDao gridServiceDao;

	/**
	 * 
	 */
	public ShareQueryController() {

	}

	/**
	 * @param commandClass
	 */
	public ShareQueryController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public ShareQueryController(Class commandClass, String commandName) {
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
		if (errors.hasErrors()) {
			return;
		}
		CQLQueryCommand command = (CQLQueryCommand) obj;
		getQueryModel().setWorkingQuery(command);

		GridDataService targetService = (GridDataService) getGridServiceDao()
				.getByUrl(command.getDataServiceUrl());
		String umlClassName = PortletUtils.getTargetUMLClassName(command
				.getCqlQuery());
		UMLClass targetClass = getQueryModel().getUmlClassDao()
				.getUmlClassFromModel(targetService.getDomainModel(),
						umlClassName);

		if (targetClass == null) {
			errors.rejectValue("cqlQuery", PortletConstants.INVALID_UML_CLASS,
					null, "No such UMLClass in DomainModel.");
			return;
		}

		SharedCQLQuery sharedQuery = new SharedCQLQuery();
		sharedQuery.setOwner(getQueryModel().getPortalUser());
		sharedQuery.setTargetService(targetService);
		sharedQuery.setTargetClass(targetClass);
		SharedQueryBean sharedQueryBean = new SharedQueryBean();
		sharedQueryBean.setQuery(sharedQuery);
		sharedQueryBean.setQueryCommand(command);
		getQueryModel().setWorkingSharedQuery(sharedQueryBean);
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

}
