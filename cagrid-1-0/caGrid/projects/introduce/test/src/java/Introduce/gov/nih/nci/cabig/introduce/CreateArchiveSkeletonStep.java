package gov.nih.nci.cabig.introduce;

import java.io.File;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.Archive;

import com.atomicobject.haste.framework.Step;

public class CreateArchiveSkeletonStep extends Step {
	
	public void runStep() throws Throwable {
		System.out.println("Creating the service skeleton");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if(pathtobasedir == null){
			System.out.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}
		
		//Archive.createArchive(TestCaseInfo.name,pathtobasedir + File.separator + TestCaseInfo.dir);
	}

}
