/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.domain.GridDataService;
import gov.nih.nci.cagrid.portal2.portlet.PortletConstants;
import gov.nih.nci.cagrid.portal2.portlet.SharedApplicationModel;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CQLQueryController extends AbstractController {

	private String viewName;

	private String commandName;
	
	private GridServiceDao gridServiceDao;
	private CQLQueryService cqlQueryService;
	private SharedApplicationModel sharedApplicationModel;

	/**
	 * 
	 */
	public CQLQueryController() {

	}

	protected void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {

		logger.debug("Got action request");

		CQLQueryCommand command = getCommand(request);
		command.clear();
		command.setDataServiceUrl(request
				.getParameter(PortletConstants.DATA_SERVICE_URL_PARAM));
		command.setCqlQuery(request
				.getParameter(PortletConstants.CQL_QUERY_PARAM));

		logger.debug("url = " + command.getDataServiceUrl());

		try {
			String url = command.getDataServiceUrl().trim();
			new URL(url);
			command.setDataServiceUrl(url);
		} catch (Exception ex) {
			logger.debug("Error parsing URL: " + ex.getMessage(), ex);
			String msg = getMessage(PortletConstants.BAD_URL_MSG);
			command.setDataServiceUrlError(msg);
		}

		if (!command.hasErrors()) {

			GridDataService dataService = null;
			try {
				dataService = (GridDataService) getGridServiceDao().getByUrl(
						command.getDataServiceUrl().trim());
			} catch (Exception ex) {
				logger.error(
						"Error looking up GridDataService '"
								+ command.getDataServiceUrl() + "': "
								+ ex.getMessage(), ex);
			}

			if (dataService == null) {
				command
						.setDataServiceUrlError(getMessage(PortletConstants.NONEXSTING_DATA_SERVICE_MSG));
			}

			if (!command.hasErrors()) {

				try {
					gov.nih.nci.cagrid.cqlquery.CQLQuery query = (gov.nih.nci.cagrid.cqlquery.CQLQuery) Utils
							.deserializeObject(new StringReader(command
									.getCqlQuery()),
									gov.nih.nci.cagrid.cqlquery.CQLQuery.class);
					StringWriter w = new StringWriter();
					Utils.serializeObject(query,
							DataServiceConstants.CQL_QUERY_QNAME, w);
					String cql = w.toString();
					command.setCqlQuery(cql);
				} catch (Exception ex) {
					logger.debug("Error parsing CQL: " + ex.getMessage());
					String msg = getMessage(PortletConstants.BAD_CQL_MSG);
					command.setCqlQueryError(msg);
				}

				if (!command.hasErrors()) {
					try {
						getCqlQueryService().submitQuery(dataService, command.getCqlQuery());
					} catch (Exception ex) {
						String msg = getMessage(PortletConstants.CQL_QUERY_SUBMIT_ERROR_MSG);
						command.setCqlQuerySubmitError(msg);
						logger.error("Error submitting query: "
								+ ex.getMessage(), ex);
					}
				}
			}
		}

		if (!command.hasErrors()) {
			command
					.setConfirmationMessage(getMessage(PortletConstants.CQL_QUERY_CONFIRM_MSG));
		} else {
			logger.debug("Errors encountered.");
		}

	}

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		logger.debug("Got render request.");

		ModelAndView mav = new ModelAndView(getViewName());

		CQLQueryCommand command = getCommand(request);
		
		Integer id = getSharedApplicationModel().getSelectedGridDataServiceId();
		if(id != null){
			GridDataService svc = (GridDataService) getGridServiceDao().getById(id);
			if(!svc.getUrl().equals(command.getDataServiceUrl())){
				command.clear();
				command.setDataServiceUrl(svc.getUrl());
			}
		}
		
		mav.addObject(getCommandName(), command);

		return mav;
	}

	

	private String getMessage(String msgName) {
		return getApplicationContext().getMessage(msgName, null,
				Locale.getDefault());
	}

	private CQLQueryCommand getCommand(PortletRequest request) {
//		CQLQueryCommand command = (CQLQueryCommand) request.getPortletSession()
//				.getAttribute(getCommandName());
		CQLQueryCommand command = getSharedApplicationModel().getWorkingCqlQuery();
		if (command == null) {
			command = new CQLQueryCommand();
//			request.getPortletSession().setAttribute(getCommandName(), command);
			getSharedApplicationModel().setWorkingCqlQuery(command);
		}
		return command;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public CQLQueryService getCqlQueryService() {
		return cqlQueryService;
	}

	public void setCqlQueryService(CQLQueryService cqlQueryService) {
		this.cqlQueryService = cqlQueryService;
	}

	public SharedApplicationModel getSharedApplicationModel() {
		return sharedApplicationModel;
	}

	public void setSharedApplicationModel(
			SharedApplicationModel sharedApplicationModel) {
		this.sharedApplicationModel = sharedApplicationModel;
	}

	

}
