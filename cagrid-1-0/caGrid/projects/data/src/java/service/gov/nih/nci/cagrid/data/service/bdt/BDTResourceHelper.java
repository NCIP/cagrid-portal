package gov.nih.nci.cagrid.data.service.bdt;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
import gov.nih.nci.cagrid.data.mapping.ClassToQname;
import gov.nih.nci.cagrid.data.mapping.Mappings;
import gov.nih.nci.cagrid.data.service.BaseServiceImpl;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.wsenum.utils.PersistantSDKObjectIterator;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.globus.transfer.AnyXmlType;
import org.globus.ws.enumeration.EnumIterator;

/** 
 *  BDTResourceHelper
 *  Uses the data service base implementation to support the BDT resource
 * 
 * @author David Ervin
 * 
 * @created Mar 12, 2007 2:08:57 PM
 * @version $Id: BDTResourceHelper.java,v 1.1 2007-03-13 16:14:11 dervin Exp $ 
 */
public class BDTResourceHelper extends BaseServiceImpl {
	private CQLQuery query;
	private String classToQNameMapfile;
	private InputStream wsddInput;
	
	private EnumIterator enumIter;
	private QName targetQName;
	private CQLQueryResults queryResults;
	private byte[] wsddBytes;
	
	public BDTResourceHelper(CQLQuery query, String classToQNameMapfile, InputStream wsddInput) {
		this.query = query;
		this.classToQNameMapfile = classToQNameMapfile;
		this.wsddInput = wsddInput;
	}
	

	public EnumIterator createEnumIterator() 
		throws QueryProcessingExceptionType, MalformedQueryExceptionType {
		if (enumIter == null) {
			// preprocessing on the query (validation, etc)
			preProcess(query);
			
			try {
				Iterator resultIter = processQueryAndIterate();
				// get the qname of the object types
				QName qName = getQueryTargetQName();
				enumIter = PersistantSDKObjectIterator.createIterator(
					resultIter, qName, getConsumableInputStream());
			} catch (gov.nih.nci.cagrid.data.QueryProcessingException ex) {
				throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
			} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {
				throw (MalformedQueryExceptionType) getTypedException(ex, new MalformedQueryExceptionType());
			} catch (FileNotFoundException ex) {
				throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
			} catch (IOException ex) {
				throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
			} catch (Exception ex) {
				throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
			}
		}
		return enumIter;
	}
	
	
	/**
	 * Returns the result of a query as an any type
	 * @return
	 * 		The query result
	 */
	public AnyXmlType resultsAsAnyType() {
		AnyXmlType any = new AnyXmlType();
		
		return any;
	}
	
	
	/**
	 * Releases resources created by the helper
	 */
	public void cleanUp() {
		if (enumIter != null) {
			enumIter.release();
		}
		
	}
	
	
	/**
	 * Processes a CQL query and returns an Iteration over the result set
	 * 
	 * @return
	 * 		An Iteration over the query result set
	 * @throws QueryProcessingException
	 * @throws MalformedQueryException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private Iterator processQueryAndIterate() throws QueryProcessingException, 
		MalformedQueryException, IOException, FileNotFoundException {
		Iterator iterator = null;
		if (queryResults == null) {
			// initialize the CQL Query Processor
			CQLQueryProcessor processor = getCqlQueryProcessorInstance();
			// perform the query
			queryResults = processor.processQuery(query);
		}
		// use the existing result set
		iterator = new CQLQueryResultsIterator(queryResults, getConsumableInputStream());
		
		return iterator;
	}
	
	
	/**
	 * Gets the query target's QName
	 * @return
	 * 		The QName
	 * @throws Exception
	 */
	private QName getQueryTargetQName() throws Exception {
		if (targetQName == null) {
			Mappings mapping = (Mappings) Utils.deserializeDocument(
				classToQNameMapfile, Mappings.class);
			for (int i = 0; i < mapping.getMapping().length; i++) {
				ClassToQname conversion = mapping.getMapping(i);
				if (conversion.getClassName().equals(query.getTarget().getName())) {
					targetQName = QName.valueOf(conversion.getQname());
					break;
				}
			}
		}
		return targetQName;
	}
	
	
	/**
	 * Gets an input stream from the wsdd input stream which can be used and
	 * abused at will
	 * @return
	 * 		A consumable version of the wsdd input stream
	 * @throws IOException
	 */
	private InputStream getConsumableInputStream() throws IOException {
		if (wsddBytes == null) {
			StringBuffer wsddContents = Utils.inputStreamToStringBuffer(wsddInput);
			wsddBytes = wsddContents.toString().getBytes();
		}
		return new ByteArrayInputStream(wsddBytes);
	}
}
