package gov.nih.nci.cagrid.data.creation.enumeration;

import gov.nih.nci.cagrid.introduce.util.FileUtils;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  DeleteOldServiceStep
 *  Deletes any old enumeration data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 30, 2006 
 * @version $Id: DeleteOldServiceStep.java,v 1.1 2006-12-18 14:48:47 dervin Exp $ 
 */
public class DeleteOldServiceStep extends Step {

	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		File serviceDir = new File(CreateEnumerationTests.SERVICE_DIR);
		if (serviceDir.exists()) {
			System.out.println("Deleting old service directory");
			FileUtils.deleteRecursive(serviceDir);
		}
	}
}
