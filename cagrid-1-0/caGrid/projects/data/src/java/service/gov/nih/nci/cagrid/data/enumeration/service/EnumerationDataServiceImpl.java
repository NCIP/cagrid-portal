package gov.nih.nci.cagrid.data.enumeration.service;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.cql.LazyCQLQueryProcessor;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
import gov.nih.nci.cagrid.data.service.BaseServiceImpl;
import gov.nih.nci.cagrid.data.service.ServiceConfigUtil;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.wsenum.utils.EnumerateResponseFactory;
import gov.nih.nci.cagrid.wsenum.utils.EnumerationCreationException;
import gov.nih.nci.cagrid.wsenum.utils.SimplePersistantSDKObjectIterator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.globus.ws.enumeration.EnumIterator;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class EnumerationDataServiceImpl extends BaseServiceImpl {

	
	public EnumerationDataServiceImpl() throws RemoteException {
		super();
	}
	
	public org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerateResponse enumerationQuery(gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery) throws RemoteException, 
		gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, 
		gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType {
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
		
		try {
			String serverConfigLocation = ServiceConfigUtil.getConfigProperty(
				DataServiceConstants.SERVER_CONFIG_LOCATION);
			InputStream configStream = new FileInputStream(serverConfigLocation);
			Iterator resIter = new CQLQueryResultsIterator(results, configStream);
			while (resIter.hasNext()) {
				resultList.add(resIter.next());
			}
			SimplePersistantSDKObjectIterator.loadWsddStream(new FileInputStream(serverConfigLocation));
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

