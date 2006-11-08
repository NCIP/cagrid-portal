package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.introduce.util.GlobusHelper;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  DeployDataServiceStep
 *  Deploys the data service to the globus container
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 8, 2006 
 * @version $Id: DeployDataServiceStep.java,v 1.1 2006-11-08 18:09:38 dervin Exp $ 
 */
public class DeployDataServiceStep extends Step {
	
	private GlobusHelper helper;
	private String serviceBase;
	
	public DeployDataServiceStep(GlobusHelper helper, String serviceBaseDir) {
		this.helper = helper;
		this.serviceBase = serviceBaseDir;
	}
	

	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		File serviceBaseDir = new File(serviceBase);
		helper.deployService(serviceBaseDir);
	}
}
