/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.DorianAddTrustedCAStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianAuthenticateStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.DorianDestroyDefaultProxyStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusDeployServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStartStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStopStep;
import gov.nci.nih.cagrid.tests.core.steps.GTSSyncOnceStep;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class BasicAnalyticalServiceWithSecurityTest
	extends AbstractServiceTest
{
	private File caFile;

	public BasicAnalyticalServiceWithSecurityTest()
	{
		super();
	}

	protected void storyTearDown() 
		throws Throwable
	{
		if (caFile != null) caFile.delete();
		super.storyTearDown();
		new DorianDestroyDefaultProxyStep().runStep();
		new DorianCleanupStep().runStep();
	}
	
	@SuppressWarnings("unchecked")
	protected Vector steps() 
	{
		super.init("BasicAnalyticalServiceWithSecurity");
		
		File dorianDir = new File(System.getProperty("dorian.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "dorian"
		));
		caFile = new File(
			System.getProperty("user.home"), 
			".globus" + File.separator + "certificates" + File.separator + "BasicAnalyticalServiceWithSecurityTest_ca.1"
		);

		Vector steps = new Vector();
		steps.add(createServiceStep);
		steps.add(new GlobusCreateStep(globus));
		steps.add(new GTSSyncOnceStep(globus));
		steps.add(new GlobusDeployServiceStep(globus, dorianDir));
		steps.add(new GlobusDeployServiceStep(globus, new File("..", "echo")));
		steps.add(new GlobusDeployServiceStep(globus, createServiceStep.getServiceDir()));
		steps.add(new GlobusStartStep(globus, port));
		steps.add(new DorianAuthenticateStep("dorian", "password", port));
		steps.add(new DorianAddTrustedCAStep(caFile, port));
		try {
			addInvokeSteps(steps);
		} catch (Exception e) {
			throw new IllegalArgumentException("could not add invoke steps", e);
		}
		steps.add(new GlobusStopStep(globus, port));
		steps.add(new GlobusCleanupStep(globus));
		steps.add(new DorianCleanupStep());
		steps.add(new DorianDestroyDefaultProxyStep());
		return steps;
	}

	public String getDescription()
	{
		return "BasicAnalyticalServiceTest";
	}

	/**
	 * Convenience method for running all the Steps in this Story.
	 */
	public static void main(String args[]) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(BasicAnalyticalServiceWithSecurityTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
