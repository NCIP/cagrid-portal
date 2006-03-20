/*
 * Created on Mar 16, 2006
 */
package gov.nih.nci.cagrid.cql2xpath;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CqlNodeParser
{
	public CqlNodeParser()
	{
		super();
	}
	
	public CqlNode[] parse(File file) 
		throws ParserConfigurationException, SAXException, IOException
	{
		SAXParserFactory spfactory = SAXParserFactory.newInstance();
		CqlMapHandler handler = new CqlMapHandler();
		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		try {
			SAXParser saxParser = spfactory.newSAXParser();
			InputSource is = new InputSource(in);
			saxParser.parse(is, handler);
		} finally {
			in.close();
		}
		
		return (CqlNode[]) handler.rootNodes.toArray(new CqlNode[0]);
	}
	
	protected class CqlMapHandler
		extends DefaultHandler
	{
		public ArrayList rootNodes = new ArrayList();
		private CqlNode node = null;

		public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
			throws SAXException
		{
			if (qName.equals("CqlNode")) {
				CqlNode node = new CqlNode();
				node.cqlName = atts.getValue("cqlName");
				node.xmlName = atts.getValue("xmlName");
				node.xmlPath = atts.getValue("xmlPath");
				if (this.node != null) {
					this.node.children.add(node);
					node.parent = this.node;
				}
				this.node = node;				
			} else if (qName.equals("CqlProperty")) {
				CqlNode.CqlProperty prop = new CqlNode.CqlProperty();
				prop.cqlName = atts.getValue("cqlName");
				prop.xmlName = atts.getValue("xmlName");
				prop.xmlPath = atts.getValue("xmlPath");
				this.node.properties.add(prop);
			}
		}
		   
		public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException 
		{	         
			if (qName.equals("CqlNode")) {
				if (node.parent == null) {
					this.rootNodes.add(node);
				}
				this.node = node.parent;
			}
		}
	}
	
	public static void main(String[] args)
		throws Exception
	{
		if (args.length == 0) args = new String[] { "queries\\scanFeatures_query1.xml" };
		
		CqlNodeParser parser = new CqlNodeParser();
		CqlNode[] nodes = parser.parse(new File(args[0]));
		for (int i = 0; i < nodes.length; i++) nodes[i].print(System.out);
	}
}