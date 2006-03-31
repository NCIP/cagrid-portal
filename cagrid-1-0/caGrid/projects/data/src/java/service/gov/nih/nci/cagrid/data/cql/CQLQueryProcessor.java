package gov.nih.nci.cagrid.data.cql;

import gov.nih.nci.cagrid.cqlquery.CQLQueryType;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType;

/** 
 *  CQLQueryProcessor
 *  Interface for a service-side implementation of CQL
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 31, 2006 
 * @version $Id$ 
 */
public interface CQLQueryProcessor {
	
	public void init(String initString) throws InitializationException;
	

	public CQLQueryResultsType processQuery(CQLQueryType query) throws Exception;
}
