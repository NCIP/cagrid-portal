package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.UpgradeDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ExtensionsUpgradeManager {
	private ServiceDescription service;
	private String pathToService;

	public ExtensionsUpgradeManager(ServiceDescription service,
			String pathToService) {
		this.service = service;
		this.pathToService = pathToService;
	}

	public boolean needsUpgrading() {
		ExtensionType[] extensions = service.getExtensions().getExtension();
		for (int extensionI = 0; extensionI < extensions.length; extensionI++) {
			ExtensionType extension = extensions[extensionI];
			String serviceExtensionVersion = extension.getVersion();
			ExtensionDescription extDescription = ExtensionsLoader
					.getInstance().getExtension(extension.getName());
			if ((extDescription != null)
					&& (extDescription.getVersion() != null)) {
				if ((serviceExtensionVersion == null)
						|| !extDescription.getVersion().equals(
								serviceExtensionVersion)) {
					return true;
				}

			}
		}
		return false;
	}

	public void upgrade() throws Exception {
		System.out.println("Trying to upgrade the service");
		List error = new ArrayList();

		ExtensionType[] extensions = service.getExtensions().getExtension();
		for (int extensionI = 0; extensionI < extensions.length; extensionI++) {
			ExtensionType extension = extensions[extensionI];
			String serviceExtensionVersion = extension.getVersion();
			ExtensionDescription extDescription = ExtensionsLoader
					.getInstance().getExtension(extension.getName());
			if ((extDescription != null)
					&& (extDescription.getVersion() != null)) {
				List upgrades = new ArrayList();
				if (((serviceExtensionVersion == null) && (extDescription
						.getVersion() != null))
						|| !extDescription.getVersion().equals(
								serviceExtensionVersion)) {
					// service needs to be upgraded
					// put together a list of upgrades to run
					UpgradeDescriptionType[] extensionUpgrades = null;
					if ((extDescription.getUpgradesDescription() != null)
							&& (extDescription.getUpgradesDescription()
									.getUpgradeDescription() != null)) {
						extensionUpgrades = extDescription
								.getUpgradesDescription()
								.getUpgradeDescription();

						String currentVersion = serviceExtensionVersion;
						while (((currentVersion == null) || !currentVersion
								.equals(extDescription.getVersion()))) {
							boolean found = false;
							int i = 0;
							for (i = 0; i < extensionUpgrades.length; i++) {
								if ((extensionUpgrades[i].getFromVersion() == null)
										&& (currentVersion == null)) {
									found = true;
									break;
								} else if (extensionUpgrades[i]
										.getFromVersion()
										.equals(currentVersion)) {
									found = true;
									break;
								}

							}
							if (found) {
								upgrades.add(extensionUpgrades[i]);
								currentVersion = extensionUpgrades[i]
										.getToVersion();
							} else {
								error
										.add(extension.getName()
												+ " does not appear to have correct upgrade.");
							}

						}

					} else {
						error.add(extension.getName()
								+ " does not appear to have any upgrades.");
					}

				}

				// run the upgraders that we put together in order
				for (int i = 0; i < upgrades.size(); i++) {
					UpgradeDescriptionType upgrade = (UpgradeDescriptionType) upgrades
							.get(i);
					Class clazz = Class.forName(upgrade.getUpgradeClass());
					Constructor con = clazz.getConstructor(new Class[] {
							ExtensionType.class, ServiceDescription.class,
							String.class, String.class, String.class });
					UpgraderI upgrader = (UpgraderI) con
							.newInstance(new Object[] { extension, service,
									pathToService, upgrade.getFromVersion(),
									upgrade.getToVersion() });
					upgrader.execute();
				}

				Utils.serializeDocument(pathToService + File.separator
						+ "introduce.xml", service,
						IntroduceConstants.INTRODUCE_SKELETON_QNAME);

			}
		}

		if (error.size() > 0) {
			String errorString = "";
			for (int errorI = 0; errorI < error.size(); errorI++) {
				errorString += (String) error.get(errorI) + "\n";
			}
			throw new Exception(errorString);
		}

	}
}
