package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;

import java.io.File;

import com.atomicobject.haste.framework.Step;

public class RemoveSkeletonStep extends Step {
	private TestCaseInfo tci;
	
	public RemoveSkeletonStep(TestCaseInfo tci){
		this.tci = tci;
	}

	public void runStep() throws Throwable {
		System.out.println("Removing the service skeleton");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if (pathtobasedir == null) {
			System.out.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}
		Thread.sleep(5000);
		boolean results = Utils.deleteDir(new File(pathtobasedir
				+ File.separator + tci.getDir()));
		System.out.println("__________________________" + results);
		assertTrue(results);
	
	}

}
