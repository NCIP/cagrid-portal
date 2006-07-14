/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.CheckGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.CleanupTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.CreateTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.DeployGlobusServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.StartGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.StopGlobusStep;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Story;

public class GlobusHelperTest
	extends Story
{
	private GlobusHelper globus;
	private int port;
	private GlobusHelper secureGlobus;
	private int securePort;
	
	public GlobusHelperTest()
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
		if (secureGlobus != null) {
			secureGlobus.stopGlobus(securePort);
			secureGlobus.cleanupTempGlobus();
		}
}
	
	@SuppressWarnings("unchecked")
	protected Vector steps()		
	{
		globus = new GlobusHelper();
		port = Integer.parseInt(System.getProperty("test.globus.port", "8080"));
		secureGlobus = new GlobusHelper(true);
		securePort = Integer.parseInt(System.getProperty("test.globus.secure.port", "8443"));

		Vector steps = new Vector();

		steps.add(new CreateTempGlobusStep(globus));
		steps.add(new StartGlobusStep(globus, port));
		try {
			steps.add(new CheckGlobusStep(port));
		} catch (MalformedURIException e) {
			throw new IllegalArgumentException("endpoint badly formed", e);
		}
		steps.add(new StopGlobusStep(globus, port));
		steps.add(new CleanupTempGlobusStep(globus));

		steps.add(new CreateTempGlobusStep(secureGlobus));
		secureGlobus.setUseCounterCheck(false);
		steps.add(new DeployGlobusServiceStep(secureGlobus, new File("..", "echo")));
		steps.add(new StartGlobusStep(secureGlobus, securePort));
		steps.add(new StopGlobusStep(secureGlobus, securePort));
		steps.add(new CleanupTempGlobusStep(secureGlobus));
		
		return steps;
	}

	public String getDescription()
	{
		return "GlobusHelperTest";
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
		TestResult result = runner.doRun(new TestSuite(GlobusHelperTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
