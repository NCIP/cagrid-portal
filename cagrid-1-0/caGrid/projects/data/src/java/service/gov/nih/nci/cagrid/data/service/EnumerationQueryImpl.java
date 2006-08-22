package gov.nih.nci.cagrid.data.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.cql.LazyCQLQueryProcessor;
import gov.nih.nci.cagrid.data.stubs.MalformedQueryException;
import gov.nih.nci.cagrid.data.stubs.QueryProcessingException;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.wsenum.utils.EnumerateResponseFactory;
import gov.nih.nci.cagrid.wsenum.utils.EnumerationCreationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.components.uuid.UUIDGenFactory;
import org.globus.ws.enumeration.EnumIterator;
import org.globus.ws.enumeration.IndexedObjectFileEnumIterator;
import org.globus.wsrf.utils.io.IndexedObjectFileWriter;
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
		gov.nih.nci.cagrid.data.stubs.QueryProcessingException, 
		gov.nih.nci.cagrid.data.stubs.MalformedQueryException {
		CQLQueryProcessor processor = getQueryProcessor();
		EnumIterator enumIter = null;
		try {
			if (processor instanceof LazyCQLQueryProcessor) {
				enumIter = processLazyQuery((LazyCQLQueryProcessor) processor, cqlQuery);
			} else {
				enumIter = processQuery(processor, cqlQuery);
			}
		} catch (gov.nih.nci.cagrid.data.QueryProcessingException ex) {
			throw (QueryProcessingException) getTypedException(ex, new QueryProcessingException());
		} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {
			throw (MalformedQueryException) getTypedException(ex, new MalformedQueryException());
		} catch (FileNotFoundException ex) {
			throw (QueryProcessingException) getTypedException(ex, new QueryProcessingException());
		} catch (IOException ex) {
			throw (QueryProcessingException) getTypedException(ex, new QueryProcessingException());
		}
		
		try {
			return EnumerateResponseFactory.createCustomResponse(enumIter, false);
		} catch (EnumerationCreationException ex) {
			throw (QueryProcessingException) getTypedException(ex, new QueryProcessingException());
		}
	}
	
	
	private Exception getTypedException(Exception cause, BaseFaultType fault) {
		FaultHelper helper = new FaultHelper(fault);
		helper.addFaultCause(cause);
		helper.setDescription(cause.getClass().getSimpleName() + " -- " + cause.getMessage());
		return helper.getFault();
	}
	
	
	private CQLQueryProcessor getQueryProcessor() throws QueryProcessingException {
		try {
			Map configMap = ServiceConfigUtil.getConfigurationMap();
			Class qpClass = Class.forName((String) configMap.get(
				DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY));
			CQLQueryProcessor processor = (CQLQueryProcessor) qpClass.newInstance();
			processor.initialize(configMap);
			return processor;
		} catch (Exception ex) {
			throw (QueryProcessingException) getTypedException(ex, new QueryProcessingException());
		}
	}
	
	
	private EnumIterator processQuery(CQLQueryProcessor processor, CQLQuery query)
		throws gov.nih.nci.cagrid.data.QueryProcessingException, 
		gov.nih.nci.cagrid.data.MalformedQueryException,
		FileNotFoundException, IOException {
		// perform the query
		CQLQueryResults results = processor.processQuery(query);
		
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

