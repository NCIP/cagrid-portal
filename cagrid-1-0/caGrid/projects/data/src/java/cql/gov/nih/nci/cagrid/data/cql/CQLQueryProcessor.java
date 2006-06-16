package gov.nih.nci.cagrid.data.cql;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;

import java.util.Map;

/** 
 *  CQLQueryProcessor
 *  Abstract class the service providers must extend to process 
 *  CQL Queries to a caGrid data service 
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 25, 2006 
 * @version $Id$ 
 */
public abstract class CQLQueryProcessor {
	
	public CQLQueryProcessor() {
	
	}
	
	
	/**
	 * Initialize the query processor with the properties it requires as specified
	 * in the map provided by getRequiredParameters()
	 * @param configuration
	 * @throws InitializationException
	 */
	public abstract void initialize(Map configuration) throws InitializationException;
	
	
	/**
	 * Processes the CQL Query
	 * @param cqlQuery
	 * @return
	 * @throws MalformedQueryException
	 * 		Should be thrown when the query itself does not conform to the
	 * 		CQL standard or attempts to perform queries outside of 
	 * 		the exposed domain model
	 * @throws QueryProcessingException
	 * 		Thrown for all exceptions in query processing not related
	 * 		to the query being malformed
	 */
	public abstract CQLQueryResults processQuery(CQLQuery cqlQuery) 
		throws MalformedQueryException, QueryProcessingException;
	
	
	/**
	 * Get a Map of parameters the query processor will require on startup.
	 * The keys are the parameters required, the values are defaults, or NULL
	 * if the parameter is not required but optional.  The keys MUST start with 
	 * a capital letter, and must NOT contain spaces or punctuation
	 * @return
	 */
	public abstract Map getRequiredParameters();
}
