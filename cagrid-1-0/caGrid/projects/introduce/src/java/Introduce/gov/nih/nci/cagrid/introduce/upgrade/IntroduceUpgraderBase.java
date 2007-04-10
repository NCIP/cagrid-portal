package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

public abstract class IntroduceUpgraderBase extends UpgraderBase {

	public IntroduceUpgraderBase(ServiceInformation serviceInformation,
			String servicePath, String fromVersion, String toVersion) {
		super(serviceInformation, servicePath, fromVersion, toVersion);
	}

	public void execute() throws Exception {
		System.out.println("Upgrading Introduce Service From Version "
				+ this.getFromVersion() + " to Version " + this.getToVersion());
		super.execute();
		getServiceInformation().getServiceDescriptor().setIntroduceVersion(getToVersion());
	}

}
