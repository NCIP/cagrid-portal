package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataListType;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;

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

		try {
			SyncTools sync = new SyncTools(new File(pathtobasedir + File.separator + tci.getDir()));
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		String cmd = CommonTools.getAntAllCommand(pathtobasedir + File.separator + tci.getDir());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals("Checking build status", 0, p.exitValue());
	}

}
