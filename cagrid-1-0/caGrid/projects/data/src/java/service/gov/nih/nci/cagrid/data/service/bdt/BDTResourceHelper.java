package gov.nih.nci.cagrid.data.service.bdt;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
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
import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;
import org.apache.axis.utils.XMLUtils;
import org.globus.transfer.AnyXmlType;
import org.globus.ws.enumeration.EnumIterator;
import org.w3c.dom.Document;

/** 
 *  BDTResourceHelper
 *  Uses the data service base implementation to support the BDT resource
 * 
 * @author David Ervin
 * 
 * @created Mar 12, 2007 2:08:57 PM
 * @version $Id: BDTResourceHelper.java,v 1.2 2007-03-14 16:29:12 dervin Exp $ 
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
	public AnyXmlType resultsAsAnyType() throws QueryProcessingException, MalformedQueryException {
		AnyXmlType any = new AnyXmlType();
		CQLQueryResults results = processQuery();
		// serialize the results
		StringWriter writer = new StringWriter();
		try {
			Utils.serializeObject(results, DataServiceConstants.CQL_RESULT_SET_QNAME, 
				writer, getConsumableInputStream());
		} catch (Exception ex) {
			throw new QueryProcessingException("Error serializing results: " + ex.getMessage(), ex);
		}
		// convert the XML to a w3c Dom element
		byte[] xmlBytes = writer.getBuffer().toString().getBytes();
		ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(xmlBytes);
		Document doc = null;
		try {
			doc = XMLUtils.newDocument(xmlInputStream);
		} catch (Exception ex) {
			throw new QueryProcessingException("Error DOMing xml: " + ex.getMessage(), ex);
		}
		// create the any type element
		MessageElement anyElement = new MessageElement(doc.getDocumentElement());
		any.set_any(new MessageElement[] {anyElement});
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
	 * Processes the CQL query if it has not already been done, and returns the 
	 * CQL Query Results from it
	 * 
	 * @return
	 * 		The CQLQueryResults of processing the query
	 * @throws QueryProcessingException
	 * @throws MalformedQueryException
	 */
	private CQLQueryResults processQuery() throws QueryProcessingException, MalformedQueryException {
		if (queryResults == null) {
			// initialize the CQL Query Processor
			CQLQueryProcessor processor = null;
			try {
				processor = getCqlQueryProcessorInstance();
			} catch (QueryProcessingExceptionType ex) {
				throw new QueryProcessingException(ex);
			}
			// perform the query
			queryResults = processor.processQuery(query);
		}
		return queryResults;
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
		MalformedQueryException, IOException {
		CQLQueryResults results = processQuery();
		Iterator iterator = new CQLQueryResultsIterator(results, getConsumableInputStream());
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
