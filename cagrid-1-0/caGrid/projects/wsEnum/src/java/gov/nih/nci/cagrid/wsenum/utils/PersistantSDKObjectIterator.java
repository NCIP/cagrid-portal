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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;

import org.globus.axis.utils.DurationUtils;
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
public class PersistantSDKObjectIterator implements EnumIterator {
	private static final String THREAD_EXCEPTION = "ThreadException";
	private static final String MUST_STOP_THREAD = "StopThread";
	private static final String ITERATION_RESULT = "IterationResult";
	
	private File file = null;
	private BufferedReader fileReader = null;
	private QName objectQName = null;
	private boolean isReleased;
	
	private PersistantSDKObjectIterator(File file, QName objectQName)
		throws FileNotFoundException {
		this.file = file;
		this.objectQName = objectQName;
		this.fileReader = new BufferedReader(new FileReader(file));
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
	 * 		An input stream to the WSDD configuration file
	 * @return
	 * 		An enum iterator instance to iterate the given objects
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
	 * @param tempFilename
	 * 		The name of the file to serialize objects into.
	 * 		<b><i>NOTE:</b></i> For security reasons, access to this location 
	 * 		must be controlled in a production data environment.
	 * @param wsddInput
	 * 		An input stream of the WSDD configuration file
	 * @return
	 * 		An enum iterator instance to iterate the given objects
	 * @throws Exception
	 */
	public static EnumIterator createIterator(List objects, QName objectQName, InputStream wsddInput, String tempFilename) throws Exception {
		StringBuffer wsddContents = Utils.inputStreamToStringBuffer(wsddInput);
		writeSdkObjects(objects, objectQName, tempFilename, wsddContents);
		return new PersistantSDKObjectIterator(new File(tempFilename), objectQName);
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
	private static void writeSdkObjects(List objects, QName name, String filename, StringBuffer wsddContents) throws Exception {
		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filename));
		Iterator objIter = objects.iterator();
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
     * Retrieves the next set of items of the enumeration.
     * <b>Note:</b> This implementation ignores the maxCharacters iteration constraint
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
	public IterationResult next(final IterationConstraints constraints) throws TimeoutException, NoSuchElementException {
		ThreadGroup execGroup = new ThreadGroup("next executor");
		final Map<Object, Object> threadCommunicationBuffer = new HashMap<Object, Object>();
		synchronized (threadCommunicationBuffer) {
			threadCommunicationBuffer.put(MUST_STOP_THREAD, Boolean.FALSE);	
		}
		final Thread nextExec = new Thread(execGroup, new Runnable() {
			public void run() {
				Thread.yield();
				try {
					// check for release
					if (isReleased) {
						throw new NoSuchElementException("Enumeration has been released");
					}
					
					// temporary list to hold SOAPElements
					List<SOAPElement> soapElements = new ArrayList<SOAPElement>(constraints.getMaxElements());
					
					// start building results
					String xml = null;
					checkForStop(threadCommunicationBuffer);
					while (soapElements.size() < constraints.getMaxElements() && (xml = getNextXmlChunk()) != null) {
						try {
							SOAPElement element = ObjectSerializer.toSOAPElement(xml, objectQName);
							if (constraints.getMaxCharacters() != -1) {
								// check the length of the newly created element can fit
								// in the iteration result
								int elemLength = element.getValue().length();
								int currentLength = countSoapElementChars(soapElements);
								if (elemLength + currentLength >= constraints.getMaxCharacters()) {
									// TODO: store the too-big element for later and return
									break;
								}
							}
							soapElements.add(element);
						} catch (SerializationException ex) {
							release();
							NoSuchElementException nse = new NoSuchElementException("Error serializing element -- " + ex.getMessage());
							nse.setStackTrace(ex.getStackTrace());
							throw nse;
						}
						checkForStop(threadCommunicationBuffer);
					}
					
					// if the xml text is null, we're at the end of the iteration
					IterationResult result = wrapUpElements(soapElements, xml == null);
					synchronized (threadCommunicationBuffer) {
						threadCommunicationBuffer.put(ITERATION_RESULT, result);
					}
				} catch (Exception ex) {
					synchronized (threadCommunicationBuffer) {
						threadCommunicationBuffer.put(THREAD_EXCEPTION, ex);	
					}
				}
			}
		});
		
		final Thread waiter = new Thread(execGroup, new Runnable() {
			public void run() {
				try {
					if (constraints.getMaxTime() != null) {
						long timeout = getWaitMills(constraints);
						nextExec.join(timeout);
						synchronized (threadCommunicationBuffer) {
							// regardless of what the other thread is doing, it should stop
							threadCommunicationBuffer.put(MUST_STOP_THREAD, Boolean.TRUE);
							// if there's no result, then some exception must be thrown
							if (threadCommunicationBuffer.get(ITERATION_RESULT) == null 
								&& threadCommunicationBuffer.get(THREAD_EXCEPTION) == null) {
								// no exception from the query thread means a timeout
								threadCommunicationBuffer.put(THREAD_EXCEPTION, new TimeoutException());
							}
						}
					} else {
						nextExec.join();
					}
				} catch (InterruptedException ex) {
					synchronized (threadCommunicationBuffer) {
						threadCommunicationBuffer.put(MUST_STOP_THREAD, Boolean.TRUE);
						threadCommunicationBuffer.put(THREAD_EXCEPTION, ex);
					}
				}
			}
		});
		// start the processing threads
		nextExec.start();
		waiter.start();
		// wait on the query monitor thread as long as it needs
		try {
			waiter.join();
		} catch (InterruptedException ex) {
			throw new TimeoutException(ex);
		}
		
		synchronized (threadCommunicationBuffer) {
			// check for exceptions from the execution thread
			Exception ex = (Exception) threadCommunicationBuffer.get(THREAD_EXCEPTION);
			if (ex != null) {				
				if (ex instanceof NoSuchElementException) {
					throw (NoSuchElementException) ex;
				} else if (ex instanceof InterruptedException || ex instanceof TimeoutException) {
					throw new TimeoutException(ex);
				} else {
					// wish we could throw some sort of processing exception
					throw new NoSuchElementException(ex.getMessage());
				}
			}
			
			// check for iteration result
			IterationResult result = (IterationResult) threadCommunicationBuffer.get(ITERATION_RESULT);
			if (result != null) {
				return result;
			} else {
				throw new NoSuchElementException("No iteration result created");
			}
		}
	}
	
	
	private long getWaitMills(IterationConstraints cons) {
		return DurationUtils.toMilliseconds(cons.getMaxTime());
	}
	
	
	/**
	 * Encapsulates converting the list of SOAPElements to an array, then an Iteration Result
	 * 
	 * @param soapElements
	 * @param end
	 * @return
	 * 		An iteration result wrapping the result list
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
	private String getNextXmlChunk() throws IOException {
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
	 * Counts all the characters in a list of soap elements
	 * 
	 * @param soapElements
	 * @return
	 * 		The total character count
	 */
	private int countSoapElementChars(List soapElements) {
		int count = 0;
		for (int i = 0; i < soapElements.size(); i++) {
			SOAPElement elem = (SOAPElement) soapElements.get(i);
			count += elem.getValue().length();
		}
		return count;
	}
	
	
	/**
	 * Raises an interrupted exception if a flag in the param map
	 * specifies that the query processing thread must be stopped
	 * 
	 * @param params
	 * @throws InterruptedException
	 */
	private void checkForStop(final Map params) throws InterruptedException {
		synchronized (params) {
			if (((Boolean) params.get(MUST_STOP_THREAD)).booleanValue()) {
				throw new InterruptedException();
			}
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