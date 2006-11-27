package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.introduce.util.GlobusHelper;

import com.atomicobject.haste.framework.Step;

/** 
 *  StartGlobusStep
 *  Step to start the Globus container with my data service deployed to it
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 8, 2006 
 * @version $Id: StartGlobusStep.java,v 1.2 2006-11-27 21:22:31 dervin Exp $ 
 */
public class StartGlobusStep extends Step {
	
	private GlobusHelper helper;
	
	public StartGlobusStep(GlobusHelper helper) {
		this.helper = helper;
	}

	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		assertFalse("Globus should not be running yet", helper.isGlobusRunning());
		helper.startGlobus();
		assertTrue("Globus should be running at this point", helper.isGlobusRunning());
	}
}
