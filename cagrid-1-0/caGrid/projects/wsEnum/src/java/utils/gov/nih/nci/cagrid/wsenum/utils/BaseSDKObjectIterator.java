package gov.nih.nci.cagrid.wsenum.utils;

import gov.nih.nci.cagrid.common.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;

import org.globus.ws.enumeration.EnumIterator;
import org.globus.ws.enumeration.IterationResult;

/** 
 *  BaseSDKObjectIterator
 *  Base class to consolidate some functionality of SDK object enum iterators
 * 
 * @author David Ervin
 * 
 * @created Mar 13, 2007 10:42:15 AM
 * @version $Id: BaseSDKObjectIterator.java,v 1.1 2007-05-16 15:00:57 dervin Exp $ 
 */
public abstract class BaseSDKObjectIterator implements EnumIterator {
	
	private File file;
	private QName objectQName;
	private BufferedReader fileReader;
	private boolean isReleased;
	
	protected BaseSDKObjectIterator(File file, QName objectQName)
		throws FileNotFoundException {
		this.file = file;
		this.objectQName = objectQName;
		this.fileReader = new BufferedReader(new FileReader(file));
		this.isReleased = false;
	}
	
	
	/**
	 * Writes the SDK serializable objects to disk
	 * 
	 * @param objects
	 * 		The list of objects to write out
	 * @param name
	 * 		The QName of the objects
	 * @param filename
	 * 		The filename to store the objects into
	 * @param wsddContents
	 * 		The contents of the WSDD configuration file
	 * @throws Exception
	 */
	protected static void writeSdkObjects(Iterator objIter, QName name, String filename, StringBuffer wsddContents) throws Exception {
		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filename));
		byte[] configBytes = null;
		if (wsddContents != null) {
			configBytes = wsddContents.toString().getBytes();
		}
		while (objIter.hasNext()) {
			StringWriter writer = new StringWriter();
			if (configBytes != null) {
				Utils.serializeObject(objIter.next(), name, writer, 
					new ByteArrayInputStream(configBytes));
			} else {
				Utils.serializeObject(objIter.next(), name, writer);
			}
			String xml = writer.toString().trim();
			fileWriter.write(String.valueOf(xml.length()) + "\n");
			fileWriter.write(xml);
		}
		fileWriter.flush();
		fileWriter.close();
	}
	
	
	/**
	 * Reads the next chunk of XML from the file
	 * 
	 * @return
	 * 		Null if no more XML is found
	 */
	protected String getNextXmlChunk() throws IOException {
		String charCountStr = fileReader.readLine();
		if (charCountStr != null) {
			int toRead = Integer.parseInt(charCountStr);
			char[] charBuff = new char[toRead];
			int count = 0;
			int len = 0;
			while (count < toRead) {
				len = fileReader.read(charBuff, count, charBuff.length - count);
				count += len;
			}
			return new String(charBuff);
		} else {
			return null;
		}
	}
	
	
	/**
	 * Encapsulates converting the list of SOAPElements to an array, then an Iteration Result
	 * 
	 * @param soapElements
	 * @param end
	 * @return
	 * 		An iteration result wrapping the result list
	 */
	protected IterationResult wrapUpElements(List<SOAPElement> soapElements, boolean end) {
		SOAPElement[] elements = new SOAPElement[soapElements.size()];
		soapElements.toArray(elements);
		return new IterationResult(elements, end);
	}


	/**
	 * Releases the enumeration and cleans up resources.
	 * Deletes the temporary persistance file
	 */
	public synchronized void release() {
		try {
			fileReader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		file.delete();
		isReleased = true;
	}
	
	
	protected boolean enumerationIsReleased() {
		return isReleased;
	}
	
	
	protected QName getObjectQName() {
		return objectQName;
	}
}