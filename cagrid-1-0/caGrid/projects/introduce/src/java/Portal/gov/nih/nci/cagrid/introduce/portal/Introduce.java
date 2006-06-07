package gov.nih.nci.cagrid.introduce.portal;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import org.projectmobius.common.MobiusException;
import org.projectmobius.portal.GridPortal;
import org.projectmobius.portal.PortalResourceManager;

public final class Introduce {

	public static void main(String[] args) {
		Introduce duce = new Introduce();
		showIntroduceSplash();
		if (args.length > 0) {
			showGridPortal(args[0]);
		} else {
			showGridPortal(null);
		}
		EventQueue.invokeLater(new IntroduceSplashCloser());
	}

	private static IntroduceSplash introduceSplash;

	private static void showIntroduceSplash() {
		introduceSplash = new IntroduceSplash("/introduceSplash.gif");
		introduceSplash.splash();
	}

	private static void showGridPortal(String confFile) {
		try {
			GridPortal portal = null;
			if (confFile != null) {
				portal = new GridPortal(confFile);
			} else {
				portal = new GridPortal(
						"conf/introduce/introduce-portal-conf.xml");
			}
			Dimension dim = PortalResourceManager.getInstance()
					.getGridPortalConfig().getApplicationDimensions();
			portal.pack();
			portal.setSize(dim);
			portal.setVisible(true);
			portal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (MobiusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static final class IntroduceSplashCloser implements Runnable {
		public void run() {
			introduceSplash.dispose();
		}
	}
}
