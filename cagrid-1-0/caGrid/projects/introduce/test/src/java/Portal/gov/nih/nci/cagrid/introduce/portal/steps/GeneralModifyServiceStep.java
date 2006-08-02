package gov.nih.nci.cagrid.introduce.portal.steps;

import junit.extensions.abbot.ScriptFixture;
import junit.framework.TestResult;

public class GeneralModifyServiceStep extends BaseStep {

	public GeneralModifyServiceStep() throws Exception {
		super();
	}
	
	public void runStep() throws Throwable {
		System.out.println("General Modify Service Step");

		// create ScriptFixture to test script
		ScriptFixture tester = new ScriptFixture(this.getBaseDir()+
				"/test/resources/abbot/GeneralModifyServiceStep.xml");

		// create TestResult to hold the result of the test
		TestResult tr = null;

		try {
			// run test
			tr = tester.run();
		} catch (Throwable e) {
			e.printStackTrace();
			fail("The general modify service step test failed.");
		}
		
		if(tr.failureCount()>0){
			fail("The general modify service step test failed.");
		}
	}

}
