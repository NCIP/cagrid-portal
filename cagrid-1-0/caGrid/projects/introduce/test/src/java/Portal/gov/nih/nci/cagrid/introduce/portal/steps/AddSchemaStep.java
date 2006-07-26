package gov.nih.nci.cagrid.introduce.portal.steps;

import junit.extensions.abbot.ScriptFixture;
import junit.framework.TestResult;

public class AddSchemaStep extends BaseStep {

	public AddSchemaStep() throws Exception {
		super();
	}
	
	public void runStep() throws Throwable {
		System.out.println("Adding a Schema");

		// create ScriptFixture to test script
		ScriptFixture tester = new ScriptFixture(
				"/test/resources/abbot/AddSchemaStep.xml");

		// create TestResult to hold the result of the test
		TestResult tr = null;

		try {
			// run test
			tr = tester.run();
		} catch (Throwable e) {
			e.printStackTrace();
			fail("The add schema step test failed.");
		}
		
		if(tr.failureCount()>0){
			fail("The add schema step test failed.");
		}
	}

}
