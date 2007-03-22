package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  DeleteOldServiceStep
 *  This step deletes an old service's directory from the file system
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 22, 2006 
 * @version $Id$ 
 */
public class DeleteOldServiceStep extends Step {
    private TestServiceInfo serviceInfo;
	
	public DeleteOldServiceStep(TestServiceInfo serviceInfo) {
		super();
        this.serviceInfo = serviceInfo;
	}
	

	public void runStep() throws Throwable {
		File oldServiceDir = new File(serviceInfo.getDir());
		if (oldServiceDir.exists()) {
			boolean deleted = Utils.deleteDir(oldServiceDir);
			assertTrue("Failed to delete directory: " + oldServiceDir.getAbsolutePath(), deleted);
		} else {
			System.out.println("Service dir " + oldServiceDir.getAbsolutePath() + " did not exist...");
		}
	}
}
