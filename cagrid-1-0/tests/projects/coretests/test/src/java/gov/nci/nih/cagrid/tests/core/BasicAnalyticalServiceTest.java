/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.GlobusCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusDeployServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStartStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStopStep;

import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class BasicAnalyticalServiceTest
	extends AbstractServiceTest
{
	public BasicAnalyticalServiceTest()
	{
		super();
	}
	
	@SuppressWarnings("unchecked")
	protected Vector steps() 
	{
		super.init("BasicAnalyticalService");

		Vector steps = new Vector();
		steps.add(createServiceStep);
		steps.add(new GlobusCreateStep(globus));
		steps.add(new GlobusDeployServiceStep(globus, createServiceStep.getServiceDir()));
		steps.add(new GlobusStartStep(globus, port));
		try {
			addInvokeSteps(steps);
		} catch (Exception e) {
			throw new IllegalArgumentException("could not add invoke steps", e);
		}
		//steps.add(new CheckServiceMetadataStep(endpoint, metadataFile));
		steps.add(new GlobusStopStep(globus, port));
		steps.add(new GlobusCleanupStep(globus));
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
		TestResult result = runner.doRun(new TestSuite(BasicAnalyticalServiceTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
