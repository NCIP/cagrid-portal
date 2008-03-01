package gov.nih.nci.cagrid.introduce.upgrade.introduce;

import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.StatusBase;
import gov.nih.nci.cagrid.introduce.upgrade.one.x.IntroduceUpgraderBase;


public class Introduce_1_2__1_3_Upgrader extends IntroduceUpgraderBase {

    public Introduce_1_2__1_3_Upgrader(IntroduceUpgradeStatus status, ServiceInformation serviceInformation,
        String servicePath) throws Exception {
        super(status, serviceInformation, servicePath, "1.2", "1.3");
    }


    protected void upgrade() throws Exception {

   
        getStatus().setStatus(StatusBase.UPGRADE_OK);
    }



}
