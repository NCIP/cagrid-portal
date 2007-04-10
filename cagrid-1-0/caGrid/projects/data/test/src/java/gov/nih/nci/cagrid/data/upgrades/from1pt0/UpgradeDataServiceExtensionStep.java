package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.upgrade.ExtensionsUpgradeManager;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  UpgradeServiceStep
 *  Upgrades the 1.0 service to the 1.1 version of Introduce
 *  and the 1.1 version of the Data Service extension
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UpgradeDataServiceExtensionStep.java,v 1.3 2007-04-10 14:30:28 hastings Exp $ 
 */
public class UpgradeDataServiceExtensionStep extends Step {
	private String serviceDir;
	
	public UpgradeDataServiceExtensionStep(String serviceDir) {
		this.serviceDir = serviceDir;
	}
	

	public void runStep() throws Throwable {
		// get the service description
	    ServiceInformation info = new ServiceInformation(new File(serviceDir + File.separator + "introduce.xml"));

		ExtensionsUpgradeManager extUpgrader = new ExtensionsUpgradeManager(
			info, new File(serviceDir).getAbsolutePath());
		// verify that the service is in need of an upgrade
		assertTrue("Service should have required upgrading", extUpgrader.needsUpgrading());
		// upgrade the service
		extUpgrader.upgrade();
	}
}
