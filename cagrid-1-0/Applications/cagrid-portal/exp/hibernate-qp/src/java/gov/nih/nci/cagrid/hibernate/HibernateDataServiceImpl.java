/**
 * 
 */
package gov.nih.nci.cagrid.hibernate;

import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
import gov.nih.nci.cagrid.data.service.DataServiceImpl;
import gov.nih.nci.cagrid.data.service.DataServiceInitializationException;

import java.rmi.RemoteException;

import org.apache.axis.MessageContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class HibernateDataServiceImpl extends DataServiceImpl {

	/**
	 * @throws DataServiceInitializationException
	 */
	public HibernateDataServiceImpl() throws DataServiceInitializationException {

	}
	
	public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults query(
			gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery)
			throws RemoteException,
			gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType,
			gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType {
		fireAuditQueryBegins(cqlQuery);

		try {
			preProcess(cqlQuery);
		} catch (MalformedQueryException ex) {
			throw (MalformedQueryExceptionType) getTypedException(ex,
					new MalformedQueryExceptionType());
		} catch (QueryProcessingException ex) {
			throw (QueryProcessingExceptionType) getTypedException(ex,
					new QueryProcessingExceptionType());
		}

		// process the query
		CQLQueryProcessor processor = null;
		try {
			WebApplicationContext wac = (WebApplicationContext) MessageContext
			.getCurrentContext()
			.getProperty(
					WebApplicationContextFilter.WEB_APPLICATION_CONTEXT_PROPERTY);
			processor = (CQLQueryProcessor)wac.getBean("cqlQueryProcessor");
		} catch (Exception ex) {
			throw (QueryProcessingExceptionType) getTypedException(ex,
					new QueryProcessingExceptionType());
		}

		try {
			CQLQueryResults results = processor.processQuery(cqlQuery);
			fireAuditQueryResults(cqlQuery, results);
			return results;
		} catch (gov.nih.nci.cagrid.data.QueryProcessingException ex) {
			fireAuditQueryProcessingFailure(cqlQuery, ex);
			throw (QueryProcessingExceptionType) getTypedException(ex,
					new QueryProcessingExceptionType());
		} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {
			throw (MalformedQueryExceptionType) getTypedException(ex,
					new MalformedQueryExceptionType());
		}
	}	

}
