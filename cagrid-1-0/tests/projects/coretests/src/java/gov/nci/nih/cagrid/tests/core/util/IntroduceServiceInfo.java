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
