/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.GMECleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.GMEConfigureStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusDeployServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GMEGetSchemaListStep;
import gov.nci.nih.cagrid.tests.core.steps.GMEGetSchemaStep;
import gov.nci.nih.cagrid.tests.core.steps.GMEPublishSchemaStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStartStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStopStep;
import gov.nci.nih.cagrid.tests.core.steps.StackTraceStep;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Story;

/**
 * This is an integration test that tests the functionality of the GME grid service. 
 * It deploys the service, adds some schemas, retrieves the schemas, and lists the schemas.
 * @testType integration
 * @steps ServiceCreateStep, 
 * @steps GlobusCreateStep, GlobusDeployServiceStep, GMEConfigureStep, GlobusStartStep
 * @steps GMEPublishSchemaStep, GMEGetSchemaStep, GMEGetSchemaListStep
 * @steps GlobusStopStep, GMECleanupStep, GlobusCleanupStep
 * @author Patrick McConnell
 */
public class GMEServiceTest
	extends Story
{
	private GlobusHelper globus;
	private File serviceDir;
	private int port;
	
	public GMEServiceTest()
	{
		super();
	}

	protected boolean storySetUp() 
		throws Throwable
	{
		return true;
	}

	protected void storyTearDown() 
		throws Throwable
	{
		if (globus != null) {
			globus.stopGlobus(port);
			globus.cleanupTempGlobus();
		}
		
		new GMECleanupStep().runStep();
	}
	
	@SuppressWarnings("unchecked")
	protected Vector steps()		
	{
		globus = new GlobusHelper();
		port = Integer.parseInt(System.getProperty("test.globus.port", "8080"));
		serviceDir = new File(System.getProperty("gme.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "gme"
		));
			
		File schemaRoot = new File("test", 
			"resources" + File.separator + "GMEServiceTest" + File.separator + "schema"
		);
		File logFile = null;
		if (System.getProperty("junit.results.dir") != null) {
			logFile = new File(System.getProperty("junit.results.dir"), "GMEServiceTest_haste.txt");
		}
		
		Vector steps = new Vector();
		steps.add(new StackTraceStep(logFile));
		steps.add(new GlobusCreateStep(globus));
		steps.add(new GlobusDeployServiceStep(globus, serviceDir));
		steps.add(new GMEConfigureStep(globus));
		steps.add(new GlobusStartStep(globus, port));
		try {
			File[] schemaDirs = schemaRoot.listFiles(new FileFilter() {
				public boolean accept(File file) {
					return file.isDirectory() && ! file.getName().equals("CVS");
				}
			});

			for (File schemaDir : schemaDirs) {
				File[] schemaFiles = schemaDir.listFiles(new FileFilter() {
					public boolean accept(File file) {
						return file.getName().endsWith(".xsd");
					}
				});
				
				for (File schemaFile : schemaFiles) {
					steps.add(new GMEPublishSchemaStep(port, schemaFile));
				}
				for (File schemaFile : schemaFiles) {
					steps.add(new GMEGetSchemaStep(port, schemaFile));
				}
				for (File schemaFile : schemaFiles) {
					steps.add(new GMEGetSchemaListStep(port, schemaFile));
				}
			}
		} catch (MalformedURIException e) {
			throw new IllegalArgumentException("unable to instantiate CheckCaDSRStep", e);
		}
		steps.add(new GlobusStopStep(globus, port));
		steps.add(new GMECleanupStep());
		steps.add(new GlobusCleanupStep(globus));
		return steps;
	}

	public String getDescription()
	{
		return "GMEServiceTest";
	}
	
	/**
	 * used to make sure that if we are going to use a junit testsuite to test
	 * this that the test suite will not error out looking for a single test......
	 */
	public void testDummy() throws Throwable {
	}

	/**
	 * Convenience method for running all the Steps in this Story.
	 */
	public static void main(String args[]) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(GMEServiceTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
