package gov.nih.nci.cagrid.introduce.upgrade.one.one;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.upgrade.common.ExtensionUpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.ExtensionUpgraderI;

/**
 * Class must be extended to provide an extension upgrader. An extension
 * upgrader should be used to provide upgrades to the service to support a newer
 * version of an extension. The extension upgrader should only touch parts of
 * the service that the extension itself controls, i.e. the extension data in
 * the xml entity in the introduce.xml document in the service's top level
 * directory. The version attribute in the extensionType will automatically be
 * updated.
 * 
 * @author hastings
 * 
 */
public abstract class ExtensionUpgraderBase implements ExtensionUpgraderI {

	ExtensionType extensionType;
	   ServiceInformation serviceInformation;
	    String fromVersion;
	    String toVersion;
	    String servicePath;

	public ExtensionUpgraderBase(ExtensionType extensionType,
			ServiceInformation serviceInformation, String servicePath,
			String fromVersion, String toVersion) {
	    this.serviceInformation = serviceInformation;
        this.fromVersion = fromVersion;
        this.toVersion = toVersion;
        this.servicePath = servicePath;
		this.extensionType = extensionType;
	}

	public ExtensionUpgradeStatus execute() throws Exception {
		System.out.println("Upgrading services " + extensionType.getName()
				+ " extension  from Version " + this.getFromVersion()
				+ " to Version " + this.getToVersion());
		ExtensionUpgradeStatus status = upgrade();
		extensionType.setVersion(getToVersion());
		return status;
	}

	public ExtensionType getExtensionType() {
		return extensionType;
	}

	public void setExtensionType(ExtensionType extensionType) {
		this.extensionType = extensionType;
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

	    protected abstract ExtensionUpgradeStatus upgrade() throws Exception;

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
