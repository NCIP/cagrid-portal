package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.introduce.test.util.GlobusHelper;

import com.atomicobject.haste.framework.Step;


public class CreateGlobusStep extends Step {
	private GlobusHelper helper;

	public CreateGlobusStep(GlobusHelper helper) throws Exception {
		super();
		this.helper = helper;
	}


	public void runStep() throws Throwable {
		System.out.println("Creating temporary globus");

		helper.createTempGlobus();
		
	}

}
