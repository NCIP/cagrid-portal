/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.GlobusCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusDeployServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStartStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStopStep;
import gov.nci.nih.cagrid.tests.core.steps.WorkflowConfigureStep;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Story;

/**
 * This is an integration test that tests the functionality of the WorkflowManagementService. 
 * It deploys the service, deploys sample services, invokes a number of bpel docs, checks the 
 * results, and then tears it all down.
 * @testType integration
 * @steps GlobusCreateStep, GlobusDeployServiceStep, WorkflowConfigureStep
 * @steps GlobusStartStep, ServiceInvokeStep, GlobusStopStep, GlobusCleanupStep
 * @author Patrick McConnell
 */
public class WorkflowTest
	extends Story
{
	private GlobusHelper globus;
	private GlobusHelper secureGlobus;
	private int port;
	private int securePort;
	private File serviceDir;
	private File sampleServicesDir;
	private EndpointReferenceType endpoint;
	
	public WorkflowTest()
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
		secureGlobus = new GlobusHelper(true);
		secureGlobus.setUseCounterCheck(false);
		port = Integer.parseInt(System.getProperty("test.globus.port", "8080"));
		securePort = Integer.parseInt(System.getProperty("test.globus.secure.port", "8443"));		
		try {
			endpoint = new EndpointReferenceType(new Address("https://localhost:" + securePort + "/wsrf/services/cagrid/WorkflowManagementService"));
		} catch (MalformedURIException e) {
			throw new RuntimeException("unable to construct wms epr", e);
		}
		
		serviceDir = new File(System.getProperty("wms.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "workflow" + File.separator + "WorkflowManagementService"
		));
		sampleServicesDir = new File(System.getProperty("wokflow.samples.dir",
			".." + File.separator + "workflow-services"
		));
			
		File methodsDir = new File("test", 
			"resources" + File.separator + "WorkflowTest" + File.separator + "methods"
		);
			
		Vector steps = new Vector();
		steps.add(new GlobusCreateStep(globus));
		
		// deploy wms
		steps.add(new GlobusDeployServiceStep(secureGlobus, serviceDir));
		steps.add(new WorkflowConfigureStep(secureGlobus));

		// deploy workflow sample services
		File[] sampleServiceDirs = sampleServicesDir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory() & file.getName().matches("\\d+_\\w+");
			}
		});
		for (File sampleServiceDir : sampleServiceDirs) {
			steps.add(new GlobusDeployServiceStep(globus, secureGlobus, serviceDir));			
		}
		
		// start globus and invoke services
		steps.add(new GlobusStartStep(globus, port));
		steps.add(new GlobusStartStep(secureGlobus, securePort));
		try {
			AbstractServiceTest.addInvokeSteps(steps, serviceDir, serviceDir, methodsDir, endpoint);
		} catch (Exception e) {
			throw new RuntimeException("unable to add invoke steps", e);
		}
		
		// cleanup
		steps.add(new GlobusStopStep(globus, port));
		steps.add(new GlobusStopStep(secureGlobus, securePort));
		steps.add(new GlobusCleanupStep(globus));
		steps.add(new GlobusCleanupStep(secureGlobus));
		return steps;
	}

	public String getDescription()
	{
		return "WorfklowTest";
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
		TestResult result = runner.doRun(new TestSuite(WorkflowTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
