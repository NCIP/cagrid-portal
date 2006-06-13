/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.CheckCaDSRServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.CleanupTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.ConfigureCaDSRServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.CreateTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.DeployGlobusServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.StartGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.StopGlobusStep;

import java.io.File;
import java.util.Vector;

import org.apache.axis.types.URI.MalformedURIException;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;

public class CaDSRServiceTest
	extends Story
{
	private GlobusHelper globus;
	private File serviceDir;
	private int port;
	
	public CaDSRServiceTest()
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
	}
	
	@SuppressWarnings("unchecked")
	protected Vector steps()		
	{
		globus = new GlobusHelper();
		port = Integer.parseInt(System.getProperty("test.globus.port", "8080"));
		serviceDir = new File(System.getProperty("cadsr.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "cadsr"
		));
			
		
		Vector steps = new Vector();
		steps.add(new CreateTempGlobusStep(globus));
		steps.add(new DeployGlobusServiceStep(globus, serviceDir));
		steps.add(new ConfigureCaDSRServiceStep(globus));
		steps.add(new StartGlobusStep(globus, port));
		try {
			steps.add(new CheckCaDSRServiceStep(port, null));
		} catch (MalformedURIException e) {
			throw new IllegalArgumentException("unable to instantiate CheckCaDSRStep", e);
		}
		steps.add(new StopGlobusStep(globus, port));
		steps.add(new CleanupTempGlobusStep(globus));
		return steps;
	}

	public String getDescription()
	{
		return "CaDSRServiceTest";
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
		TestResult result = runner.doRun(new TestSuite(CaDSRServiceTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
