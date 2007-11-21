/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.PortletConstants;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CQLQueryCommandValidator implements Validator {
	
	private GridServiceDao gridServiceDao;

	/**
	 * 
	 */
	public CQLQueryCommandValidator() {

	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class klass) {
		return CQLQueryCommand.class.isAssignableFrom(klass);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		CQLQueryCommand command = (CQLQueryCommand)obj;
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
		
		GridDataService dataService = null;
		try {
			dataService = (GridDataService) getGridServiceDao().getByUrl(
					command.getDataServiceUrl().trim());
		} catch (ClassCastException ex) {
			errors.rejectValue("dataServiceUrl",
					PortletConstants.WRONG_SERVICE_TYPE,
					new String[] { command.getDataServiceUrl() }, command
							.getDataServiceUrl()
							+ " doesn't point to a data service.");
		}
		
		if (!errors.hasErrors() && dataService == null) {
			errors.rejectValue("dataServiceUrl",
					PortletConstants.NONEXSTING_DATA_SERVICE_MSG,
					new String[] { command.getDataServiceUrl() },
					"No service found for url: "
							+ command.getDataServiceUrl());
		}
		
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
			errors.rejectValue("cqlQuery",
					PortletConstants.BAD_CQL_MSG, null,
					"Could not parse CQL query.");
		}
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

}
