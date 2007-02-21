package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.upgrade.IntroduceUpgradeManager;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  UpgradeIntroduceServiceStep
 *  Upgrades introduce managed parts of the service from 1.0 to 1.1
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 21, 2007 
 * @version $Id: UpgradeIntroduceServiceStep.java,v 1.1 2007-02-21 16:09:50 dervin Exp $ 
 */
public class UpgradeIntroduceServiceStep extends Step {
	private String serviceDir;
	
	public UpgradeIntroduceServiceStep(String serviceDir) {
		this.serviceDir = serviceDir;
	}
	

	public void runStep() throws Throwable {
		// get the service description
		ServiceDescription serviceDesc = (ServiceDescription) Utils.deserializeDocument(
			serviceDir + File.separator + "introduce.xml", ServiceDescription.class);
		// create the introduce upgrade manager
		IntroduceUpgradeManager upgrader = new IntroduceUpgradeManager(serviceDesc, serviceDir);
		assertTrue("Introduce service needs to be upgraded to 1.1", upgrader.needsUpgrading());
		upgrader.upgrade();
	}
}
