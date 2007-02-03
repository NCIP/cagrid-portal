/*
 * Created on Jan 27, 2007
 */
package gov.nih.nci.cagrid.annualdemo.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class TreeViewHelperTest
extends TestCase
{
	public TreeViewHelperTest(String name)
	{
		super(name);
	}
	
	public void testWriteTreeViewFiles()
		throws Exception
	{
		File dir = new File("test", "resources");
		GeWorkbenchParser parser = new GeWorkbenchParser();

		parser.parseCluster(
			GeWorkbenchParserTest.readText(new File(dir, "geworkbench_cluster.xml"))
		);
		ClusterData clusterData = parser.getClusterData();

		parser.parseMicroarray(
			GeWorkbenchParserTest.readText(new File(dir, "geworkbench_microarrayset.xml"))
		);
		MicroarrayData microarrayData = parser.getMicroarrayData();
		
		TreeViewHelper helper = new TreeViewHelper(microarrayData, clusterData);
		
		File cdtFile = new File(dir, "geworkbench_test.cdt");
		File atrFile = new File(dir, "geworkbench_test.atr");
		File gtrFile = new File(dir, "geworkbench_test.gtr");

		PrintWriter cdtOut = new PrintWriter(new BufferedWriter(new FileWriter(cdtFile)));
		PrintWriter atrOut = new PrintWriter(new BufferedWriter(new FileWriter(atrFile)));
		PrintWriter gtrOut = new PrintWriter(new BufferedWriter(new FileWriter(gtrFile)));
		
		try {
			helper.writeCdt(cdtOut);
			helper.writeAtr(atrOut);
			helper.writeGtr(gtrOut);
		} finally {
			cdtOut.flush();
			cdtOut.close();
			atrOut.flush();
			atrOut.close();
			gtrOut.flush();
			gtrOut.close();
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(TreeViewHelperTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
