package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.introduce.test.util.GlobusHelper;

import com.atomicobject.haste.framework.Step;

/**
 * StopGlobusStep Shuts down the Globus container I've been testing against
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: StopGlobusStep.java,v 1.3 2007-02-26 20:24:27 hastings Exp $
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
