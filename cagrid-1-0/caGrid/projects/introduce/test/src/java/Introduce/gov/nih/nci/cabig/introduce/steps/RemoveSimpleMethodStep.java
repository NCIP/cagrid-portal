package gov.nih.nci.cabig.introduce.steps;

import gov.nih.nci.cabig.introduce.TestCaseInfo;

import com.atomicobject.haste.framework.Step;

public class RemoveSimpleMethodStep extends Step {
	private TestCaseInfo tci;
	
	public RemoveSimpleMethodStep(TestCaseInfo tci){
		this.tci = tci;
	}

	public void runStep() throws Throwable {
		System.out.println("Removing a simple method.");
		
	}

}
