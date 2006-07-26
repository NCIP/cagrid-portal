package gov.nih.nci.cagrid.introduce.portal.steps;

import junit.extensions.abbot.ScriptFixture;
import junit.framework.TestResult;

public class ModifyMultipleOperationsStep extends BaseStep {

	public ModifyMultipleOperationsStep() throws Exception {
		super();
	}
	
	public void runStep() throws Throwable {
		System.out.println("Modifying Multiple Operations");

		// create ScriptFixture to test script
		ScriptFixture tester = new ScriptFixture(
				"/test/resources/abbot/ModifyMultipleOperationsStep.xml");

		// create TestResult to hold the result of the test
		TestResult tr = null;

		try {
			// run test
			tr = tester.run();
		} catch (Throwable e) {
			e.printStackTrace();
			fail("The modify multiple operations step test failed.");
		}
		
		if(tr.failureCount()>0){
			fail("The modify multiple operations step test failed.");
		}
	}

}
