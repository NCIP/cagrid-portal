/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.CheckServiceMetadataStep;
import gov.nci.nih.cagrid.tests.core.steps.CleanupTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.CreateTempGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.DeployGlobusServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.StartGlobusStep;
import gov.nci.nih.cagrid.tests.core.steps.StopGlobusStep;

import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class BasicAnalyticalServiceWithMetadataTest
	extends AbstractServiceTest
{
	public BasicAnalyticalServiceWithMetadataTest()
	{
		super();
	}
	
	@SuppressWarnings("unchecked")
	protected Vector steps() 
	{
		super.init("BasicAnalyticalServiceWithMetadata");

		Vector steps = new Vector();
		steps.add(createServiceStep);
		steps.add(new CreateTempGlobusStep(globus));
		steps.add(new DeployGlobusServiceStep(globus, createServiceStep.getServiceDir()));
		steps.add(new StartGlobusStep(globus, port));
		try {
			addInvokeSteps(steps);
		} catch (Exception e) {
			throw new IllegalArgumentException("could not add invoke steps", e);
		}
		steps.add(new CheckServiceMetadataStep(endpoint, metadataFile));
		steps.add(new StopGlobusStep(globus, port));
		steps.add(new CleanupTempGlobusStep(globus));
		return steps;
	}

	public String getDescription()
	{
		return "BasicAnalyticalServiceWithMetadataTest";
	}

	/**
	 * Convenience method for running all the Steps in this Story.
	 */
	public static void main(String args[]) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(BasicAnalyticalServiceWithMetadataTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
