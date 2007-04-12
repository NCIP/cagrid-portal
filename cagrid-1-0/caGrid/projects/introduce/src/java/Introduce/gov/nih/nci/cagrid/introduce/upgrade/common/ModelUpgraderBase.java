package gov.nih.nci.cagrid.introduce.upgrade.common;



public abstract class ModelUpgraderBase implements ModelUpgraderI {

	String fromVersion;
	String toVersion;
	String servicePath;
	IntroduceUpgradeStatus status;

	public ModelUpgraderBase(IntroduceUpgradeStatus status, String servicePath,
			String fromVersion, String toVersion) {
	    this.status = status;
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
	
	public IntroduceUpgradeStatus getStatus(){
	    return this.status;
	}

	public String getToVersion() {
		return toVersion;
	}

	public void setToVersion(String toVersion) {
		this.toVersion = toVersion;
	}

	protected abstract void upgrade() throws Exception;

	public void execute() throws Exception {
	    System.out.println("Upgrading introduce model"
            + " from Version " + this.getFromVersion()
            + " to Version " + this.getToVersion());
		this.upgrade();
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}
}
