package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;

public abstract class ExtensionUpgraderBase extends UpgraderBase {

	public ExtensionUpgraderBase(ServiceDescription serviceDescription,
			String fromVersion, String toVersion) {
		super(serviceDescription, fromVersion, toVersion);
	}

}
