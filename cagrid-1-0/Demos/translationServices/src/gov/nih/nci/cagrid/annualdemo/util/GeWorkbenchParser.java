/*
 * Created on Jan 27, 2007
 */
package gov.nih.nci.cagrid.annualdemo.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GeWorkbenchParser
{
	private MicroarrayData microarrayData = new MicroarrayData();
	private ClusterData clusterData = new ClusterData();

	public GeWorkbenchParser()
	{
		super();
	}
	
	public void parseCluster(String xml)
		throws SAXException, IOException, ParserConfigurationException
	{
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		ClusterHandler handler = new ClusterHandler();
		parser.parse(new ByteArrayInputStream(xml.getBytes()), handler);
		clusterData.swapClusters();
	}
	
	public void parseMicroarray(String xml)
		throws SAXException, IOException, ParserConfigurationException
	{
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		MicroarrayHandler handler = new MicroarrayHandler();
		parser.parse(new ByteArrayInputStream(xml.getBytes()), handler);
	}
	
	private class MicroarrayHandler
		extends DefaultHandler
	{
		private StringBuffer chars = new StringBuffer();
		
		private ArrayList<Double> arrayData = new ArrayList<Double>();
		
		public MicroarrayHandler()
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
			String name = qName;
			int index = name.indexOf(":");
			if (index != -1) name = name.substring(index + 1);
			
			if (name.equals("arrayData")) {
				arrayData.add(Double.parseDouble(chars.toString().trim()));
			} else if (name.equals("arrayName")) {
				microarrayData.arrayNames.add(chars.toString().trim());
			} else if (name.equals("Microarray")) {
				double[] d = new double[arrayData.size()];
				for (int i = 0; i < d.length; i++) {
					d[i] = arrayData.get(i).doubleValue();
				}
				microarrayData.data.add(d);
				arrayData.clear();
			} else if (name.equals("markerName")) {
				microarrayData.geneNames.add(chars.toString().trim());
			}
			
			chars.delete(0, chars.length());
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
		{
			chars.delete(0, chars.length());
		}
		
		public void endDocument() throws SAXException
		{
		}
	}
	
	private class ClusterHandler
		extends DefaultHandler
	{
		private StringBuffer chars = new StringBuffer();

		private boolean isArrayCluster = false;
		private boolean isGeneCluster = false;
		private ClusterData.Cluster currentCluster = null;
		
		public ClusterHandler()
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
			String name = qName;
			int index = name.indexOf(":");
			if (index != -1) name = name.substring(index + 1);

			if (name.equals("leafLabel")) {
				currentCluster.name = chars.toString().trim();
			} else if (name.equals("leaf")) {
				if (chars.toString().trim().equals("true")) {
					currentCluster.isLeaf = true;
					if (isArrayCluster) clusterData.arrays.add(currentCluster.name);
					if (isGeneCluster) clusterData.genes.add(currentCluster.name);
				}
			} else if (name.equals("HierarchicalClusterNode")) {
				currentCluster = currentCluster.parent;
			}

			chars.delete(0, chars.length());
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
		{
			chars.delete(0, chars.length());
			
			String name = qName;
			int index = name.indexOf(":");
			if (index != -1) name = name.substring(index + 1);
			
			if (name.equals("microarrayCluster")) {
				currentCluster = clusterData.arrayCluster = new ClusterData.Cluster(null);
				isArrayCluster = true;
				isGeneCluster = false;
			} else if (name.equals("markerCluster")) {
				currentCluster = clusterData.geneCluster = new ClusterData.Cluster(null);
				isArrayCluster = false;
				isGeneCluster = true;
			} else if (name.equals("HierarchicalClusterNode")) {
				currentCluster = new ClusterData.Cluster(currentCluster);
			}
		}
		
		public void endDocument() throws SAXException
		{
		}
	}

	public ClusterData getClusterData()
	{
		return clusterData;
	}

	public MicroarrayData getMicroarrayData()
	{
		return microarrayData;
	}
}
