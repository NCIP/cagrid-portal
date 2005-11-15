package gov.nih.nci.cabig.introduce;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class SimpleTest extends TestCase {
	
	public void testAlive(){
		
		
	}
	
	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(SimpleTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
