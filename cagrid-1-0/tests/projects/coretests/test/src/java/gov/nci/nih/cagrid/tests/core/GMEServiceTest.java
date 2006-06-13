/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.CheckCaDSRServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.CleanupGMEStep;
import gov.nci.nih.cagrid.tests.core.steps.CleanupTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.ConfigureCaDSRServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.CreateTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.DeployGlobusServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GetSchemaListStep;
import gov.nci.nih.cagrid.tests.core.steps.GetSchemaStep;
import gov.nci.nih.cagrid.tests.core.steps.PublishSchemaStep;
import gov.nci.nih.cagrid.tests.core.steps.StartGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.StopGlobusStep;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Story;

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
		
		new CleanupGMEStep().runStep();
	}
	
	@SuppressWarnings("unchecked")
	protected Vector steps()		
	{
		globus = new GlobusHelper();
		port = Integer.parseInt(System.getProperty("test.globus.port", "8080"));
		serviceDir = new File(System.getProperty("cadsr.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "gme"
		));
			
		
		Vector steps = new Vector();
		steps.add(new CreateTempGlobusStep(globus));
		steps.add(new DeployGlobusServiceStep(globus, serviceDir));
		steps.add(new StartGlobusStep(globus, port));
		try {
			steps.add(new PublishSchemaStep(port, null));
			steps.add(new GetSchemaStep(port, null));
			steps.add(new GetSchemaListStep(port, null, null));
		} catch (MalformedURIException e) {
			throw new IllegalArgumentException("unable to instantiate CheckCaDSRStep", e);
		}
		steps.add(new StopGlobusStep(globus, port));
		steps.add(new CleanupGMEStep());
		steps.add(new CleanupTempGlobusStep(globus));
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
