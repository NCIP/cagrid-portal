package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.cqlresultset.CQLIdentifierResult;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** 
 *  CQLIdentifierResultIterator
 *  Iterator over CQL Identifier Results
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 20, 2006 
 * @version $Id$ 
 */
public class CQLIdentifierResultIterator implements Iterator {
	private CQLIdentifierResult[] results;
	private int currentIndex;
	
	CQLIdentifierResultIterator(CQLIdentifierResult[] results) {
		this.results = results;
		this.currentIndex = -1;
	}
	

	public void remove() {
		throw new UnsupportedOperationException("remove() is not supported by " + getClass().getName());
	}


	public boolean hasNext() {
		return currentIndex + 1 < results.length;
	}


	public Object next() {
		currentIndex++;
		if (currentIndex >= results.length) {
			throw new NoSuchElementException();
		}
		return results[currentIndex].getIdentifier();
	}
}