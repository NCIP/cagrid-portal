package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.util.GlobusHelper;

import java.io.File;

import com.atomicobject.haste.framework.Step;


public class DeployGlobusServiceStep extends Step {
	private GlobusHelper helper;
	private TestCaseInfo info;

	public DeployGlobusServiceStep(GlobusHelper helper, TestCaseInfo info) throws Exception {
		super();
		this.info = info;
		this.helper = helper;
	}


	public void runStep() throws Throwable {
		System.out.println("Deploying service " + info.getName());

		helper.deployService(new File(info.getDir()));
		
	}

}
