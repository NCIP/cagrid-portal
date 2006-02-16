package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;

import com.atomicobject.haste.framework.Step;


public class RollBackStep extends Step {
	private TestCaseInfo tci;


	public RollBackStep(TestCaseInfo tci) {
		this.tci = tci;
	}


	public void runStep() throws Throwable {
		System.out.println("Rolling back to previous version.");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if (pathtobasedir == null) {
			System.err.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}

		ResourceManager.restoreLatest(String.valueOf(System.currentTimeMillis()), tci.getName(), tci.getDir());

		// check to see that this is same as before....
	}

}
