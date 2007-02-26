package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.introduce.test.util.GlobusHelper;

import com.atomicobject.haste.framework.Step;


public class StartGlobusStep extends Step {
	private GlobusHelper helper;

	public StartGlobusStep(GlobusHelper helper) throws Exception {
		super();
		this.helper = helper;
	}


	public void runStep() throws Throwable {
		System.out.println("Starting temporary globus");
		
		helper.startGlobus();
		Thread.sleep(10000);
	}

}
