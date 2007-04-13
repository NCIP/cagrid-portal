package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.upgrade.UpgradeManager;
import gov.nih.nci.cagrid.introduce.upgrade.common.UpgradeStatus;

import java.io.File;


public class UpgradesStep extends BaseStep {
    private TestCaseInfo tci;


    public UpgradesStep(TestCaseInfo tci, boolean build) throws Exception {
        super(tci.getDir(), build);
        this.tci = tci;
    }


    public void runStep() throws Throwable {
        System.out.println("Upgrading Service");


        UpgradeManager upgrader = new UpgradeManager(this.tci.getDir());

        try {
            UpgradeStatus status = upgrader.upgrade();
            System.out.println(status);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        ServiceInformation info = new ServiceInformation(new File(getBaseDir() + File.separator + this.tci.getDir()
            + File.separator));

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
