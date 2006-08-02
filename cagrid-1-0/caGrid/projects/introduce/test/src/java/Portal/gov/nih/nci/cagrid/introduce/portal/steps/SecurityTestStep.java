package gov.nih.nci.cagrid.introduce.portal.steps;

import junit.extensions.abbot.ScriptFixture;
import junit.framework.TestResult;

public class SecurityTestStep extends BaseStep {

	public SecurityTestStep() throws Exception {
		super();
	}
	
	public void runStep() throws Throwable {
		System.out.println("Security Test Step");

		// create ScriptFixture to test script
		ScriptFixture tester = new ScriptFixture(this.getBaseDir()+
				"/test/resources/abbot/SecurityTestStep.xml");

		// create TestResult to hold the result of the test
		TestResult tr = null;

		try {
			// run test
			tr = tester.run();
		} catch (Throwable e) {
			e.printStackTrace();
			fail("The security test step test failed.");
		}
		
		if(tr.failureCount()>0){
			fail("The security test step test failed.");
		}
	}

}
