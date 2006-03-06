package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataListType;

import java.io.File;

import javax.xml.namespace.QName;

import com.atomicobject.haste.framework.Step;


public class RemoveAllMetadataStep extends Step {
	private TestCaseInfo tci;


	public RemoveAllMetadataStep(TestCaseInfo tci) {
		this.tci = tci;
	}


	public void runStep() throws Throwable {
		System.out.println("Adding metadata.");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if (pathtobasedir == null) {
			System.err.println("basedir system property not set");
			throw new Exception("basedir system property not set");
		}

		ServiceDescription introService = (ServiceDescription) Utils.deserializeDocument(pathtobasedir + File.separator
			+ tci.getDir() + File.separator + "introduce.xml", ServiceDescription.class);
		MetadataListType metadatasType = introService.getMetadataList();
		metadatasType.setMetadata(null);

		Utils.serializeDocument(pathtobasedir + File.separator + tci.getDir() + File.separator + "introduce.xml",
			introService, new QName("gme://gov.nih.nci.cagrid/1/Introduce", "ServiceSkeleton"));

		String cmd = CommonTools.getAntSkeletonResyncCommand(pathtobasedir + File.separator + tci.getDir());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();

		assertEquals("Checking resync status", 0, p.exitValue());

		cmd = CommonTools.getAntAllCommand(pathtobasedir + File.separator + tci.getDir());

		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals("Checking build status", 0, p.exitValue());
	}

}
