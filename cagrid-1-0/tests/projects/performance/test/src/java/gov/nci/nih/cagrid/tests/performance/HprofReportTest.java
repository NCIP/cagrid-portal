/*
 * Created on May 30, 2006
 */
package gov.nci.nih.cagrid.tests.performance;

import gov.nci.nih.cagrid.tests.performance.HprofReport;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class HprofReportTest
	extends TestCase
{
	private class Junk
	{
		public Junk() { };
	}
	public HprofReportTest(String name)
	{
		super(name);
	}
	
	public void testReport() 
		throws Exception
	{
		Junk[] f = new Junk[10000];
		for (int i = 0; i < f.length; i++) f[i] = new Junk();
		
		com.yourkit.api.Controller controller = new com.yourkit.api.Controller("localhost", 4000);
		System.out.println("CPU snapshot");
		System.out.println(controller.captureCPUSnapshot(true));
		System.out.println("Memory snapshot");
		System.out.println(controller.captureMemorySnapshot(true));

		HprofReport report = new HprofReport();
		
		report.parse(new File(System.getProperty("hprof.file",
			"test" + File.separator + "data" + File.separator + "hprof" + File.separator + "hprof.txt"
		)));		
	}
	
	public static void main(String[] args) throws Exception
	{
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(HprofReportTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
