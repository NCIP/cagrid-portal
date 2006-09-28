package gov.nih.nci.cagrid.introduce.portal;

import gov.nih.nci.cagrid.common.portal.SplashScreen;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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


	private static void showGridPortal(String confFile) {
		try {
			GridPortal portal = null;
			if (confFile == null) {
				confFile = ResourceManager.getPortalConfigFileLocation();
			}
			checkGlobusLocation();
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
		try {
			String currGlobusLocation = ResourceManager.getConfigurationProperty(IntroduceConstants.GLOBUS_LOCATION);
			if (currGlobusLocation == null || currGlobusLocation.length() == 0) {
				try {
					String globusLocation = System.getenv("GLOBUS_LOCATION");
					ResourceManager.setConfigurationProperty(IntroduceConstants.GLOBUS_LOCATION, globusLocation);
				} catch (Exception ex) {
					ResourceManager.setConfigurationProperty(IntroduceConstants.GLOBUS_LOCATION, "");
					ex.printStackTrace();
					String[] error = {"Error getting GLOBUS_LOCATION environment variable: ", ex.getMessage(),
							"Please set GLOBUS_LOCATION in preferences!"};
					JOptionPane.showMessageDialog(null, error, "ConfigurationError", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			String[] error = {"Error updating configuration:", ex.getMessage()};
			JOptionPane.showMessageDialog(null, error, "Configuration Error", JOptionPane.ERROR_MESSAGE);
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
