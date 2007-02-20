package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;

public abstract class IntroduceUpgraderBase extends UpgraderBase {

	public IntroduceUpgraderBase(ServiceDescription serviceDescription,
			String servicePath, String fromVersion, String toVersion) {
		super(serviceDescription, servicePath, fromVersion, toVersion);
	}

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		super.execute();
		getServiceDescription().setIntroduceVersion(getToVersion());
	}

}
