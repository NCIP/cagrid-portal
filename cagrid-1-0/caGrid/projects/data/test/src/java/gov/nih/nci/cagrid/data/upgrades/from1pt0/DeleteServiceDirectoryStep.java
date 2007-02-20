package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  DeleteServiceDirectoryStep
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: DeleteServiceDirectoryStep.java,v 1.1 2007-02-20 21:03:17 dervin Exp $ 
 */
public class DeleteServiceDirectoryStep extends Step {
	private String serviceDir;
	
	public DeleteServiceDirectoryStep(String serviceDir) {
		this.serviceDir = serviceDir;
	}
	

	public void runStep() throws Throwable {
		File dir = new File(serviceDir);
		dir.delete();
	}
}
