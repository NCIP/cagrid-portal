package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.introduce.util.GlobusHelper;

import com.atomicobject.haste.framework.Step;

/** 
 *  StopGlobusStep
 *  Shuts down the Globus container I've been testing against
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 8, 2006 
 * @version $Id: StopGlobusStep.java,v 1.2 2006-11-27 19:38:22 dervin Exp $ 
 */
public class StopGlobusStep extends Step {
	
	private GlobusHelper helper;
	
	public StopGlobusStep(GlobusHelper helper) {
		this.helper = helper;
	}
	

	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		System.out.println("stopping temporary globus");

		helper.stopGlobus();

		assertFalse(helper.isGlobusRunning());
	}
}
