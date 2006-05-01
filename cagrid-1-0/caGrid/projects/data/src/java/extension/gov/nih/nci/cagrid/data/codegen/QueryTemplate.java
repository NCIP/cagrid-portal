package gov.nih.nci.cagrid.data.codegen;

/** 
 *  QueryTemplate
 *  Template for the query method of a caGrid data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 1, 2006 
 * @version $Id$ 
 */
public class QueryTemplate {

	public gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType query(gov.nih.nci.cagrid.cqlquery.CQLQueryType cqlQuery) 
		throws gov.nih.nci.cagrid.data.QueryProcessingException, gov.nih.nci.cagrid.data.MalformedQueryException {
		String implClassName = "%%processorClass%%";
		Class implClass = null;
		try {
			implClass = Class.forName(implClassName);
		} catch (Exception ex) {
			throw new gov.nih.nci.cagrid.data.QueryProcessingException(
				"Error getting query processing implementation class: " + ex.getMessage(), ex);
		}
		gov.nih.nci.cagrid.data.cql.CQLQueryProcessor processor = null;
		try {
			processor = (gov.nih.nci.cagrid.data.cql.CQLQueryProcessor) implClass.newInstance();
		} catch (Exception ex) {
			throw new gov.nih.nci.cagrid.data.QueryProcessingException(
				"Error instantiating processor implementation: " + ex.getMessage(), ex);
		}
		return processor.processQuery(cqlQuery);
	}
}
