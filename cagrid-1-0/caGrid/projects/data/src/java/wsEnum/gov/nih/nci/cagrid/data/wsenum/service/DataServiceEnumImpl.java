package gov.nih.nci.cagrid.data.wsenum.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.cql.LazyCQLQueryProcessor;
import gov.nih.nci.cagrid.data.service.DataServiceImpl;
import gov.nih.nci.cagrid.data.stubs.MalformedQueryException;
import gov.nih.nci.cagrid.data.stubs.QueryProcessingException;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;

import java.rmi.RemoteException;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axis.components.uuid.UUIDGenFactory;
import org.apache.axis.message.MessageElement;
import org.globus.ws.enumeration.EnumIterator;
import org.globus.ws.enumeration.EnumProvider;
import org.globus.ws.enumeration.EnumResource;
import org.globus.ws.enumeration.EnumResourceHome;
import org.globus.ws.enumeration.IndexedObjectFileEnumIterator;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.utils.io.IndexedObjectFileWriter;
import org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerateResponse;
import org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerationContextType;
import org.xmlsoap.schemas.ws._2004._09.enumeration.ExpirationType;

/** 
 *  DataServiceEnumImpl
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 7, 2006 
 * @version $Id$ 
 */
public class DataServiceEnumImpl extends DataServiceImpl {

	public DataServiceEnumImpl() throws RemoteException {
		super();
	}
	
	
	public org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerateResponse enumerationQuery(gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery) throws RemoteException {
		CQLQueryProcessor processor = getQueryProcessor();
		EnumIterator enumIter = null;
		try {
			if (processor instanceof LazyCQLQueryProcessor) {
				enumIter = processLazyQuery((LazyCQLQueryProcessor) processor, cqlQuery);
			} else {
				enumIter = processQuery(processor, cqlQuery);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			// throw exceptions here
		}

		try {
			// create a resource for the new iteration
			EnumResourceHome resourceHome = EnumResourceHome.getEnumResourceHome();
			// create a persistent resource
			EnumResource resource = resourceHome.createEnumeration(enumIter, true);
			ResourceKey key = resourceHome.getKey(resource);
			EnumerationContextType enumContext = 
				EnumProvider.createEnumerationContextType(key);
			
			EnumerateResponse response = new EnumerateResponse(new MessageElement[] {}, enumContext, new ExpirationType());
			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
			// throw something
		}
		return null;
	}
	
	
	private CQLQueryProcessor getQueryProcessor() throws QueryProcessingException {
		try {
			Class qpClass = Class.forName((String) getConfiguration().get(
				DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY));
			CQLQueryProcessor processor = (CQLQueryProcessor) qpClass.newInstance();
			processor.initialize(getConfiguration());
			return processor;
		} catch (Exception ex) {
			QueryProcessingException qpe = new QueryProcessingException();
			FaultHelper helper = new FaultHelper(qpe);
			helper.addFaultCause(ex);
			helper.setDescription(ex.getMessage());
			throw (QueryProcessingException) helper.getFault();
		}
	}
	
	
	private EnumIterator processQuery(CQLQueryProcessor processor, CQLQuery query)
		throws QueryProcessingException, MalformedQueryException{
		try {
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	
	private EnumIterator processLazyQuery(LazyCQLQueryProcessor processor, CQLQuery query)
		throws QueryProcessingException, MalformedQueryException {
		try {
			// perform the query
			Iterator results = processor.processQueryLazy(query);
			
			// create an iterator
			EnumIterator iterator = new LazyQueryResultEnumIterator(results);
			return iterator;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
