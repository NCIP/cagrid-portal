package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import java.io.File;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.upgrade.ExtensionsUpgradeManager;

import com.atomicobject.haste.framework.Step;

/** 
 *  UpgradeServiceStep
 *  Upgrades the 1.0 service to the 1.1 version of Introduce
 *  and the 1.1 version of the Data Service extension
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UpgradeDataServiceExtensionStep.java,v 1.2 2007-04-09 15:26:14 dervin Exp $ 
 */
public class UpgradeDataServiceExtensionStep extends Step {
	private String serviceDir;
	
	public UpgradeDataServiceExtensionStep(String serviceDir) {
		this.serviceDir = serviceDir;
	}
	

	public void runStep() throws Throwable {
		// get the service description
		ServiceDescription serviceDesc = (ServiceDescription) Utils.deserializeDocument(
			serviceDir + File.separator + "introduce.xml", ServiceDescription.class);
		ExtensionsUpgradeManager extUpgrader = new ExtensionsUpgradeManager(
			serviceDesc, new File(serviceDir).getAbsolutePath());
		// verify that the service is in need of an upgrade
		assertTrue("Service should have required upgrading", extUpgrader.needsUpgrading());
		// upgrade the service
		extUpgrader.upgrade();
	}
}
