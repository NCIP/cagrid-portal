package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;

public abstract class UpgraderBase implements UpgraderI {
	
	ServiceDescription serviceDescription;
	String fromVersion;
	String toVersion;
	
	UpgraderBase(ServiceDescription serviceDescription, String fromVersion, String toVersion){
		this.serviceDescription = serviceDescription;
		this.fromVersion = fromVersion;
		this.toVersion = toVersion;
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
	
	
	public void execute() throws Exception{
		System.out.println("Upgrading Introduce Service From Version " + this.getFromVersion() + " to Version " + this.getToVersion());
		this.upgrade();
	}
	
	public ServiceDescription getServiceDescription() {
		return serviceDescription;
	}
	public void setServiceDescription(ServiceDescription serviceDescription) {
		this.serviceDescription = serviceDescription;
	}
}
