package gov.nih.nci.cagrid.introduce.test.steps;

import java.io.File;

import gov.nih.nci.cagrid.common.ZipUtilities;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;

import com.atomicobject.haste.framework.Step;

public class UnzipOldServiceStep extends Step {

	private TestCaseInfo tci;
	private String serviceZipFile;

	public UnzipOldServiceStep(String serviceZipFile, TestCaseInfo tci)
			throws Exception {
		super();
		this.tci = tci;
		this.serviceZipFile = serviceZipFile;
	}

	public void runStep() throws Throwable {
		System.out.println("Unzipping old service");
		File zipDir = new File(tci.getDir());
		zipDir.mkdir();
		ZipUtilities.unzip(new File(serviceZipFile), zipDir);
	}

}
