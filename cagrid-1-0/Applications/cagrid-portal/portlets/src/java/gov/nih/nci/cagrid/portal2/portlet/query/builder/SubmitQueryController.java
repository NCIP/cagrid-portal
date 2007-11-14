/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.builder;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.portal2.dao.CQLQueryDao;
import gov.nih.nci.cagrid.portal2.dao.CQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal2.domain.GridDataService;
import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal2.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal2.portlet.PortletConstants;
import gov.nih.nci.cagrid.portal2.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal2.util.PortalUtils;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SubmitQueryController extends AbstractQueryActionController {

	private GridServiceDao gridServiceDao;

	private PortalUserDao portalUserDao;
	private CQLQueryDao cqlQueryDao;
	private CQLQueryInstanceDao cqlQueryInstanceDao;


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
					errors.rejectValue("cqlQuery",
							PortletConstants.BAD_CQL_MSG, null,
							"Could not parse CQL query.");
				}

				if (!errors.hasErrors()) {
					try {
						submitQuery(getQueryModel().getPortalUser(), dataService, command.getCqlQuery());
					} catch (Exception ex) {
						logger.error(ex);
						errors.reject(
								PortletConstants.CQL_QUERY_SUBMIT_ERROR_MSG,
								new String[] { ex.getMessage() },
								"Error submitting CQL query.");
					}
				}
			}
		}
	}
	
	private void submitQuery(PortalUser user, GridDataService service, String cql){
		logger.debug("Submitted Query: "+ cql);
		
		String hash = PortalUtils.createHash(cql);
		CQLQuery query = getCqlQueryDao().getByHash(hash);

		if (query == null) {
			query = new CQLQuery();
			query.setXml(cql);
			query.setHash(hash);
			getCqlQueryDao().save(query);
		}

		CQLQueryInstance inst = new CQLQueryInstance();
		inst.setDataService(service);
		if (user != null) {
			inst.setPortalUser(user);
		}
		inst.setQuery(query);
		getCqlQueryInstanceDao().save(inst);

		query.getInstances().add(inst);
		getCqlQueryDao().save(query);

		if (user != null) {
			PortalUser p = getPortalUserDao().getById(user.getId());
			p.getQueryInstances().add(inst);
			getPortalUserDao().save(p);
		}
		getQueryModel().submitCqlQuery(inst);
	}

	@Required
	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}


	@Required
	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

	@Required
	public CQLQueryDao getCqlQueryDao() {
		return cqlQueryDao;
	}

	public void setCqlQueryDao(CQLQueryDao cqlQueryDao) {
		this.cqlQueryDao = cqlQueryDao;
	}

	@Required
	public CQLQueryInstanceDao getCqlQueryInstanceDao() {
		return cqlQueryInstanceDao;
	}

	public void setCqlQueryInstanceDao(CQLQueryInstanceDao cqlQueryInstanceDao) {
		this.cqlQueryInstanceDao = cqlQueryInstanceDao;
	}

}
