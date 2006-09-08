package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.introduce.util.GlobusHelper;

import com.atomicobject.haste.framework.Step;


public class CleanupGlobusStep extends Step {
	private GlobusHelper helper;


	public CleanupGlobusStep(GlobusHelper helper) throws Exception {
		super();
		this.helper = helper;
	}


	public void runStep() throws Throwable {
		System.out.println("removing temporary globus");

		helper.cleanupTempGlobus();
	}

}
