package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;
import java.lang.reflect.Constructor;

public class IntroduceUpgradeManager {
	private ServiceDescription service;
	private String pathToService;

	public IntroduceUpgradeManager(ServiceDescription service,
			String pathToService) {
		this.service = service;
		this.pathToService = pathToService;
	}

	private static String getUpgradeVersion(String oldVersion) {
		if (oldVersion.equals("1.0")) {
			return "1.1";
		}

		return null;
	}

	private static String getUpgradeClass(String version) {
		if (version.equals("1.1")) {
			return "gov.nih.nci.cagrid.introduce.upgrade.introduce.Introduce_1_0__1_1_Upgrader";
		}
		return null;
	}

	public boolean needsUpgrading() {
		if ((service.getIntroduceVersion() == null)
				|| !service.getIntroduceVersion().equals(
						CommonTools.getIntroduceVersion())) {
			return true;
		}
		return false;
	}

	public boolean canBeUpgraded(String version) {
		if ((getUpgradeVersion(version) != null)
				&& (getUpgradeClass(getUpgradeVersion(version)) != null)) {
			return true;
		} else {
			return false;
		}
	}

	public void upgrade() throws Exception {
		System.out.println("Trying to upgrade the service");

		if (canBeUpgraded(service.getIntroduceVersion())) {

			// upgrade the introduce service
			String version = CommonTools.getIntroduceVersion();
			if (version != null) {

				String vers = service.getIntroduceVersion();

				while (canBeUpgraded(vers)) {
					String newVersion = getUpgradeVersion(vers);
					if (newVersion == null) {
						System.out
								.println("The service"
										+ " is upgradeable however no upgrade version from the version "
										+ vers + " could be found.");
						break;
					}

					String className = getUpgradeClass(newVersion);
					if (className == null) {
						System.out
								.println("The service"
										+ " is upgradeable however no upgrade class from the version "
										+ vers + " could be found.");
						break;
					}

					// upgrade the introduce service
					Class clazz = Class.forName(className);
					Constructor con = clazz.getConstructor(new Class[] {
							ServiceDescription.class, String.class });
					UpgraderI upgrader = (UpgraderI) con
							.newInstance(new Object[] { service, pathToService });
					upgrader.execute();

					vers = newVersion;
				}

				Utils.serializeDocument(pathToService + File.separator
						+ "introduce.xml", service,
						IntroduceConstants.INTRODUCE_SKELETON_QNAME);

			} else {
				throw new Exception(
						"ERROR: The service"
								+ " is not upgradable because it's version cannot be determined or is corupt");
			}

		} else {
			throw new Exception("ERROR: The service"
					+ " needs to be upgraded but no upgrader can be found");
		}
	}
}
