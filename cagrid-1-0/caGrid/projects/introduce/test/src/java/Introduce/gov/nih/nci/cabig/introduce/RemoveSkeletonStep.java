package gov.nih.nci.cabig.introduce;

import gov.nih.nci.cagrid.common.CommonTools;

import java.io.File;

import com.atomicobject.haste.framework.Step;

public class RemoveSkeletonStep extends Step {

	public void runStep() throws Throwable {
		System.out.println("Removing the service skeleton");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if (pathtobasedir == null) {
			System.out.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}
		assertTrue(CommonTools.deleteDir(new File(pathtobasedir
				+ File.separator + TestCaseInfo.dir)));
	}

}
