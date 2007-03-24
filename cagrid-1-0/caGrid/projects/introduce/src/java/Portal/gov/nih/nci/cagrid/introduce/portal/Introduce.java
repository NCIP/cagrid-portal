package gov.nih.nci.cagrid.introduce.portal;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.SplashScreen;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JFrame;

import org.projectmobius.common.MobiusException;
import org.projectmobius.portal.GridPortal;
import org.projectmobius.portal.PortalResourceManager;


public final class Introduce {

    private static SplashScreen introduceSplash;


    public static void main(String[] args) {
        showIntroduceSplash();
        if (args.length > 0) {
            showGridPortal(args[0]);
        } else {
            showGridPortal(null);
        }
        EventQueue.invokeLater(new IntroduceSplashCloser());
    }


    private static void showIntroduceSplash() {
        try {
            introduceSplash = new SplashScreen("/introduceSplash.png");
        } catch (Exception e) {

        }
    }


    private static void initialize() {
        try {
            ResourceManager.setConfigurationProperty(IntroduceConstants.NAMESPACE_TYPE_REPLACEMENT_POLICY_PROPERTY,
                NamespaceTypeDiscoveryComponent.ERROR);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        checkGlobusLocation();
    }


    private static void showGridPortal(String confFile) {
        try {
            initialize();

            GridPortal portal = null;
            if (confFile == null) {
                confFile = ResourceManager.getPortalConfigFileLocation();
            }
            portal = new GridPortal(confFile);
            Dimension dim = PortalResourceManager.getInstance().getGridPortalConfig().getApplicationDimensions();
            try {
                portal.pack();
            } catch (Exception e) {
                portal.setIconImage(null);
                portal.pack();
            }
            portal.setSize(dim);
            portal.setVisible(true);
            portal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (MobiusException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private static void checkGlobusLocation() {
        String currGlobusLocation = ResourceManager.getConfigurationProperty(IntroduceConstants.GLOBUS_LOCATION);
        if ((currGlobusLocation == null) || (currGlobusLocation.length() == 0)) {
            try {
                String globusLocation = System.getenv("GLOBUS_LOCATION");
                ResourceManager.setConfigurationProperty(IntroduceConstants.GLOBUS_LOCATION, globusLocation);
            } catch (Throwable ex) {
                ex.printStackTrace();
                String[] error = {"Error getting GLOBUS_LOCATION environment variable: ", ex.getMessage(),
                        "Please set GLOBUS_LOCATION in preferences!"};
                ErrorDialog.showErrorDialog("Error getting GLOBUS_LOCATION", error);
                try {
                    ResourceManager.setConfigurationProperty(IntroduceConstants.GLOBUS_LOCATION, "");
                } catch (Exception configEx) {
                    // now what?
                    configEx.printStackTrace();
                    ErrorDialog.showErrorDialog(configEx);
                }
            }
        }
    }


    private static final class IntroduceSplashCloser implements Runnable {
        public void run() {
            try {
                introduceSplash.dispose();
            } catch (Exception e) {

            }
        }
    }
}
