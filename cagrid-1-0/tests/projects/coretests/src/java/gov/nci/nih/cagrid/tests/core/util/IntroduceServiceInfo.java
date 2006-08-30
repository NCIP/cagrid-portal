/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class IntroduceServiceInfo
{
	public static final String INTRODUCE_CREATESERVICE_TASK = "createService";
	public static final String INTRODUCE_RESYNCSERVICE_TASK = "resyncService";
	public static final String INTRODUCE_SERVICEXML_FILENAME = "introduce.xml";
	public static final String INTRODUCE_SERVICEMETADATA_FILENAME = "serviceMetadata.xml";

	private String serviceName;
	private String namespace;
	private String packageName;
	private String[] methodNames;
	private boolean transportSecurity = false;
	
	public IntroduceServiceInfo(File serviceXmlDescriptor) 
		throws ParserConfigurationException, SAXException, IOException
	{
		super();
		
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		ServiceXmlHandler handler = new ServiceXmlHandler();
		parser.parse(serviceXmlDescriptor, handler);
		this.methodNames = handler.methodNames.toArray(new String[0]);
	}
	
	private class ServiceXmlHandler
		extends DefaultHandler
	{
		public ArrayList<String> methodNames = new ArrayList<String>();
		
		public void startElement(
			String uri, String lname, String qname, Attributes atts
		) {
			if (qname.endsWith("Service")) {
				serviceName = atts.getValue("name");
				namespace = atts.getValue("namespace");
				packageName = atts.getValue("packageName");
			} else if (qname.endsWith("Method")) {
				if ("false".equals(atts.getValue("isProvided"))) {
					methodNames.add(atts.getValue("name"));
				}
			} else if (qname.endsWith("TransportLevelSecurity")) {
				transportSecurity = atts.getValue("xsi:type").endsWith("TransportLevelSecurity");
			}
		}
	}
	
	public static Class loadClass(File serviceDir, String className) 
		throws MalformedURLException, ClassNotFoundException
	{
		// create class loader and load class
		File[] jars = getServiceJars(serviceDir);
		URL[] urls = new URL[jars.length];
		for (int i = 0; i < jars.length; i++) {
			urls[i] = jars[i].toURL();
		}
		ClassLoader cloader = new URLClassLoader(urls);
		return cloader.loadClass(className);
	}
	
	public static File[] getServiceJars(File serviceDir)
	{
		ArrayList<File> jars = new ArrayList<File>();
		
		addJars(new File(serviceDir, "lib"), jars);
		addJars(new File(serviceDir, "ext"), jars);
		addJars(new File(serviceDir, "build"), jars);
		
		return jars.toArray(new File[0]);
	}
	
	public static void addJars(File file, ArrayList<File> jars)
	{
		if (file.getName().endsWith(".jar")) {
			jars.add(file);
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File nextFile : files) addJars(nextFile, jars);
		}
	}

	public String getNamespace()
	{
		return namespace;
	}

	public String getPackageName()
	{
		return packageName;
	}

	public String getServiceName()
	{
		return serviceName;
	}

	public String[] getMethodNames()
	{
		return methodNames;
	}

	public boolean isTransportSecurity()
	{
		return transportSecurity;
	}

}
