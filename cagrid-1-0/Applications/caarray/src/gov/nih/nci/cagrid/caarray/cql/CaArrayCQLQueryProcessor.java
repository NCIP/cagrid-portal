package gov.nih.nci.cagrid.caarray.cql;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.mapping.Mappings;
import gov.nih.nci.cagrid.data.service.ServiceConfigUtil;
import gov.nih.nci.cagrid.data.utilities.CQLResultsCreationUtil;
import gov.nih.nci.cagrid.data.utilities.ResultsCreationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class CaArrayCQLQueryProcessor extends CQLQueryProcessor {

	public CQLQueryResults processQuery(CQLQuery cqlQuery) throws MalformedQueryException, QueryProcessingException {
		List coreResultsList = queryCaArrayService(cqlQuery);
		String targetName = cqlQuery.getTarget().getName();
		Mappings mappings = null;
		try {
			mappings = getClassToQnameMappings();
		} catch (Exception ex) {
			throw new QueryProcessingException("Error getting class to qname mappings: " + ex.getMessage(), ex);
		}
		CQLQueryResults results = null;
		// decide on type of results
		boolean objectResults = cqlQuery.getQueryModifier() == null ||
			(!cqlQuery.getQueryModifier().isCountOnly() 
				&& cqlQuery.getQueryModifier().getAttributeNames() == null 
				&& cqlQuery.getQueryModifier().getDistinctAttribute() == null);
		if (objectResults) {
			try {
				results = CQLResultsCreationUtil.createObjectResults(coreResultsList, targetName, mappings);
			} catch (ResultsCreationException ex) {
				throw new QueryProcessingException(ex.getMessage(), ex);
			}
		} else {
			QueryModifier mod = cqlQuery.getQueryModifier();
			if (mod.isCountOnly()) {
				// parse the value as a string to long.  This covers returning
				// integers, shorts, and longs
				Long val = Long.valueOf(coreResultsList.get(0).toString());
				results = CQLResultsCreationUtil.createCountResults(val.longValue(), targetName);
			} else {
				// attributes distinct or otherwise
				String[] names = null;
				if (mod.getDistinctAttribute() != null) {
					names = new String[] {mod.getDistinctAttribute()};
				} else {
					names = mod.getAttributeNames();
				}
				results = CQLResultsCreationUtil.createAttributeResults(
					coreResultsList, targetName, names);
			}
		}
		return results;
	}
	private List queryCaArrayService(CQLQuery cqlQuery) {
		List results = new ArrayList();
		
		return results;
	}
	private Mappings getClassToQnameMappings() throws Exception {
		// get the mapping file name
		String filename = ServiceConfigUtil.getClassToQnameMappingsFile();
		Mappings mappings = (Mappings) Utils.deserializeDocument(filename, Mappings.class);
		return mappings;
	}


	public Properties getRequiredParameters() {
		// TODO Auto-generated method stub
		return new Properties();
	}
}
