package gov.nih.nci.cagrid.introduce.upgrade;

import java.io.File;

import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.UpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.UpgradeUtilities;

import org.apache.log4j.Logger;


public class UpgradeManager {

    private IntroduceUpgradeManager iUpgrader;
    private ExtensionsUpgradeManager eUpgradeManager;

    private String pathToService;
    private String id;
    private static final Logger logger = Logger.getLogger(UpgradeManager.class);


    public UpgradeManager(String pathToService) {
        this.pathToService = pathToService;
        iUpgrader = new IntroduceUpgradeManager(pathToService);
    }


    public boolean canIntroduceBeUpgraded() {
        try {
            if (iUpgrader.needsUpgrading()
                && iUpgrader.canBeUpgraded(UpgradeUtilities.getCurrentServiceVersion(pathToService + File.separator
                    + "introduce.xml"))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public boolean extensionsNeedUpgraded() {
        if (!canIntroduceBeUpgraded()) {
            ServiceInformation info = null;
            try {
                info = new ServiceInformation(new File(pathToService));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ExtensionsUpgradeManager eUpgradeManager = new ExtensionsUpgradeManager(info, pathToService);
            if (eUpgradeManager.needsUpgrading()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    private void backup() throws Exception {
        logger.info("Creating backup of service prior to upgrading.");
        id = String.valueOf(System.currentTimeMillis());
        ResourceManager.createArchive(id, UpgradeUtilities.getServiceName(pathToService + File.separator
            + "introduce.xml")
            + "UPGRADE", pathToService);

    }


    public void recover() throws Exception {
        logger.info("Recovering backup of service after failed upgrade.");
        ResourceManager.restoreSpecific(id, UpgradeUtilities.getServiceName(pathToService + File.separator
            + "introduce.xml")
            + "UPGRADE", pathToService);
    }


    public UpgradeStatus upgrade() throws Exception {
        UpgradeStatus status = new UpgradeStatus();
        backup();

        if (canIntroduceBeUpgraded()) {
           upgradeIntroduce(status);
           return status;
        } else if (extensionsNeedUpgraded()) {
            status.addIntroduceUpgradeStatus(upgradeExtensionsOnly());
            return status;
        } else {
            return status;
        }
    }


    private void upgradeIntroduce(UpgradeStatus status) throws Exception {
        if (iUpgrader.needsUpgrading()) {
            try {
                iUpgrader.upgrade(status);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(
                    "Service upgrader failed.  This service does not appear to be upgradable possibly due to modification of Introduce managed files.");
            }

        }
    }


    private IntroduceUpgradeStatus upgradeExtensionsOnly() throws Exception {
        if (!iUpgrader.needsUpgrading()) {
            try {
                ServiceInformation info = null;
                try {
                    info = new ServiceInformation(new File(pathToService));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                IntroduceUpgradeStatus status = new IntroduceUpgradeStatus();
                ExtensionsUpgradeManager eUpgradeManager = new ExtensionsUpgradeManager(info, pathToService);
                eUpgradeManager.upgrade(status);
                return status;
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(
                    "Extensions upgrader failed.  Certain Extensions upgraders must have failed. Please see upgrade status log.");
            }
        }
        return null;
    }
}
