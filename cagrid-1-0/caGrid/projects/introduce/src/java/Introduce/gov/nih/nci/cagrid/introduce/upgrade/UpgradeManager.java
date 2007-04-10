package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.io.File;

import org.apache.log4j.Logger;


public class UpgradeManager {
	private ServiceInformation serviceInfo;
	private IntroduceUpgradeManager iUpgrader;
	private ExtensionsUpgradeManager eUpgrader;
	private String pathToService;
	private String id;
	private static final Logger logger = Logger.getLogger(UpgradeManager.class);


	public UpgradeManager(ServiceInformation serviceInformation, String pathToService) {
		this.serviceInfo = serviceInformation;
		this.pathToService = pathToService;
		iUpgrader = new IntroduceUpgradeManager(serviceInfo, pathToService);
		eUpgrader = new ExtensionsUpgradeManager(serviceInfo, pathToService);
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
	    logger.info("Creating backup of service prior to upgrading.");
		id = String.valueOf(System.currentTimeMillis());
		ResourceManager.createArchive(id, serviceInfo.getServices().getService(0).getName() + "UPGRADE", pathToService);
		
	}


	private void recover() throws Exception {
	    logger.info("Recovering backup of service after failed upgrade.");
		ResourceManager.restoreSpecific(id, serviceInfo.getServices().getService(0).getName() + "UPGRADE", pathToService);
	}


	public void upgrade() throws Exception {
		try {
			backup();

			if (canIntroduceBeUpgraded()) {
				upgradeIntroduce();
			}
			if (canExtensionsBeUpgraded()) {
				upgradeExtensions();
			}
			
			serviceInfo.persistInformation();

			SyncTools sync = new SyncTools(new File(pathToService));
			sync.sync();

		} catch (Exception e) {
		    try {
			recover();
		    } catch (Exception e1){
		        logger.debug("Failed attempting to recover the service from backup.");
		        e.printStackTrace();
		    }
			throw e;
		}
	}


	private void upgradeIntroduce() throws Exception {
		if (iUpgrader.needsUpgrading()) {
			try {
				iUpgrader.upgrade();
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
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Extensions Upgrader Failed.  This service does not appear to be upgradable.");
			}
		}
	}

}
