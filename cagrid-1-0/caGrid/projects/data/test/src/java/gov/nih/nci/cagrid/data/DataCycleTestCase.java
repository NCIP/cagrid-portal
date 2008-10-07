package gov.nih.nci.cagrid.data;

import gov.nih.nci.cagrid.core.CycleTestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;


public class DataCycleTestCase extends CycleTestCase {

	public DataCycleTestCase(String name) {
		super(name);
	}


	public static void main(String args[]) {

		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(DataCycleTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}