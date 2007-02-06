/*
 * Created on Jan 27, 2007
 */
package gov.nih.nci.cagrid.annualdemo.util;

import edu.columbia.geworkbench.cagrid.microarray.MicroarraySet;
import gov.nih.nci.cagrid.common.Utils;
import gridextensions.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.namespace.QName;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class GeWorkbenchParserTest
	extends TestCase
{
	public GeWorkbenchParserTest(String name)
	{
		super(name);
	}
	
	public void testParseCluster()
		throws Exception
	{
		GeWorkbenchParser parser = new GeWorkbenchParser();
		parser.parseCluster(
			readText(new File("test", "resources" + File.separator + "geworkbench_cluster.xml"))
		);
		ClusterData data = parser.getClusterData();
		assertEquals(30, data.genes.size());
		assertEquals(200, data.arrays.size());
	}
	
	public void testParseMicroarray()
		throws Exception
	{
		GeWorkbenchParser parser = new GeWorkbenchParser();
		parser.parseMicroarray(
			readText(new File("test", "resources" + File.separator + "geworkbench_microarrayset.xml"))
		);
		MicroarrayData data = parser.getMicroarrayData();
		assertEquals(200, data.arrayNames.size());
		assertEquals(30, data.geneNames.size());
		assertEquals(30, data.data.size());
		assertEquals(200, data.data.get(0).length);
	}
	
	public void testParseMicroarray2()
		throws Exception
	{
		GeWorkbenchParser parser = new GeWorkbenchParser();
		parser.parseMicroarray(
			readText(new File("test", "resources" + File.separator + "geworkbench_microarrayset2.xml"))
		);
		MicroarrayData data = parser.getMicroarrayData();
		assertEquals(200, data.arrayNames.size());
		assertEquals(30, data.geneNames.size());
		assertEquals(30, data.data.size());
		assertEquals(200, data.data.get(0).length);
	}
	
	public void testRoundTrip()
		throws Exception
	{
		String xml = readText(new File("test", "resources" + File.separator + "geworkbench_microarrayset2.xml"));
		MicroarraySet ms = (MicroarraySet) Utils.deserializeObject(new StringReader(xml), MicroarraySet.class);

		GeWorkbenchParser parser = new GeWorkbenchParser();
		parser.parseMicroarray(xml);
		MicroarraySet ms2 = parser.convertToMicroarraySet();
		
		assertEquals(ms.getMarker().length, ms2.getMarker().length);
		assertEquals(ms.getMicroarray().length, ms2.getMicroarray().length);
		assertEquals(ms.getMarker()[0].getMarkerName(), ms2.getMarker()[0].getMarkerName());
		assertEquals(ms.getMarker()[0].getMarkerData().length, ms2.getMarker()[0].getMarkerData().length);
		assertEquals(ms.getMarker()[0].getMarkerData()[0], ms2.getMarker()[0].getMarkerData()[0]);
	}
	
	public void testConvertStatml()
		throws Exception
	{
		GeWorkbenchParser parser = new GeWorkbenchParser();
		parser.parseMicroarray(
			readText(new File("test", "resources" + File.separator + "geworkbench_microarrayset.xml"))
		);
		MicroarrayData data = parser.getMicroarrayData();

		GenePatternParser gpParser = new GenePatternParser();
		gpParser.setMicroarrayData(parser.getMicroarrayData());
		Data statml = gpParser.convertToStatml();
		Utils.serializeDocument(
			"test" + File.separator + "resources" + File.separator + "geworkbench_statml.xml", 
			statml, new QName("MicroarraySet")
		);
	}
	
	protected static String readText(File file) 
		throws IOException
	{
		StringBuffer sb = new StringBuffer((int) file.length());
		BufferedReader br = new BufferedReader(new FileReader(file));
		char[] ch = new char[1024];
		int count = -1;
		while ((count = br.read(ch)) != -1) sb.append(ch, 0, count);
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception
	{
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(GeWorkbenchParserTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
