package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  DeleteOldServiceStep
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 22, 2006 
 * @version $Id$ 
 */
public class DeleteOldServiceStep extends Step {
	
	public DeleteOldServiceStep() {
		super();
	}
	

	public void runStep() throws Throwable {
		File oldServiceDir = new File(CreationTests.SERVICE_DIR);
		if (oldServiceDir.exists()) {
			boolean deleted = Utils.deleteDir(oldServiceDir);
			assertTrue("Deleting directory: " + oldServiceDir.getAbsolutePath(), deleted);
		} else {
			System.out.println("Service dir " + oldServiceDir.getAbsolutePath() + " did not exist...");
		}
	}

}
