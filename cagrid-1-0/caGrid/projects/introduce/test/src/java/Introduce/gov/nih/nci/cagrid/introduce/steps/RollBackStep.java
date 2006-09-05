package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;

import com.atomicobject.haste.framework.Step;


public class RollBackStep extends BaseStep {
	private TestCaseInfo tci;


	public RollBackStep(TestCaseInfo tci) throws Exception {
		super(tci.getDir(),false);
		this.tci = tci;
	}


	public void runStep() throws Throwable {
		System.out.println("Rolling back to previous version.");

		ResourceManager.restoreLatest(String.valueOf(System.currentTimeMillis()), tci.getName(), tci.getDir());

		// check to see that this is same as before....
	}
}
