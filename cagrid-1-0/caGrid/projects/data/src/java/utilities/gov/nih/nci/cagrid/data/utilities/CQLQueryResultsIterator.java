package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
	
	public CQLQueryResultsIterator(CQLQueryResults results) {
		this(results, false);
	}
	
	
	/**
	 * When returning objects, setting xmlOnly to true will bypass the
	 * AXIS deserializer and return straight XML strings
	 * @param results
	 * @param xmlOnly
	 */
	public CQLQueryResultsIterator(CQLQueryResults results, boolean xmlOnly) {
		this.results = results;
		this.xmlOnly = xmlOnly;
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
				resultIterator = new CQLObjectResultIterator(results.getObjectResult(), xmlOnly);
			} else if (results.getAttributeResult() != null && results.getAttributeResult().length != 0) {
				resultIterator = new CQLAttributeResultIterator(results.getAttributeResult());
			} else if (results.getIdentifierResult() != null && results.getIdentifierResult().length != 0) {
				resultIterator = new CQLIdentifierResultIterator(results.getIdentifierResult());
			} else {
				resultIterator = new NullIterator();
			}
		}
		return resultIterator;
	}
	
	
	private static class NullIterator implements Iterator {
		public void remove() {
			throw new UnsupportedOperationException("remove() is not supported by " + getClass().getName());
		}
		
		
		public boolean hasNext() {
			return false;
		}
		
		
		public Object next() {
			throw new NoSuchElementException();
		}
	}
}
