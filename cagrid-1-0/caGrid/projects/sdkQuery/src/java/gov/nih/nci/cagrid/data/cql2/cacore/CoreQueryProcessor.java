package gov.nih.nci.cagrid.data.cql2.cacore;

import gov.nih.nci.cagrid.cqlquery.CQLQueryType;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.cql.InitializationException;
import gov.nih.nci.cagrid.data.cql.MalformedQueryException;

/** 
 *  CoreQueryProcessor
 *  Processes CQL v2 queries against a caCORE data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 26, 2006 
 * @version $Id$ 
 */
public class CoreQueryProcessor extends CQLQueryProcessor {

	public CoreQueryProcessor(String initString) throws InitializationException {
		super(initString);
	}


	public CQLQueryResultsType processQuery(CQLQueryType cqlQuery) throws MalformedQueryException, Exception {
		
		return null;
	}
}
