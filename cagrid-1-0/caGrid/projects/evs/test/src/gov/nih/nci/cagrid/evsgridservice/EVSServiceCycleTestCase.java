package gov.nih.nci.cagrid.evsgridservice;

import gov.nih.nci.cagrid.core.CycleTestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;


public class EVSServiceCycleTestCase extends CycleTestCase {
	public EVSServiceCycleTestCase(String name) {
		super(name);
	}


	public static void main(String args[]) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(EVSServiceCycleTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
