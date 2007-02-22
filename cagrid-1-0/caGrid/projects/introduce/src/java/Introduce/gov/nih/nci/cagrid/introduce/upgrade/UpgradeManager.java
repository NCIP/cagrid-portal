package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;

import java.io.File;

public class UpgradeManager {
	private ServiceDescription service;
	private IntroduceUpgradeManager iUpgrader;
	private ExtensionsUpgradeManager eUpgrader;
	private String pathToService;
	private String id;

	public UpgradeManager(ServiceDescription service, String pathToService) {
		this.service = service;
		this.pathToService = pathToService;
		iUpgrader = new IntroduceUpgradeManager(service, pathToService);
		eUpgrader = new ExtensionsUpgradeManager(service, pathToService);
	}

	public boolean canIntroduceBeUpgraded() {
		if (iUpgrader.needsUpgrading()) {
			return true;
		} else {
			return false;
		}

	}

	public boolean canExtensionsBeUpgraded() {
		if (eUpgrader.needsUpgrading()) {
			return true;
		} else {
			return false;
		}

	}

	private void backup() throws Exception {
		id = "UPGRADE-" + System.currentTimeMillis();
		ResourceManager.createArchive(id, service.getServices().getService(0)
				.getName(), pathToService);
	}

	private void recover() throws Exception {
		ResourceManager.restoreSpecific(id, service.getServices().getService(0)
				.getName(), pathToService);
	}

	public void upgrade() throws Exception {
		try {
			backup();

			if (canIntroduceBeUpgraded()) {
				upgradeIntroduce();
				service = (ServiceDescription) Utils.deserializeDocument(
						pathToService + File.separator + "introduce.xml",
						ServiceDescription.class);
			}
			if (canExtensionsBeUpgraded()) {
				upgradeExtensions();
			}
		} catch (Exception e) {
			recover();
			throw e;
		}
	}

	private void upgradeIntroduce() throws Exception {
		if (iUpgrader.needsUpgrading()) {
			try {
				iUpgrader.upgrade();
				// reload the service description after the upgrade
				service = (ServiceDescription) Utils.deserializeDocument(
						pathToService + File.separator + "introduce.xml",
						ServiceDescription.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(
						"Service upgrader failed.  This service does not appear to be upgradable possibly due to modification of Introduce managed files.");
			}
		}
	}

	private void upgradeExtensions() throws Exception {
		if (eUpgrader.needsUpgrading()) {
			try {
				eUpgrader.upgrade();
				service = (ServiceDescription) Utils.deserializeDocument(
						pathToService + File.separator + "introduce.xml",
						ServiceDescription.class);

			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(
						"Extensions Upgrader Failed.  This service does not appear to be upgradable.");
			}
		}
	}

}
