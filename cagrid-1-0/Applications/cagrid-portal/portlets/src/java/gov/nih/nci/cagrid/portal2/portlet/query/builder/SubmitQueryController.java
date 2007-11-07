/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.builder;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.domain.GridDataService;
import gov.nih.nci.cagrid.portal2.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal2.portlet.PortletConstants;
import gov.nih.nci.cagrid.portal2.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.CQLQueryService;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SubmitQueryController extends AbstractQueryActionController {

	private GridServiceDao gridServiceDao;
	private CQLQueryService cqlQueryService;

	/**
	 * 
	 */
	public SubmitQueryController() {

	}

	/**
	 * @param commandClass
	 */
	public SubmitQueryController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SubmitQueryController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {

		CQLQueryCommand command = (CQLQueryCommand) obj;
		
		getQueryModel().setWorkingQuery(command);
		
		logger.debug("url = " + command.getDataServiceUrl());
		try {
			String url = command.getDataServiceUrl().trim();
			new URL(url);
			command.setDataServiceUrl(url);
		} catch (Exception ex) {
			errors
					.rejectValue("dataServiceUrl",
							PortletConstants.BAD_URL_MSG,
							new String[] { command.getDataServiceUrl() },
							"Invalid URL");
		}

		if (!errors.hasErrors()) {

			GridDataService dataService = null;
			try {
				dataService = (GridDataService) getGridServiceDao().getByUrl(
						command.getDataServiceUrl().trim());
			} catch (ClassCastException ex) {
				logger.error(ex);
				errors.rejectValue("dataServiceUrl",
						PortletConstants.WRONG_SERVICE_TYPE,
						new String[] { command.getDataServiceUrl() }, command
								.getDataServiceUrl()
								+ " doesn't point to a data service.");
			} catch (Exception ex) {
				throw new CaGridPortletApplicationException(
						"Error looking up GridDataService '"
								+ command.getDataServiceUrl() + "': "
								+ ex.getMessage(), ex);
			}

			if (!errors.hasErrors() && dataService == null) {
				logger.error("didn't find data service");
				errors.rejectValue("dataServiceUrl",
						PortletConstants.NONEXSTING_DATA_SERVICE_MSG,
						new String[] { command.getDataServiceUrl() },
						"No service found for url: "
								+ command.getDataServiceUrl());
			}

			if (!errors.hasErrors()) {

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
					logger.error(ex);
					errors.rejectValue("cqlQuery", PortletConstants.BAD_CQL_MSG, null, "Could not parse CQL query.");
				}

				if (!errors.hasErrors()) {
					try {
						getCqlQueryService().submitQuery(dataService,
								command.getCqlQuery());
						
					} catch (Exception ex) {
						logger.error(ex);
						errors.reject(PortletConstants.CQL_QUERY_SUBMIT_ERROR_MSG, new String[]{ex.getMessage()}, "Error submitting CQL query.");
					}
				}
			}
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
	public CQLQueryService getCqlQueryService() {
		return cqlQueryService;
	}

	public void setCqlQueryService(CQLQueryService cqlQueryService) {
		this.cqlQueryService = cqlQueryService;
	}

}