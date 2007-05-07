/**
 * 
 */
package org.cagrid.rav.test.steps;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;

import org.cagrid.rav.test.unit.CreationTest;

import com.atomicobject.haste.framework.Step;

/**
 * @author madduri
 *
 */
public class DeleteOldServiceStep extends Step {

	/**
	 * 
	 */
	public DeleteOldServiceStep() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.atomicobject.haste.framework.Step#runStep()
	 */
	public void runStep() throws Throwable {
		File oldServiceDir = new File(CreationTest.SERVICE_DIR);
		if (oldServiceDir.exists()) {
			boolean deleted = Utils.deleteDir(oldServiceDir);
			assertTrue("Failed to delete directory: " + oldServiceDir.getAbsolutePath(), deleted);
		} else {
			System.out.println("Service dir " + oldServiceDir.getAbsolutePath() + " did not exist...");
		}
	}

}
