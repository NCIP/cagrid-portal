package gov.nih.nci.cagrid.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.AxisEngine;
import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.MessageContext;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.message.MessageElement;
import org.apache.axis.server.AxisServer;
import org.apache.axis.utils.XMLUtils;
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class Utils {

	public static File getCaGridUserHome() {
		String userHome = System.getProperty("user.home");
		File userHomeF = new File(userHome);
		File caGridCache = new File(userHomeF.getAbsolutePath() + File.separator + ".cagrid");
		if (!caGridCache.exists()) {
			caGridCache.mkdirs();
		}
		return caGridCache;
	}


	public static String getExceptionMessage(Exception e) {
		String mess = e.getMessage();
		if (e instanceof AxisFault) {
			AxisFault af = (AxisFault) e;
			if ((af.getFaultCode() != null)
				&& (af.getFaultCode().toString()
					.equals("{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd}General"))) {
				System.out.println(af.getFaultString());
				if ((af.getFaultString() != null)
					&& (af.getFaultString().equals("javax.xml.rpc.soap.SOAPFaultException"))) {
					mess = "An error occurred establishing a secure communication channel.  The \n"
						+ "problem may be that the client's credentials are NOT trusted by the server.";
				} else {

					mess = af.getFaultString();
				}

			} else if ((af.getFaultString() != null) && (af.getFaultString().equals("java.io.EOFException"))) {
				mess = "An error occurred in communicating with the service.  If using\n"
					+ "credentials to authenticate to the service, the problem may be\n"
					+ "that the credentials being used are not trusted by the server.";
			} else if ((af.getFaultString() != null)
				&& (af.getFaultString().equals("java.net.SocketException: Connection reset"))) {
				mess = "An error occurred in communicating with the service.  If using\n"
					+ "credentials to authenticate to the service, the problem may be\n"
					+ "that the credentials being used are not trusted by the server.";
			} else {
				mess = af.getFaultString();
			}
		}
		return simplifyErrorMessage(mess);
	}


	public static String simplifyErrorMessage(String m) {
		if ((m == null) || (m.equalsIgnoreCase("null"))) {
			m = "Unknown Error";
		} else if (m.indexOf("Connection refused") >= 0) {
			m = "Could not connect to the request service, the service may not exist or may be down.";
		} else if (m.indexOf("Unknown CA") >= 0) {
			m = "Could establish a connection with the service, the service CA is not trusted.";
		}
		return m;
	}


	public static Object deserializeDocument(String fileName, Class objectType) throws Exception {
		InputStream inputStream = null;

		inputStream = new FileInputStream(fileName);
		org.w3c.dom.Document doc = XMLUtils.newDocument(inputStream);
		Object obj = ObjectDeserializer.toObject(doc.getDocumentElement(), objectType);
		inputStream.close();
		return obj;

	}


	public static void copyFile(File in, File out) throws Exception {
		FileInputStream fis = new FileInputStream(in);
		out.getCanonicalFile().getParentFile().mkdirs();
		FileOutputStream fos = new FileOutputStream(out);
		byte[] buf = new byte[1024];
		int i = 0;
		while ((i = fis.read(buf)) != -1) {
			fos.write(buf, 0, i);
		}
		fis.close();
		fos.close();
	}


	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					System.err.println("could not remove directory: " + dir.getAbsolutePath());
					return false;
				}
			}
		}
		return dir.delete();
	}


	public static StringBuffer fileToStringBuffer(File file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuffer sb = new StringBuffer();
		try {
			String s = null;
			while ((s = br.readLine()) != null) {
				sb.append(s + "\n");
			}
		} catch (Exception e) {
			br.close();
			throw new Exception("Error reading the buffer: " + e.getMessage(), e);
		}

		br.close();

		return sb;
	}
	
	
	/**
	 * Serialize an Object to XML
	 * @param obj
	 * 		The object to be serialized
	 * @param qname
	 * 		The QName of the object
	 * @param writer
	 * 		A writer to place XML into (eg: FileWriter, StringWriter)
	 * @param wsdd
	 * 		A stream containing the WSDD configuration
	 * @throws Exception
	 */
	public static void serializeObject(Object obj, QName qname, Writer writer, InputStream wsdd) 
		throws Exception {
		// derive a message element for the object
		MessageElement element = (MessageElement) ObjectSerializer.toSOAPElement(obj, qname);
		// configure the axis engine to use the supplied wsdd file
		EngineConfiguration engineConfig = new FileProvider(wsdd);
		AxisEngine axisClient = new AxisServer(engineConfig);
		MessageContext messageContext = new MessageContext(axisClient);
		messageContext.setEncodingStyle("");
		messageContext.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		// the following two properties prevent xsd types from appearing in
		// every single element in the serialized XML
		messageContext.setProperty(AxisEngine.PROP_EMIT_ALL_TYPES, Boolean.FALSE);
		messageContext.setProperty(AxisEngine.PROP_SEND_XSI, Boolean.FALSE);
		
		// create a serialization context to use the new message context
		SerializationContext serializationContext = 
			new SerializationContext(writer, messageContext);
		serializationContext.setPretty(true);
		
		// output the message element through the serialization context
		element.output(serializationContext);
		writer.write("\n");
	}
	
	
	/**
	 * Deserializes XML into an object
	 * @param xmlReader
	 * 		The reader for the XML (eg: FileReader, StringReader, etc)
	 * @param clazz
	 * 		The class to serialize to
	 * @param wsdd
	 * 		A stream containing the WSDD configuration
	 * @return
	 * @throws SAXException
	 * @throws DeserializationException
	 */
	public static Object deserializeObject(Reader xmlReader, Class clazz, InputStream wsdd) 
	throws SAXException, DeserializationException {
		// input source for the xml
		InputSource xmlSource = new InputSource(xmlReader);
		
		return ConfigurableObjectDeserializer.toObject(xmlSource, clazz, wsdd);		
	}


	public static void serializeDocument(String fileName, Object object, QName qname) throws Exception {
		FileWriter fw = null;
		fw = new FileWriter(fileName);
		ObjectSerializer.serialize(fw, object, qname);
		fw.close();
	}


	public static String clean(String s) {
		if ((s == null) || (s.trim().length() == 0)) {
			return null;
		} else {
			return s;
		}
	}


	public static void stringBufferToFile(StringBuffer string, String fileName) throws Exception {
		FileWriter fw = new FileWriter(new File(fileName));
		fw.write(string.toString());
		fw.close();
	}


	public static boolean equals(Object o1, Object o2) {
		if ((o1 == null) && (o2 == null)) {
			return true;
		} else if ((o1 != null) && (o2 == null)) {
			return false;
		} else if ((o1 == null) && (o2 != null)) {
			return false;
		} else {
			return o1.equals(o2);
		}
	}


	/**
	 * Gets the QName that Axis has registered for the given java class
	 * 
	 * @param clazz
	 * @return
	 */
	public static QName getRegisteredQName(Class clazz) {
		return MessageContext.getCurrentContext().getTypeMapping().getTypeQName(clazz);
	}


	/**
	 * Gets the Class that Axis has registerd for the given QName
	 * 
	 * @param qname
	 * @return
	 */
	public static Class getRegisteredClass(QName qname) {
		return MessageContext.getCurrentContext().getTypeMapping().getClassForQName(qname);
	}
	
	
	public static List recursiveListFiles(File baseDir, final FileFilter filter) {
		FileFilter dirFilter = new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory() || filter.accept(pathname);
			}
		};
		File[] fileArray = baseDir.listFiles(dirFilter);
		List files = new ArrayList(fileArray.length);
		for (int i = 0; i < fileArray.length; i++) {
			if (fileArray[i].isDirectory()) {
				files.addAll(recursiveListFiles(fileArray[i], filter));
			} else {
				files.add(fileArray[i]);
			}
		}
		return files;
	}
}
