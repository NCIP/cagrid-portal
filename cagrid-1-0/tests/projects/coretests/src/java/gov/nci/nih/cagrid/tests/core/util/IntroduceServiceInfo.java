/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core.util;

import java.io.File;
import java.io.IOException;
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

	private String serviceName;
	private String namespace;
	private String packageName;
	private String[] methodNames;
	
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
				methodNames.add(atts.getValue("name"));
			}
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

}
