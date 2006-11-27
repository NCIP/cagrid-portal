package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.introduce.util.GlobusHelper;

import com.atomicobject.haste.framework.Step;

/** 
 *  DestroyTempGlobusStep
 *  Step to destroy the temporary testing Globus
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 8, 2006 
 * @version $Id: DestroyTempGlobusStep.java,v 1.2 2006-11-27 17:18:31 dervin Exp $ 
 */
public class DestroyTempGlobusStep extends Step {
	
	private GlobusHelper helper;
	
	public DestroyTempGlobusStep(GlobusHelper helper) {
		this.helper = helper;
	}
	

	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		helper.cleanupTempGlobus();
		if (helper.getTempGlobusLocation() != null && helper.getTempGlobusLocation().exists()) {
			helper.getTempGlobusLocation().deleteOnExit();
		}
	}
}
