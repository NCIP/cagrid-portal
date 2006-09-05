package gov.nih.nci.cagrid.data.service;

import gov.nih.nci.cagrid.common.FaultHelper;
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
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.axis.utils.ClassUtils;
import org.globus.ws.enumeration.EnumIterator;
import org.oasis.wsrf.faults.BaseFaultType;

/** 
 *  EnumerationQueryImpl
 *  Implementation of the EnumerationQuery
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class EnumerationQueryImpl {

	
	public EnumerationQueryImpl() throws RemoteException {
		
	}
	
	
	public org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerateResponse enumerationQuery(
		gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery) throws RemoteException, 
		gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType, 
		gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType {
		CQLQueryProcessor processor = getQueryProcessor();
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
	
	
	private Exception getTypedException(Exception cause, BaseFaultType fault) {
		FaultHelper helper = new FaultHelper(fault);
		helper.addFaultCause(cause);
		helper.setDescription(cause.getClass().getSimpleName() + " -- " + cause.getMessage());
		return helper.getFault();
	}
	
	
	private CQLQueryProcessor getQueryProcessor() throws QueryProcessingExceptionType {
		try {
			Properties configParams = ServiceConfigUtil.getQueryProcessorConfigurationParameters();
			String qpClassName = ServiceConfigUtil.getCqlQueryProcessorClassName();
			Class qpClass = Class.forName(qpClassName);
			CQLQueryProcessor processor = (CQLQueryProcessor) qpClass.newInstance();
			InputStream configStream = ClassUtils.getResourceAsStream(
				getClass(), "server-config.wsdd");
			processor.initialize(configParams, configStream);
			return processor;
		} catch (Exception ex) {
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
		/*
		// TODO: Fix this to use ws-enum utilities for SDK object iterators
		// write the results to disk
		// first, need a unique ID to use for a file name
		String uuid = UUIDGenFactory.getUUIDGen().nextUUID();
		String filename = uuid + "_EnumerationResults";
		IndexedObjectFileWriter writer = new IndexedObjectFileWriter(filename);
		// now walk through the query results and hand off to writer
		Iterator objIter = new CQLQueryResultsIterator(results);
		QName objectQname = null;
		while (objIter.hasNext()) {
			Object o = objIter.next();
			if (objectQname == null) {
				objectQname = Utils.getRegisteredQName(o.getClass());
			}
			writer.writeObject(o);
		}
		// close the file handle
		writer.close();
		
		// create the persistent iterator
		EnumIterator iterator = new IndexedObjectFileEnumIterator(filename, objectQname);
		return iterator;
		*/
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

