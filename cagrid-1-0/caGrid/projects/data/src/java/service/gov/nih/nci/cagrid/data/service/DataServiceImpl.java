package gov.nih.nci.cagrid.data.service;

import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;

import java.io.InputStream;
import java.rmi.RemoteException;

import org.apache.axis.utils.ClassUtils;

/** 
 *  gov.nih.nci.cagrid.dataI
 *  TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class DataServiceImpl extends BaseServiceImpl {
	
	public DataServiceImpl() throws RemoteException {
		super();
	}
	
	
	public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults query(gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery) 
		throws RemoteException, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType {
		preProcess(cqlQuery);
		
		// process the query
		gov.nih.nci.cagrid.data.cql.CQLQueryProcessor processor = null;
		try {
			processor = getCqlQueryProcessorInstance();
			InputStream configStream = ClassUtils.getResourceAsStream(
				getClass(), "server-config.wsdd");
			processor.initialize(getCqlQueryProcessorConfig(), configStream);
		} catch (Exception ex) {
			throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
		}
		try {
			return processor.processQuery(cqlQuery);
		} catch (gov.nih.nci.cagrid.data.QueryProcessingException ex) {
			throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
		} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {
			throw (MalformedQueryExceptionType) getTypedException(ex, new MalformedQueryExceptionType());
		}
	}	
}

