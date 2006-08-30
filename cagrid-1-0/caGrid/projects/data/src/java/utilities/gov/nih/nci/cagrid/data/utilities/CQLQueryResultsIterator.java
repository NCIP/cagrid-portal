package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;

import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.axis.utils.ClassUtils;

/** 
 *  CQLQueryResultsIterator
 *  Iterator over CQL Query Results
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 20, 2006 
 * @version $Id$ 
 */
public class CQLQueryResultsIterator implements Iterator {
	private CQLQueryResults results;
	private Iterator resultIterator;
	private boolean xmlOnly;
	private InputStream wsddConfigStream;
	
	/**
	 * Create a new CQLQueryResultsIterator which will return Object 
	 * results deserialized with the default configured AXIS deserializers
	 * 
	 * @param results
	 * 		The results to iterate over
	 */
	public CQLQueryResultsIterator(CQLQueryResults results) {
		this(results, false, null);
	}
	
	
	/**
	 * When returning objects, setting xmlOnly to true will bypass the
	 * AXIS deserializer and return straight XML strings.  When set to
	 * false, the results are deseriailzed with the default AXIS config
	 * 
	 * @param results
	 * 		The resuls to iterate over
	 * @param xmlOnly
	 * 		A flag to indicate if xml strings or objects should be returned
	 */
	public CQLQueryResultsIterator(CQLQueryResults results, boolean xmlOnly) {
		this(results, xmlOnly, null);
	}
	
	
	/**
	 * When returning objects, the supplied WSDD configuration file is used
	 * to configure the AXIS deserializers
	 * 
	 * @param results
	 * 		The results to iterate over
	 * @param wsddFilename
	 * 		The filename of a wsdd file to use for configuration
	 */
	public CQLQueryResultsIterator(CQLQueryResults results, InputStream wsdd) {
		this(results, false, wsdd);
	}
	
	
	/**
	 * Internal constructor
	 * @param results
	 * @param xmlOnly
	 * @param wsddFilename
	 */
	private CQLQueryResultsIterator(CQLQueryResults results, boolean xmlOnly, InputStream wsdd) {
		this.results = results;
		this.xmlOnly = xmlOnly;
		this.wsddConfigStream = wsdd;
	}
	

	public void remove() {
		throw new UnsupportedOperationException("remove() is not supported by " + getClass().getName());
	}


	public boolean hasNext() {
		return getIterator().hasNext();
	}


	public Object next() {
		return getIterator().next();
	}

	
	private Iterator getIterator() {
		if (resultIterator == null) {
			if (results.getObjectResult() != null && results.getObjectResult().length != 0) {
				try {
					resultIterator = new CQLObjectResultIterator(
						results.getObjectResult(), results.getTargetClassname(), 
						xmlOnly, findConfigWsdd());
				} catch (ClassNotFoundException ex) {
					resultIterator = new NullIterator(ex.getMessage());
					ex.printStackTrace();
				}
			} else if (results.getAttributeResult() != null && results.getAttributeResult().length != 0) {
				resultIterator = new CQLAttributeResultIterator(results.getAttributeResult());
			} else if (results.getIdentifierResult() != null && results.getIdentifierResult().length != 0) {
				resultIterator = new CQLIdentifierResultIterator(results.getIdentifierResult());
			} else {
				resultIterator = new NullIterator("No results");
			}
		}
		return resultIterator;
	}
	
	
	private InputStream findConfigWsdd() {
		if (wsddConfigStream == null) {
			// use the axis default client configuration
			wsddConfigStream = ClassUtils.getResourceAsStream(getClass(), "client-config.wsdd");
		}
		return wsddConfigStream;
	}
	
	
	private static class NullIterator implements Iterator {
		private String errorMessage;
		
		public NullIterator(String err) {
			this.errorMessage = err;
		}
		
		
		public void remove() {
			throw new UnsupportedOperationException("remove() is not supported by " + getClass().getName());
		}
		
		
		public boolean hasNext() {
			return false;
		}
		
		
		public Object next() {
			throw new NoSuchElementException(errorMessage);
		}
	}
}
