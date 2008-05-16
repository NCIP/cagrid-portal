package org.cagrid.data.test.upgrades.from1pt0;

import gov.nih.nci.cagrid.common.ZipUtilities;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;

/** 
 *  UnzipOldServiceStep
 *  Step to unzip an old service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UnzipOldServiceStep.java,v 1.1 2008-05-16 19:25:25 dervin Exp $ 
 */
public class UnzipOldServiceStep extends Step {
	private String testDir;
    private String serviceZipName;
	
	public UnzipOldServiceStep(String testDir, String serviceZipName) {
		super();
		this.testDir = testDir;
        this.serviceZipName = serviceZipName;
	}
	

	public void runStep() throws Throwable {
		File zippedService = new File(testDir + File.separator + "resources" + File.separator + serviceZipName);
		ZipUtilities.unzip(zippedService, new File(testDir));
	}
}
