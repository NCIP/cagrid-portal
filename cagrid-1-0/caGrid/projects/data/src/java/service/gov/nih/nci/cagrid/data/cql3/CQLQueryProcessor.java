package gov.nih.nci.cagrid.data.cql3;

import gov.nih.nci.cagrid.cqlquery3.NestedObjectType;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;

/** 
 *  CQLQueryProcessor
 *  Abstract class the service providers must extend to process 
 *  CQL Queries to a caGrid data service 
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 2, 2006 
 * @version $Id$ 
 */
public abstract class CQLQueryProcessor {

	private String initString;

	public CQLQueryProcessor(String initString) throws InitializationException {
		this.initString = initString;
	}
	
	
	protected String getInitString() {
		return this.initString;
	}
	
	
	public abstract CQLQueryResultsType processQuery(NestedObjectType cqlQuery) 
		throws MalformedQueryException, QueryProcessingException;
}
