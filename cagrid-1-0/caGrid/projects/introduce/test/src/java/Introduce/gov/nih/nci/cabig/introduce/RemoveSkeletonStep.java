package gov.nih.nci.cabig.introduce;

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
		assertTrue(deleteDir(new File(pathtobasedir + File.separator
				+ TestCaseInfo.dir)));
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

}
