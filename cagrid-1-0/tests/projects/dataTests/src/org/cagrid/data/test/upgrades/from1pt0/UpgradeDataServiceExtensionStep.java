package org.cagrid.data.test.upgrades.from1pt0;

import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.upgrade.ExtensionsUpgradeManager;
import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgradeStatus;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;

/** 
 *  UpgradeServiceStep
 *  Upgrades the 1.0 service to the 1.1 version of Introduce
 *  and the 1.1 version of the Data Service extension
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UpgradeDataServiceExtensionStep.java,v 1.1 2008-05-16 19:25:25 dervin Exp $ 
 */
public class UpgradeDataServiceExtensionStep extends Step {
	private String serviceDir;
	
	public UpgradeDataServiceExtensionStep(String serviceDir) {
		this.serviceDir = serviceDir;
	}
	

	public void runStep() throws Throwable {
		// get the service description
	    ServiceInformation info = new ServiceInformation(new File(serviceDir));

		ExtensionsUpgradeManager extUpgrader = new ExtensionsUpgradeManager(
			info, new File(serviceDir).getAbsolutePath());
		// verify that the service is in need of an upgrade
		assertTrue("Service should have required upgrading", extUpgrader.needsUpgrading());
		// upgrade the service
		IntroduceUpgradeStatus status = new IntroduceUpgradeStatus();
		extUpgrader.upgrade(status);
	}
}
