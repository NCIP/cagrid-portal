package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.axis.message.MessageElement;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.xml.sax.InputSource;
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
	private InputStream wsddInputStream;
	private byte[] wsddContent;
	
	CQLObjectResultIterator(CQLObjectResult[] results, String targetName, 
		boolean xmlOnly, InputStream wsdd) throws ClassNotFoundException {
		this.objectClass = Class.forName(targetName);
		this.results = results;
		this.currentIndex = -1;
		this.xmlOnly = xmlOnly;
		this.wsddInputStream = wsdd;
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
		try {
			StringWriter writer = new StringWriter();
			InputStream configStream = getConsumableInputStream();
			if (configStream != null) {
				Utils.serializeObject(element, element.getQName(), writer, configStream);
			} else {
				Utils.serializeObject(element, element.getQName(), writer);
			}
			String documentString = writer.getBuffer().toString();
	        if (xmlOnly) {
				return documentString;
			}
	        // since the config stream has been read and closed, it must be recreated
			configStream = getConsumableInputStream();
			if (configStream != null) {
				return Utils.deserializeObject(new StringReader(documentString), objectClass, 
					getConsumableInputStream());
			} else {
				InputSource objectSource = new InputSource(new StringReader(documentString));
				return ObjectDeserializer.deserialize(objectSource, objectClass);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	
	private InputStream getConsumableInputStream() throws IOException {
		if (wsddInputStream != null) {
			if (wsddContent == null) {
				wsddContent = new byte[0];
				byte[] readBytes = new byte[1024];
				int len = -1;
				BufferedInputStream buffStream = new BufferedInputStream(wsddInputStream);
				while ((len = buffStream.read(readBytes)) != -1) {
					byte[] tmpContent = new byte[wsddContent.length + len];
					System.arraycopy(wsddContent, 0, tmpContent, 0, wsddContent.length);
					System.arraycopy(readBytes, 0, tmpContent, wsddContent.length, len);
					wsddContent = tmpContent;
				}
				buffStream.close();
			}
			return new ByteArrayInputStream(wsddContent);
		}
		return null;
	}
}
