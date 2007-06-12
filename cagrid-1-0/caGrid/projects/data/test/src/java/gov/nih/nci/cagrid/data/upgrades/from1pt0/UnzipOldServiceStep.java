package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import gov.nih.nci.cagrid.common.ZipUtilities;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  UnzipOldServiceStep
 *  Step to unzip an old service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UnzipOldServiceStep.java,v 1.4 2007-06-12 16:13:40 dervin Exp $ 
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
