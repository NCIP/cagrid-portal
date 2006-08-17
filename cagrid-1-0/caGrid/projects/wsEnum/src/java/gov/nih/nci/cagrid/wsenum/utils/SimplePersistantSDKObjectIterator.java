package gov.nih.nci.cagrid.wsenum.utils;

import gov.nih.nci.cagrid.common.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;

import org.globus.ws.enumeration.EnumIterator;
import org.globus.ws.enumeration.IterationConstraints;
import org.globus.ws.enumeration.IterationResult;
import org.globus.ws.enumeration.TimeoutException;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.encoding.SerializationException;

/** 
 *  PersistantSDKObjectIterator
 *  Enumeration iterator which provides for persisting caCORE SDK objects to disk
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 17, 2006 
 * @version $Id$ 
 */
public class SimplePersistantSDKObjectIterator implements EnumIterator {
	private File file = null;
	private BufferedReader fileReader = null;
	private QName objectQName = null;
	private boolean isReleased;
	
	private SimplePersistantSDKObjectIterator(File file, QName objectQName) throws FileNotFoundException {
		this.file = file;
		this.fileReader = new BufferedReader(new FileReader(file));
		this.objectQName = objectQName;
		this.isReleased = false;
	}
	
	
	/**
	 * Serializes a List of caCORE SDK generated objects to a temp file on
	 * the local disk, then creates an EnumIterator which can return
	 * those objects.
	 * 
	 * @param objects
	 * 		The list of caCORE SDK objects to be enumerated
	 * @param objectQName
	 * 		The QName of the objects
	 * @return
	 * @throws Exception
	 */
	public static EnumIterator createIterator(List objects, QName objectQName) throws Exception {
		return createIterator(objects, objectQName, File.createTempFile("EnumIteration", ".serialized").getAbsolutePath());
	}
	
	
	/**
	 * Serializes a List of caCORE SDK generated objects to a specified file on
	 * the local disk, then creates an EnumIterator which can return
	 * those objects.
	 * 
	 * @param objects
	 * 		The list of caCORE SDK objects to be enumerated
	 * @param objectQName
	 * 		The QName of the objects
	 * @param filename
	 * 		The name of the file to serialize objects into
	 * @return
	 * @throws Exception
	 */
	public static EnumIterator createIterator(List objects, QName objectQName, String filename) throws Exception {
		writeSdkObjects(objects, objectQName, filename);
		return new SimplePersistantSDKObjectIterator(new File(filename), objectQName);
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
	 * @throws Exception
	 */
	private static void writeSdkObjects(List objects, QName name, String filename) throws Exception {
		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filename));
		Iterator objIter = objects.iterator();
		while (objIter.hasNext()) {
			StringWriter writer = new StringWriter();
			Utils.serializeObject(objIter.next(), name, writer);
			String xml = writer.toString();			
			fileWriter.write(String.valueOf(xml.length()) + "\n");
			fileWriter.write(xml + "\n");
		}
		fileWriter.flush();
		fileWriter.close();
	}
	

	/**
     * Retrieves the next set of items of the enumeration.
     * <b>Note:</b> This implementation ignores any iteration constraints except for max elements
     *
     * @param constraints the constrains for this iteration. Can be null.
     *        If null, default constraints must be assumed.
     * @return the result of this iteration that fulfils the specified
     *         constraints. It must always be non-null.
     * @throws TimeoutException if <tt>maxTime</tt> constraint was specified
     *         and the enumeration data was not collected within that time.
     *         <i>This is never thrown in this implementation</i>
     * @throws NoSuchElementException if iterator has no more elements
     */
	public IterationResult next(IterationConstraints constraints) throws TimeoutException, NoSuchElementException {
		// check for release
		if (isReleased) {
			throw new NoSuchElementException("Enumeration has been released");
		}
		// temporary list to hold SOAPElements
		List soapElements = new ArrayList(constraints.getMaxElements());
		
		// start building results
		String xml = null;
		while (soapElements.size() < constraints.getMaxElements() && (xml = getNextXmlChunk()) != null) {
			try {
				SOAPElement element = ObjectSerializer.toSOAPElement(xml, objectQName);
				soapElements.add(element);
			} catch (SerializationException ex) {
				release();
				NoSuchElementException nse = new NoSuchElementException("Error serializing element -- " + ex.getMessage());
				nse.setStackTrace(ex.getStackTrace());
				throw nse;
			}
		}
		// if the xml text is null, we're at the end of the iteration
		return wrapUpElements(soapElements, xml != null);
	}
	
	
	/**
	 * Encapsulates converting the list of SOAPElements to an array, then an Iteration Result
	 * 
	 * @param soapElements
	 * @param end
	 * @return
	 */
	private IterationResult wrapUpElements(List soapElements, boolean end) {
		SOAPElement[] elements = new SOAPElement[soapElements.size()];
		soapElements.toArray(elements);
		return new IterationResult(elements, end);
	}
	
	
	/**
	 * Reads the next chunk of XML from the file
	 * 
	 * @return
	 * 		Null if no more XML is found
	 */
	private String getNextXmlChunk() {
		try {
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
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}


	/**
	 * Releases the enumeration's resources.  Specificaly, this deletes the underlying serialization file
	 */
	public void release() {
		try {
			fileReader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		file.delete();
		isReleased = true;
	}
}
