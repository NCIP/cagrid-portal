package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

public abstract class UpgraderBase implements UpgraderI {

	ServiceInformation serviceInformation;
	String fromVersion;
	String toVersion;
	String servicePath;

	UpgraderBase(ServiceInformation serviceInformation, String servicePath,
			String fromVersion, String toVersion) {
		this.serviceInformation = serviceInformation;
		this.fromVersion = fromVersion;
		this.toVersion = toVersion;
		this.servicePath = servicePath;
	}

	public String getFromVersion() {
		return fromVersion;
	}

	public void setFromVersion(String fromVersion) {
		this.fromVersion = fromVersion;
	}

	public String getToVersion() {
		return toVersion;
	}

	public void setToVersion(String toVersion) {
		this.toVersion = toVersion;
	}

	protected abstract void upgrade() throws Exception;

	public void execute() throws Exception {
		this.upgrade();
	}

	public ServiceInformation getServiceInformation() {
		return serviceInformation;
	}

	public void setServiceInformation(ServiceInformation serviceInformation) {
		this.serviceInformation = serviceInformation;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}
}
