/*
 * Created on Jan 25, 2007
 */
package gov.nih.nci.cagrid.annualdemo.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.duke.cabig.javar.io.StatMLSerializationException;
import edu.duke.cabig.javar.io.StatMLSerializer;

import gridextensions.*;

public class MageToStatml
{
	public MageToStatml()
	{
		super();
	}
	
	public Data translate(String mage) 
		throws ParserConfigurationException, SAXException, IOException
	{
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		MageHandler handler = new MageHandler();
		parser.parse(new ByteArrayInputStream(mage.getBytes()), handler);
		return handler.data;
	}
		
	private class MageHandler
		extends DefaultHandler
	{
		Data data = new Data();
		private StringBuffer chars = new StringBuffer();
		
		public MageHandler()
		{
			super();
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException
		{
			chars.append(ch, start, length);
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException
		{
			if (qName.equals("UML:Package")) {
				
				chars.delete(0, chars.length());
			}
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
		{
			chars.delete(0, chars.length());
			
			String name = qName;
			int index = name.indexOf(":");
			if (index != -1) name = name.substring(index + 1);
			
			if (name.equals("MeasuredBioAssayData")) {
				System.out.println(atts.getValue("identifier"));
			}
		}
		
		public void endDocument() throws SAXException
		{
		}
	}		 
}