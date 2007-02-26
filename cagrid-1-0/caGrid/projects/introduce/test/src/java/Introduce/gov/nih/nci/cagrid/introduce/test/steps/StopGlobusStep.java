package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.introduce.test.util.GlobusHelper;

import com.atomicobject.haste.framework.Step;


public class StopGlobusStep extends Step {
	private GlobusHelper helper;


	public StopGlobusStep(GlobusHelper helper) throws Exception {
		super();
		this.helper = helper;
	}


	public void runStep() throws Throwable {
		System.out.println("stopping temporary globus");

		helper.stopGlobus();

		assertFalse(helper.isGlobusRunning());
	}

}
