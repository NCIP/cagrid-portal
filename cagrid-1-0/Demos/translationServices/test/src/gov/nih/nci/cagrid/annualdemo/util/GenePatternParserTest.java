/*
 * Created on Feb 2, 2007
 */
package gov.nih.nci.cagrid.annualdemo.util;

import gov.nih.nci.cagrid.common.Utils;
import gridextensions.Cluster;
import gridextensions.Data;

import java.io.File;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class GenePatternParserTest
extends TestCase
{
	public GenePatternParserTest(String name)
	{
		super(name);
	}
	
	public void testParseCluster()
		throws Exception
	{
		Cluster[] clusters = new Cluster[] {
			new Cluster(new String[] { "node1", "node2" }, "root"),
			new Cluster(new String[] { "leaf1", "leaf2" }, "node1"),
			new Cluster(new String[] { "node3", "node4" }, "node2"),
			new Cluster(new String[] { "leaf3", "leaf4" }, "node3"),
			new Cluster(new String[] { "leaf5", "leaf6" }, "node4"),
			new Cluster(null, "leaf1"),
			new Cluster(null, "leaf2"),
			new Cluster(null, "leaf3"),
			new Cluster(null, "leaf4"),
			new Cluster(null, "leaf5"),
			new Cluster(null, "leaf6"),
		};
		
		GenePatternParser parser = new GenePatternParser();
		parser.parseCluster(clusters);
		
		ClusterData data = parser.getClusterData();
		
		assertEquals(clusters.length, data.genes.size());
		assertEquals(clusters[7].getName(), data.genes.get(7));
		
		assertNotNull(data.geneCluster);
		assertNotNull(data.geneCluster.cluster2);
		assertNotNull(data.geneCluster.cluster2.cluster1);
		assertNotNull(data.geneCluster.cluster2.cluster1.cluster2);
		assertNotNull(data.geneCluster.cluster2.cluster1.cluster2.name);
		assertEquals("leaf4", data.geneCluster.cluster2.cluster1.cluster2.name);
	}
	
	public void testParseMicroarray()
		throws Exception
	{
		Data data = (Data) Utils.deserializeDocument(
			"test" + File.separator + "resources"  + File.separator + "genepattern_statml.xml",
			Data.class
		);

		GenePatternParser parser = new GenePatternParser();
		parser.parseMicroarray(data);
		MicroarrayData microarray = parser.getMicroarrayData();
		assertEquals(2, microarray.geneNames.size());
		assertEquals(3, microarray.arrayNames.size());
		assertEquals(2, microarray.data.size());
		assertEquals(3, microarray.data.get(0).length);
	}
	
	public void testRoundTrip()
		throws Exception
	{
		Data data = (Data) Utils.deserializeDocument(
			"test" + File.separator + "resources"  + File.separator + "genepattern_statml.xml",
			Data.class
		);

		GenePatternParser parser = new GenePatternParser();
		parser.parseMicroarray(data);
		Data data2 = parser.convertToStatml();
		
		assertEquals(data.getArray().length, data2.getArray().length);
		assertEquals(data.getArray(0).getBase64Value(), data2.getArray(0).getBase64Value());
		assertEquals(data.getArray(2).getBase64Value(), data2.getArray(2).getBase64Value());
		assertEquals(data.getArray(3).getBase64Value(), data2.getArray(3).getBase64Value());
		assertEquals(data.getArray(4).getBase64Value(), data2.getArray(4).getBase64Value());
	}
	
	public static void main(String[] args) throws Exception
	{
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(GenePatternParserTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
