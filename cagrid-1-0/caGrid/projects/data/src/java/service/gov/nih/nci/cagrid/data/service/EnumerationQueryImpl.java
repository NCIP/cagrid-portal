package gov.nih.nci.cagrid.data.service;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.cql.LazyCQLQueryProcessor;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.wsenum.utils.EnumerateResponseFactory;
import gov.nih.nci.cagrid.wsenum.utils.EnumerationCreationException;
import gov.nih.nci.cagrid.wsenum.utils.SimplePersistantSDKObjectIterator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.utils.ClassUtils;
import org.globus.ws.enumeration.EnumIterator;

/** 
 *  EnumerationQueryImpl
 *  Implementation of the EnumerationQuery
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class EnumerationQueryImpl extends BaseServiceImpl {

	
	public EnumerationQueryImpl() throws RemoteException {
		super();
	}
	
	
	public org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerateResponse enumerationQuery(
		gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery) throws RemoteException, 
		gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType, 
		gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType {
		preProcess(cqlQuery);
		
		CQLQueryProcessor processor = getCqlQueryProcessorInstance();
		EnumIterator enumIter = null;
		try {
			if (processor instanceof LazyCQLQueryProcessor) {
				enumIter = processLazyQuery((LazyCQLQueryProcessor) processor, cqlQuery);
			} else {
				enumIter = processQuery(processor, cqlQuery);
			}
		} catch (gov.nih.nci.cagrid.data.QueryProcessingException ex) {
			throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
		} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {
			throw (MalformedQueryExceptionType) getTypedException(ex, new MalformedQueryExceptionType());
		} catch (FileNotFoundException ex) {
			throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
		} catch (IOException ex) {
			throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
		}
		
		try {
			return EnumerateResponseFactory.createCustomResponse(enumIter, false);
		} catch (EnumerationCreationException ex) {
			throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
		}
	}	
	
	
	private EnumIterator processQuery(CQLQueryProcessor processor, CQLQuery query)
		throws gov.nih.nci.cagrid.data.QueryProcessingException, 
		gov.nih.nci.cagrid.data.MalformedQueryException,
		FileNotFoundException, IOException {
		// perform the query
		CQLQueryResults results = processor.processQuery(query);
		// pump the results into a list
		List resultList = new LinkedList();
		Iterator resIter = new CQLQueryResultsIterator(results);
		while (resIter.hasNext()) {
			resultList.add(resIter);
		}
		
		try {
			// set the config stream on the SDK Object Iterator
			SimplePersistantSDKObjectIterator.loadWsddStream(ClassUtils.getResourceAsStream(
				getClass(), "server-config.wsdd"));
			// create the EnumIterator from the objects
			QName name = Utils.getRegisteredQName(resultList.get(0).getClass());
			EnumIterator enumIter = SimplePersistantSDKObjectIterator.createIterator(resultList, name);
			return enumIter;
		} catch (Exception ex) {
			throw new gov.nih.nci.cagrid.data.QueryProcessingException(ex);
		}
	}
	
	
	private EnumIterator processLazyQuery(LazyCQLQueryProcessor processor, CQLQuery query)
		throws gov.nih.nci.cagrid.data.QueryProcessingException, 
		gov.nih.nci.cagrid.data.MalformedQueryException {
		// perform the query
		Iterator results = processor.processQueryLazy(query);
		
		// create an iterator
		EnumIterator iterator = new LazyQueryResultEnumIterator(results);
		return iterator;
	}
}

