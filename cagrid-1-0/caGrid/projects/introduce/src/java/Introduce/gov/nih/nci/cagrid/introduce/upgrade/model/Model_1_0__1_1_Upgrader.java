package gov.nih.nci.cagrid.introduce.upgrade.model;

import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.ModelUpgraderBase;


public class Model_1_0__1_1_Upgrader extends ModelUpgraderBase {

    public Model_1_0__1_1_Upgrader(IntroduceUpgradeStatus status, String servicePath) {
        super(status, servicePath, "1.0", "1.1");
    }


    protected void upgrade() throws Exception {
        getStatus().addDescriptionLine("Nothing to update in the introduce model");
    }
}
