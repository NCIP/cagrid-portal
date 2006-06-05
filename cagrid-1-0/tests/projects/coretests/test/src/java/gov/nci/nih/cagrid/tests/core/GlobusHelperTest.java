/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.CheckServiceMetadataStep;
import gov.nci.nih.cagrid.tests.core.steps.CleanupTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.CreateTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.DeployGlobusServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.InvokeTestGridServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.StartGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.StopGlobusStep;

import java.io.File;
import java.util.Vector;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.Story;

public class GlobusHelperTest
	extends Story
{
	private GlobusHelper globus;
	private File serviceDir;
	private int port;
	
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
	}
	
	@SuppressWarnings("unchecked")
	protected Vector steps()		
	{
		globus = new GlobusHelper();
		serviceDir = new File(System.getProperty("test.service.dir", ".." + File.separator + "BasicAnalyticalService"));
		port = Integer.parseInt(System.getProperty("test.globus.port", "8080"));

		EndpointReferenceType endpoint;
		try {
			endpoint = new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/cagrid/BasicAnalyticalService"));
		} catch (MalformedURIException e) {
			throw new IllegalArgumentException("endpoint badly formed");
		}
		File metadataFile = new File(System.getProperty("GlobusHelperTest.file", 
			"test" + File.separator + "data" + File.separator + "serviceMetadata.xml"
		));

		Vector steps = new Vector();
		steps.add(new CreateTempGlobusStep(globus));
		steps.add(new DeployGlobusServiceStep(globus, serviceDir));
		steps.add(new StartGlobusStep(globus, port));
		steps.add(new InvokeTestGridServiceStep(port));
		steps.add(new CheckServiceMetadataStep(endpoint, metadataFile));
		steps.add(new StopGlobusStep(globus, port));
		steps.add(new CleanupTempGlobusStep(globus));
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
