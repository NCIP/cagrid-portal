package gov.nih.nci.cagrid.data.common;

import gov.nih.nci.cagrid.cqlquery.CQLQueryType;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType;

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
	public static final String CQL_QUERY_TYPE = CQLQueryType.class.getName();
	public static final String CQL_RESULT_SET_TYPE = CQLQueryResultsType.class.getName();

	private DataServiceConstants() {
		
	}
}
