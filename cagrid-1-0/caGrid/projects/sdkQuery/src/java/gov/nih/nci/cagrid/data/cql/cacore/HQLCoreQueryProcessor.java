package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.LazyCQLQueryProcessor;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsUtil;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/** 
 *  HQLCoreQueryProcessor
 *  Implementation of CQL against a caCORE data source using HQL queries
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 2, 2006 
 * @version $Id$ 
 */
public class HQLCoreQueryProcessor extends LazyCQLQueryProcessor {
	public static final String DEFAULT_LOCALHOST_CACORE_URL = "http://localhost:8080/cacore31/server/HTTPServer";
	public static final String APPLICATION_SERVICE_URL = "appserviceUrl";
	
	private static Logger LOG = Logger.getLogger(HQLCoreQueryProcessor.class);
	
	private ApplicationService coreService;
	private InputStream configStream;
	
	public HQLCoreQueryProcessor() {
		super();
	}
	
	
	public void initialize(Map properties) throws InitializationException {
		String url = (String) properties.get(APPLICATION_SERVICE_URL);
		if (url == null || url.length() == 0) {
			throw new InitializationException(
				"Required parameter " + APPLICATION_SERVICE_URL + " was not defined!");
		}
		configStream = (InputStream) properties.get(AXIS_WSDD_CONFIG_STREAM);
		coreService = ApplicationService.getRemoteInstance(url);
	}
	

	public CQLQueryResults processQuery(CQLQuery cqlQuery) 
		throws MalformedQueryException, QueryProcessingException {
		List coreResultsList = queryCoreService(cqlQuery);		
		CQLQueryResults results = CQLQueryResultsUtil.createQueryResults(coreResultsList, configStream);
		return results;
	}
	
	
	public Iterator processQueryLazy(CQLQuery cqlQuery) 
		throws MalformedQueryException, QueryProcessingException {
		List coreResultsList = queryCoreService(cqlQuery);
		return coreResultsList.iterator();
	}
	
	
	private List queryCoreService(CQLQuery query) 
		throws MalformedQueryException, QueryProcessingException {
		HQLCriteria hqlCriteria = new HQLCriteria(CQL2HQL.translate(query));
		LOG.debug("Executing HQL:" + hqlCriteria.getHqlString());
		List targetObjects = null;
		try {
			targetObjects = coreService.query(hqlCriteria, query.getTarget().getName());
		} catch (Exception ex) {
			throw new QueryProcessingException("Error invoking core query method: " + ex.getMessage(), ex);
		}
		return targetObjects;
	}
	
	
	public Map getRequiredParameters() {
		Map params = new HashMap();
		params.put(APPLICATION_SERVICE_URL, DEFAULT_LOCALHOST_CACORE_URL);
		return params;
	}
}
