package gov.nih.nci.cagrid.introduce.portal.steps;

import junit.extensions.abbot.ScriptFixture;
import junit.framework.TestResult;

public class RemoveMultipleOperationsStep extends BaseStep {

	public RemoveMultipleOperationsStep() throws Exception {
		super();
	}
	
	public void runStep() throws Throwable {
		System.out.println("Removing Multiple Operations");

		// create ScriptFixture to test script
		ScriptFixture tester = new ScriptFixture(
				"/test/resources/abbot/RemoveMultipleOperationsStep.xml");

		// create TestResult to hold the result of the test
		TestResult tr = null;

		try {
			// run test
			tr = tester.run();
		} catch (Throwable e) {
			e.printStackTrace();
			fail("The remove multiple operations step test failed.");
		}
		
		if(tr.failureCount()>0){
			fail("The remove multiple operations step test failed.");
		}
	}

}
