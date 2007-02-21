package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import gov.nih.nci.cagrid.common.ZipUtilities;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  UnzipOldServiceStep
 *  Step to unzip the old service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UnzipOldServiceStep.java,v 1.2 2007-02-21 16:48:56 dervin Exp $ 
 */
public class UnzipOldServiceStep extends Step {
	private String testDir;
	
	public UnzipOldServiceStep(String testDir) {
		super();
		this.testDir = testDir;
	}
	

	public void runStep() throws Throwable {
		File zippedService = new File(testDir + File.separator + "resources" + File.separator + "CaGridTutorialService1.0.zip");
		ZipUtilities.unzip(zippedService, new File(testDir));
	}
}
