package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;

public class ExtensionsUpgradeManager {
	private ServiceDescription service;

	public ExtensionsUpgradeManager(ServiceDescription service,
			String pathToService) {
		this.service = service;
	}

	public void upgrade() throws Exception {
		System.out.println("Trying to upgrade the service");

		ExtensionType[] extensions = service.getExtensions().getExtension();
		for (int extensionI = 0; extensionI < extensions.length; extensionI++) {
			ExtensionType extension = extensions[extensionI];
			String serviceExtensionVersion = extension.getVersion();
			ExtensionDescription extDescription = ExtensionsLoader
					.getInstance().getExtension(extension.getName());
			if (extDescription != null) {
				if (!extDescription.getVersion()
						.equals(serviceExtensionVersion)) {
					// service needs to be upgraded
				}

			} else {
				// service does not have the right extension to run with
				// this service
			}
		}

	}

}
