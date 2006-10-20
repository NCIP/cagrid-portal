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
import gov.nci.nih.cagrid.tests.core.util.ServiceHelper;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * This is an integration test that tests the Introduce functionality of creating an 
 * analytical service with transport-level security and deploying it.  It creates a 
 * service from scratch, deploys it, and attempts to invoke a method on it.  Dorian
 * is also deployed and used to gain user credentials.
 * @testType integration
 * @steps ServiceCreateStep, 
 * @steps GlobusCreateStep, GTSSyncOnceStep, GlobusDeployServiceStep, GlobusStartStep
 * @steps DorianAuthenticateStep, DorianAddTrustedCAStep
 * @steps ServiceInvokeStep, ServiceCheckMetadataStep
 * @steps GlobusStopStep, GlobusCleanupStep
 * @steps DorianCleanupStep, DorianDestroyDefaultProxyStep
 * @author Patrick McConnell
 */
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
		
		ServiceHelper echoHelper = new ServiceHelper("IntroduceEcho"); 
		
		File dorianDir = new File(System.getProperty("dorian.dir",
			".." + File.separator + ".." + File.separator + ".." + File.separator + 
			"caGrid" + File.separator + "projects" + File.separator + "dorian"
		));
		caFile = new File(
			System.getProperty("user.home"), 
			".globus" + File.separator + "certificates" + File.separator + "BasicAnalyticalServiceWithSecurityTest_ca.1"
		);

		Vector steps = new Vector();
		steps.add(echoHelper.getCreateServiceStep());
		steps.add(getCreateServiceStep());
		steps.add(new GlobusCreateStep(getGlobus()));
		steps.add(new GTSSyncOnceStep(getGlobus()));
		steps.add(new GlobusDeployServiceStep(getGlobus(), dorianDir));
		steps.add(new GlobusDeployServiceStep(getGlobus(), echoHelper.getCreateServiceStep().getServiceDir()));
		steps.add(new GlobusDeployServiceStep(getGlobus(), getCreateServiceStep().getServiceDir()));
		steps.add(new GlobusStartStep(getGlobus(), getPort()));
		steps.add(new DorianAuthenticateStep("dorian", "password", getPort()));
		steps.add(new DorianAddTrustedCAStep(caFile, getPort()));
		try {
			addInvokeSteps(steps);
		} catch (Exception e) {
			throw new IllegalArgumentException("could not add invoke steps", e);
		}
		steps.add(new GlobusStopStep(getGlobus(), getPort()));
		steps.add(new GlobusCleanupStep(getGlobus()));
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
