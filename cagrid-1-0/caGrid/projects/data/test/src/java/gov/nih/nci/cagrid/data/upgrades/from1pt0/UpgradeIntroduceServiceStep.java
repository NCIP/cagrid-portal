package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.upgrade.IntroduceUpgradeManager;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  UpgradeIntroduceServiceStep
 *  Upgrades introduce managed parts of the service from 1.0 to 1.1
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 21, 2007 
 * @version $Id: UpgradeIntroduceServiceStep.java,v 1.5 2007-04-12 22:02:05 hastings Exp $ 
 */
public class UpgradeIntroduceServiceStep extends Step {
	private String serviceDir;
	
	public UpgradeIntroduceServiceStep(String serviceDir) {
		this.serviceDir = serviceDir;
	}
	

	public void runStep() throws Throwable {
		// create the introduce upgrade manager
		IntroduceUpgradeManager upgrader = new IntroduceUpgradeManager(serviceDir);
		assertTrue("Introduce service should have required upgrade to 1.1", upgrader.needsUpgrading());
		upgrader.upgrade();
	}
}
