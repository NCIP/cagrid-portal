package gov.nih.nci.cagrid.data.common;


/** 
 *  DataServiceConstants
 *  Assorted constants for data services
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 31, 2006 
 * @version $Id$ 
 */
public class DataServiceConstants {

	public static final String CQL_QUERY_SCHEMA = "1_gov.nih.nci.cagrid.CQLQuery.xsd";
	public static final String CQL_RESULT_SET_SCHEMA = "3_gov.nih.nci.cagrid.CQLResultSet.xsd";
	public static final String CQL_QUERY_URI = "http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery";
	public static final String CQL_RESULT_SET_URI = "http://CQL.caBIG/3/gov.nih.nci.cagrid.CQLResultSet";
	public static final String CQL_QUERY_TYPE = gov.nih.nci.cagrid.cqlquery.Object.class.getName();
	public static final String CQL_RESULT_SET_TYPE = gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType.class.getName();
	public static final String QUERY_PROCESSOR_CLASS_PROPERTY = "QueryProcessorClass";
	
	// query method constants
	public static final String QUERY_METHOD_NAME = "query";
	public static final String QUERY_METHOD_RETURN_TYPE = CQL_RESULT_SET_TYPE;
	public static final String QUERY_METHOD_PARAMETER_TYPE = CQL_QUERY_TYPE;
	public static final String QUERY_METHOD_PARAMETER_NAME = "cqlQuery";
	/*
	public static final String[] QUERY_METHOD_EXCEPTIONS = new String[] {
		QueryProcessingException.class.getName(), MalformedQueryException.class.getName()
	};
	*/
	// TODO: this goes away when introduce supports using typed exceptions from packages
	// outside of the generated source tree
	public static final String[] QUERY_METHOD_EXCEPTIONS = new String[] {
		"QueryProcessingException", "MalformedQueryException"
	};
	public static final String QUERY_PROCESSOR_ELEMENT_NAME = "queryProcessorClass";

	private DataServiceConstants() {
		
	}
}
