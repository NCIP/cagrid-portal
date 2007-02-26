package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.upgrade.UpgradeManager;

import java.io.File;

public class UpgradesStep extends BaseStep {
	private TestCaseInfo tci;

	public UpgradesStep(TestCaseInfo tci, boolean build) throws Exception {
		super(tci.getDir(), build);
		this.tci = tci;
	}

	public void runStep() throws Throwable {
		System.out.println("Upgrading Service");

		ServiceDescription introService = (ServiceDescription) Utils
				.deserializeDocument(getBaseDir() + File.separator
						+ tci.getDir() + File.separator + "introduce.xml",
						ServiceDescription.class);

		UpgradeManager upgrader = new UpgradeManager(introService, tci.getDir());

		try {
			upgrader.upgrade();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		Utils.serializeDocument(getBaseDir() + File.separator + tci.getDir()
				+ File.separator + "introduce.xml", introService,
				IntroduceConstants.INTRODUCE_SKELETON_QNAME);

		try {
			SyncTools sync = new SyncTools(new File(getBaseDir()
					+ File.separator + tci.getDir()));
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		buildStep();
	}

}
