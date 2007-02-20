package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;

/**
 * Class must be extended to provide an extension upgrader. An extension
 * upgrader should be used to provide upgrades to the service to support a newer
 * version of an extension. The extension upgrader should only touch parts of the
 * service that the extension itself controls, i.e. the extension data in the
 * xml entity in the introduce.xml document in the service's top level directory.
 * The version attribute in the extensionType will automatically be updated.
 * 
 * @author hastings
 * 
 */
public abstract class ExtensionUpgraderBase extends UpgraderBase {

	ExtensionType extensionType;

	public ExtensionUpgraderBase(ExtensionType extensionType,
			ServiceDescription serviceDescription, String servicePath,
			String fromVersion, String toVersion) {
		super(serviceDescription, servicePath, fromVersion, toVersion);
		this.extensionType = extensionType;
	}

	
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		super.execute();
		getServiceDescription().setIntroduceVersion(getToVersion());
	}

	
	public ExtensionType getExtensionType() {
		return extensionType;
	}

	
	public void setExtensionType(ExtensionType extensionType) {
		this.extensionType = extensionType;
	}
}
