package gov.nih.nci.cagrid.dorian.ui;

import gov.nih.nci.cagrid.common.portal.SplashScreen;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import org.projectmobius.common.MobiusException;
import org.projectmobius.portal.GridPortal;
import org.projectmobius.portal.PortalResourceManager;


public final class DorianPortal {

	public static final String DEFAULT_CONFIG_FILE = "etc/dorian-portal-conf.xml";

	private static SplashScreen dorianSplash;


	public static void main(String[] args) {
		showDorianSplash();
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (args.length > 0) {
			showGridPortal(args[0]);
		} else {
			showGridPortal(null);
		}
		EventQueue.invokeLater(new DorianSplashCloser());
	}


	private static void showDorianSplash() {
		dorianSplash = new SplashScreen("/dorianSplash.png");
	}


	private static void showGridPortal(String confFile) {
		try {
			GridPortal portal = null;
			if (confFile == null) {
				confFile = DEFAULT_CONFIG_FILE;
			}
			portal = new GridPortal(confFile);
			Dimension dim = PortalResourceManager.getInstance().getGridPortalConfig().getApplicationDimensions();
			portal.pack();
			portal.setSize(dim);
			portal.setVisible(true);
			portal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (MobiusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static final class DorianSplashCloser implements Runnable {
		public void run() {
			dorianSplash.dispose();
		}
	}
}
