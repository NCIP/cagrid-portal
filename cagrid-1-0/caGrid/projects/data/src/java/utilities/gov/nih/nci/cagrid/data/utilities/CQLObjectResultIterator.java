package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.axis.message.MessageElement;
import org.apache.axis.utils.XMLUtils;
import org.globus.wsrf.encoding.ObjectDeserializer;

/** 
 *  CQLObjectResultIterator
 *  Iterator over CQL Object Results
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 20, 2006 
 * @version $Id$ 
 */
public class CQLObjectResultIterator implements Iterator {
	private CQLObjectResult[] results;
	private int currentIndex;
	private Class objectClass;
	private boolean xmlOnly;
	
	CQLObjectResultIterator(CQLObjectResult[] results, boolean xmlOnly) {
		if (results.length != 0) {
			objectClass = getObjectClass(results[0]);
		}
		this.results = results;
		this.currentIndex = -1;
		this.xmlOnly = xmlOnly;
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
		MessageElement element = results[currentIndex].get_any()[0];
		Object obj = null;
		try {
			String documentString = element.getAsString();
			if (xmlOnly) {
				return documentString;
			}
			InputStream documentStream = new ByteArrayInputStream(documentString.getBytes());
			org.w3c.dom.Document doc = XMLUtils.newDocument(documentStream);
			obj = ObjectDeserializer.toObject(doc.getDocumentElement(), objectClass);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return obj;
	}
	
	
	private Class getObjectClass(CQLObjectResult result) {
		Class c = null;
		try {
			c = Class.forName(result.getType());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return c;
	}
}
