package gov.nih.nci.cagrid.data.sdk32query.experiemental;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.mapping.Mappings;
import gov.nih.nci.cagrid.data.service.ServiceConfigUtil;
import gov.nih.nci.cagrid.data.utilities.CQLResultsCreationUtil;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.List;
import java.util.Properties;

/** 
 *  DirectCQLQueryProcessor
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Jan 22, 2007 
 * @version $Id: DirectCQLQueryProcessor.java,v 1.1 2007-01-24 19:21:03 dervin Exp $ 
 */
public class DirectCQLQueryProcessor extends CQLQueryProcessor {
	
	public static final String APPLICATION_SERVICE_URL = "appserviceUrl";

	public CQLQueryResults processQuery(CQLQuery cqlQuery) throws MalformedQueryException, QueryProcessingException {
		ApplicationService service = ApplicationService.getRemoteInstance(
			getConfiguredParameters().getProperty(APPLICATION_SERVICE_URL));
		
		gov.nih.nci.system.query.cql.CQLQuery convertedQuery = null;
		try {
			// convert REAL cql to appservice's fake CQL classes
			convertedQuery = CQL2CoreCQL.convert(cqlQuery);
		} catch (Exception ex) {
			throw new QueryProcessingException("Error converting query format: " + ex.getMessage(), ex);
		}
		
		List resultList = null;
		try {
			// run the query
			resultList = service.query(convertedQuery, convertedQuery.getTarget().getName());
		} catch (Exception ex) {
			throw new QueryProcessingException("Error executing query: " + ex.getMessage(), ex);
		}
		
		// result list to result set
		CQLQueryResults results = null;
		try {
			results = CQLResultsCreationUtil.createObjectResults(resultList,
				convertedQuery.getTarget().getName(), getClassToQnameMappings());
		} catch (Exception ex) {
			throw new QueryProcessingException("Error creating results: " + ex.getMessage(), ex);
		}
			
		return results;
	}
	
	
	public Properties getRequiredParameters() {
		Properties params = new Properties();
		params.setProperty(APPLICATION_SERVICE_URL, "");
		return params;
	}
	
	
	private Mappings getClassToQnameMappings() throws Exception {
		// get the mapping file name
		String filename = ServiceConfigUtil.getClassToQnameMappingsFile();
		Mappings mappings = (Mappings) Utils.deserializeDocument(filename, Mappings.class);
		return mappings;
	}
}
