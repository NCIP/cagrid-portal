package gov.nih.nci.cagrid.introduce.extensions;

import gov.nih.nci.cagrid.core.CycleTestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;


public class ExtentionsTestCase extends CycleTestCase {

	public ExtentionsTestCase(String name) {
		super(name);
	}


	public static void main(String args[]) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(ExtentionsTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}