package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;

public abstract class IntroduceUpgraderBase extends UpgraderBase {

	public IntroduceUpgraderBase(ServiceDescription serviceDescription,
			String fromVersion, String toVersion) {
		super(serviceDescription, fromVersion, toVersion);
	}

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		super.execute();
		getServiceDescription().setIntroduceVersion(getToVersion());
	}

}
