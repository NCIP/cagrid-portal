package gov.nih.nci.cabig.introduce.steps;

import gov.nih.nci.cabig.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.common.CommonTools;

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
		boolean results = CommonTools.deleteDir(new File(pathtobasedir
				+ File.separator + tci.getDir()));
		System.out.println("__________________________" + results);
		assertTrue(results);
	
	}

}
