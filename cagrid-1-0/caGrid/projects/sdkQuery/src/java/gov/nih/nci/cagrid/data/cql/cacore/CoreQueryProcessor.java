package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsUtil;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

/** 
 *  CoreQueryProcessor
 *  CQL Query processor implementation for SDK generated data sources
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 2, 2006 
 * @version $Id$ 
 */
public class CoreQueryProcessor extends CQLQueryProcessor {
	public static final String APPLICATION_SERVICE_URL = "appservice.url";
	
	private ApplicationService coreService;
	
	public CoreQueryProcessor() {
		super();
		
	}
	
	
	public void initialize(Map properties) throws InitializationException {
		String url = (String) properties.get(APPLICATION_SERVICE_URL);
		if (url == null || url.length() == 0) {
			// TODO: change this... you don't have the configuration file that locates
			// the default remote application service on cabio
			coreService = ApplicationService.getRemoteInstance();
		} else {
			coreService = ApplicationService.getRemoteInstance(url);
		}
	}
	

	public CQLQueryResults processQuery(CQLQuery cqlQuery) 
		throws MalformedQueryException, QueryProcessingException {
		DetachedCriteria objectCriteria = CQL2DetachedCriteria.translate(cqlQuery);
		
		List targetObjects = null;
		try {
			targetObjects = coreService.query(objectCriteria, cqlQuery.getTarget().getName());
		} catch (Exception ex) {
			throw new QueryProcessingException("Error invoking core query method: " + ex.getMessage(), ex);
		}
		CQLQueryResults results = CQLQueryResultsUtil.createQueryResults(targetObjects);
		return results;
	}
	
	
	public Map getRequiredParameters() {
		Map params = new HashMap();
		params.put(APPLICATION_SERVICE_URL, null);
		return params;
	}
}
