package gov.nih.nci.cagrid.introduce.portal.steps;

import junit.extensions.abbot.ScriptFixture;
import junit.framework.TestResult;

public class CreateServiceStep extends BaseStep {

	public CreateServiceStep() throws Exception {
		super();
	}

	public void runStep() throws Throwable {
		System.out.println("Creating a service");

		// create ScriptFixture to test script
		ScriptFixture tester = new ScriptFixture(this.getBaseDir()+
				"/test/resources/abbot/CreateServiceStep.xml");

		// create TestResult to hold the result of the test
		TestResult tr = null;

		try {
			// run test
			tr = tester.run();
		} catch (Throwable e) {
			e.printStackTrace();
			fail("The create service step failed.");
		}
		
		if(tr.failureCount()>0){
			fail("The create service step failed.");
		}
	}

}
