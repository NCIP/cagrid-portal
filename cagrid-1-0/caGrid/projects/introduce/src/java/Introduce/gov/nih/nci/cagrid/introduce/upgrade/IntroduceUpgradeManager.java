package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.ModelUpgraderI;
import gov.nih.nci.cagrid.introduce.upgrade.common.UpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.UpgradeUtilities;
import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgraderI;

import java.io.File;
import java.lang.reflect.Constructor;


public class IntroduceUpgradeManager {
    private ExtensionsUpgradeManager eUpgrader;
    private String pathToService;


    public IntroduceUpgradeManager(String pathToService) {
        this.pathToService = pathToService;
    }


    private static String getUpgradeVersion(String oldVersion) {
        if (oldVersion.equals("1.0")) {
            return "1.1";
        }

        return null;
    }


    private static String getIntroduceUpgradeClass(String version) {
        if (version.equals("1.1")) {
            return "gov.nih.nci.cagrid.introduce.upgrade.introduce.Introduce_1_0__1_1_Upgrader";
        }
        return null;
    }
    
    private static String getModelUpgradeClass(String version) {
        if (version.equals("1.1")) {
            return "gov.nih.nci.cagrid.introduce.upgrade.model.Model_1_0__1_1_Upgrader";
        }
        return null;
    }


    protected boolean needsUpgrading() {
        try {
            String serviceVersion = UpgradeUtilities.getCurrentServiceVersion(
                pathToService + File.separator + IntroduceConstants.INTRODUCE_XML_FILE);
            if ((serviceVersion == null) || !serviceVersion.equals(CommonTools.getIntroduceVersion())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    protected boolean canBeUpgraded(String version) {
        if ((getUpgradeVersion(version) != null) && (getIntroduceUpgradeClass(getUpgradeVersion(version)) != null)) {
            return true;
        } else {
            return false;
        }
    }


    protected void upgrade(UpgradeStatus status) throws Exception {
        System.out.println("Trying to upgrade the service");

        String serviceVersion = UpgradeUtilities.getCurrentServiceVersion(pathToService + File.separator + IntroduceConstants.INTRODUCE_XML_FILE);

        if (canBeUpgraded(serviceVersion)) {

            // upgrade the introduce service
            String version = CommonTools.getIntroduceVersion();
            if (version != null) {

                String vers = UpgradeUtilities.getCurrentServiceVersion(pathToService + File.separator + IntroduceConstants.INTRODUCE_XML_FILE);

                while (canBeUpgraded(vers)) {
                    String newVersion = getUpgradeVersion(vers);
                    if (newVersion == null) {
                        System.out.println("The service"
                            + " is upgradeable however no upgrade version from the version " + vers
                            + " could be found.");
                        break;
                    }

                    String className = getModelUpgradeClass(newVersion);
                    if (className == null) {
                        System.out.println("The model" + " is upgradeable however no upgrade class from the version "
                            + vers + " could be found.");
                        break;
                    }
                    
                    IntroduceUpgradeStatus iStatus = new IntroduceUpgradeStatus();
                    status.addIntroduceUpgradeStatus(iStatus);

                    // upgrade the introduce service
                    Class clazz = Class.forName(className);
                    Constructor con = clazz.getConstructor(new Class[]{IntroduceUpgradeStatus.class, String.class});
                    ModelUpgraderI modelupgrader = (ModelUpgraderI) con.newInstance(new Object[]{iStatus,pathToService});
                    modelupgrader.execute();

                    ServiceInformation serviceInfo = new ServiceInformation(new File(pathToService));

                    className = getIntroduceUpgradeClass(newVersion);
                    if (className == null) {
                        System.out.println("The service" + " is upgradeable however no upgrade class from the version "
                            + vers + " could be found.");
                        break;
                    }
                    
                    // upgrade the introduce service
                    clazz = Class.forName(className);
                    con = clazz.getConstructor(new Class[]{IntroduceUpgradeStatus.class, ServiceInformation.class, String.class});
                    IntroduceUpgraderI upgrader = (IntroduceUpgraderI) con.newInstance(new Object[]{iStatus,serviceInfo, pathToService});
                    upgrader.execute();
                    
                    vers = newVersion;

                    eUpgrader = new ExtensionsUpgradeManager(serviceInfo, pathToService);

                    if (eUpgrader.needsUpgrading()) {
                        try {
                            eUpgrader.upgrade(iStatus);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new Exception(
                                "Extensions Upgrader Failed.  This service does not appear to be upgradable.");
                        }
                    }
                    
                    serviceInfo.persistInformation();
                    
                }

            } else {
                throw new Exception("ERROR: The service"
                    + " is not upgradable because it's version cannot be determined or is corupt");
            }

        } else {
            throw new Exception("ERROR: The service" + " needs to be upgraded but no upgrader can be found");
        }
    }
}
