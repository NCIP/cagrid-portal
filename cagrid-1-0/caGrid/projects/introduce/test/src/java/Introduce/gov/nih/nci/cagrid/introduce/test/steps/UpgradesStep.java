package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
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
        ServiceInformation info = new ServiceInformation(new File(getBaseDir() + File.separator + this.tci.getDir()
            + File.separator));

        UpgradeManager upgrader = new UpgradeManager(info, this.tci.getDir());

        try {
            upgrader.upgrade();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            SyncTools sync = new SyncTools(new File(getBaseDir() + File.separator + this.tci.getDir()));
            sync.sync();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        buildStep();
    }
}
