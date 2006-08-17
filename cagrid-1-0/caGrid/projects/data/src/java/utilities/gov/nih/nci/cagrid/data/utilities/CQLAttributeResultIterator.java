package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.cqlresultset.CQLAttributeResult;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** 
 *  CQLAttributeResultIterator
 *  Iterator over attribute results
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 20, 2006 
 * @version $Id$ 
 */
public class CQLAttributeResultIterator implements Iterator {
	private CQLAttributeResult[] results;
	private int currentIndex;
	
	CQLAttributeResultIterator(CQLAttributeResult[] results) {
		this.results = results;
		this.currentIndex = -1;
	}
	

	public void remove() {
		throw new UnsupportedOperationException("remove() is not supported by " + getClass().getName());
	}


	public boolean hasNext() {
		return currentIndex + 1 < results.length;
	}


	/**
	 * @return TypeAttribute[]
	 */
	public Object next() {
		currentIndex++;
		if (currentIndex >= results.length) {
			throw new NoSuchElementException();
		}
		return results[currentIndex].getAttribute();
	}
}
