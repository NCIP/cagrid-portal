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
import java.io.InputStream;
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
 *  Enumeration iterator which provides for persisting caCORE SDK objects to disk.
 *  This iterator makes no attempt to respect any IterationConstraints except
 *  for maxElements.
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
	 * <b><i>NOTE:</b></i> The temp file is created in the current user's 
	 * home directory /.cagrid/SDKEnumIterator directory.  For security
	 * reasons, access to this location must be controlled in a production
	 * data environment. 
	 * 
	 * @param objects
	 * 		The list of caCORE SDK objects to be enumerated
	 * @param objectQName
	 * 		The QName of the objects
	 * @param wsddInput
	 * 		An input stream of the WSDD configuration
	 * @return
	 * 		An enum iterator instance for the given objects
	 * @throws Exception
	 */
	public static EnumIterator createIterator(List objects, QName objectQName, InputStream wsddInput) throws Exception {
		File tempSerializationDir = new File(Utils.getCaGridUserHome().getAbsolutePath() 
			+ File.separator + "SDKEnumIterator");
		if (!tempSerializationDir.exists()) {
			tempSerializationDir.mkdirs();
		}
		return createIterator(objects, objectQName, wsddInput, 
			File.createTempFile("EnumIteration", ".serialized", tempSerializationDir).getAbsolutePath());
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
	 * 		The name of the file to serialize objects into.
	 * 		<b><i>NOTE:</b></i> For security reasons, access to this location 
	 * 		must be controlled in a production data environment.
	 * @param wsddInput
	 * 		An input stream of the WSDD configuration 
	 * @return
	 * 		An enum iterator instance for the given objects
	 * @throws Exception
	 */
	public static EnumIterator createIterator(List objects, QName objectQName, InputStream wsddInput, String filename) throws Exception {
		StringBuffer wsddContents = Utils.inputStreamToStringBuffer(wsddInput);
		writeSdkObjects(objects, objectQName, filename, wsddContents);
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
	 * @param configFileContents
	 * 		The contents of the WSDD config document
	 * @throws Exception
	 */
	private static void writeSdkObjects(List objects, QName name, String filename, StringBuffer configFileContents) throws Exception {
		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filename));
		Iterator objIter = objects.iterator();
		byte[] configBytes = null;
		if (configFileContents != null) {
			configBytes = configFileContents.toString().getBytes();
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
		List<SOAPElement> soapElements = new ArrayList<SOAPElement>(constraints.getMaxElements());
		
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
		return wrapUpElements(soapElements, xml == null);
	}
	
	
	/**
	 * Encapsulates converting the list of SOAPElements to an array, then an Iteration Result
	 * 
	 * @param soapElements
	 * @param end
	 * @return
	 * 		Wraps the list of soap elements in an iteration result
	 */
	private IterationResult wrapUpElements(List<SOAPElement> soapElements, boolean end) {
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
