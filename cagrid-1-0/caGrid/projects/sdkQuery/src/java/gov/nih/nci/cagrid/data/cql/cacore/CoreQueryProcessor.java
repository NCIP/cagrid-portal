package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.LazyCQLQueryProcessor;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsUtil;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

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
public class CoreQueryProcessor extends LazyCQLQueryProcessor {
	public static final String APPLICATION_SERVICE_URL = "appserviceUrl";
	
	private ApplicationService coreService;
	private StringBuffer wsddContents; 
	
	public CoreQueryProcessor() {
		super();
	}
	
	
	private InputStream getWsdd() throws Exception {
		if (getConfiguredWsddStream() != null) {
			if (wsddContents == null) {
				wsddContents = Utils.inputStreamToStringBuffer(getConfiguredWsddStream());
			}
			return new ByteArrayInputStream(wsddContents.toString().getBytes());
		} else {
			return null;
		}
	}
	

	public CQLQueryResults processQuery(CQLQuery cqlQuery) 
		throws MalformedQueryException, QueryProcessingException {
		InputStream configStream = null;
		try {
			configStream = getWsdd();
		} catch (Exception ex) {
			throw new QueryProcessingException(ex);
		}
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
		if (coreService == null) {
			String url = getConfiguredParameters().getProperty(APPLICATION_SERVICE_URL);
			if (url == null || url.length() == 0) {
				throw new QueryProcessingException(
					"Required parameter " + APPLICATION_SERVICE_URL + " was not defined!");
			}
			coreService = ApplicationService.getRemoteInstance(url);
		}
		DetachedCriteria objectCriteria = CQL2DetachedCriteria.translate(query);
		List targetObjects = null;
		try {
			targetObjects = coreService.query(objectCriteria, query.getTarget().getName());
		} catch (Exception ex) {
			throw new QueryProcessingException("Error invoking core query method: " + ex.getMessage(), ex);
		}
		return targetObjects;
	}
	
	
	public Properties getRequiredParameters() {
		Properties params = new Properties();
		params.setProperty(APPLICATION_SERVICE_URL, "http://localhost:8080/cacore31/server/HTTPServer");
		return params;
	}
}
