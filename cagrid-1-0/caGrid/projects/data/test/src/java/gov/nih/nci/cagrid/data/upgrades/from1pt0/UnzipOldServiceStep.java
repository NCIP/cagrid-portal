package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import gov.nih.nci.cagrid.common.ZipUtilities;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;

/** 
 *  UnzipOldServiceStep
 *  Step to unzip an old service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UnzipOldServiceStep.java,v 1.5 2007-12-03 16:27:19 hastings Exp $ 
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
