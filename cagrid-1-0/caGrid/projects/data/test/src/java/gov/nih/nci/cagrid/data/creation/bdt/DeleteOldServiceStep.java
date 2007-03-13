package gov.nih.nci.cagrid.data.creation.bdt;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  DeleteOldServiceStep
 *  Deletes an old service directory
 * 
 * @author David Ervin
 * 
 * @created Mar 13, 2007 2:48:22 PM
 * @version $Id: DeleteOldServiceStep.java,v 1.1 2007-03-13 19:28:07 dervin Exp $ 
 */
public class DeleteOldServiceStep extends Step {
	private String serviceDir;
	
	public DeleteOldServiceStep(String serviceDir) {
		this.serviceDir = serviceDir;
	}
	

	public void runStep() throws Throwable {
		File dir = new File(serviceDir);
		if (dir.exists()) {
			Utils.deleteDir(dir);
		}
	}
}
