package gov.nih.nci.cagrid.introduce.portal;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.SplashScreen;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.portal.GridPortal;
import org.projectmobius.portal.PortalResourceManager;


public final class Introduce {

	public static final String DEFAULT_CONFIG_FILE = "conf/introduce/introduce-portal-conf.xml";

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
				confFile = DEFAULT_CONFIG_FILE;
			}
			checkGlobusLocation(confFile);
			portal = new GridPortal(confFile);
			Dimension dim = PortalResourceManager.getInstance().getGridPortalConfig().getApplicationDimensions();
			portal.setSize(dim);
			try {
				portal.pack();
			} catch (Exception e) {
				portal.setIconImage(null);
				portal.setSize(dim);
				portal.pack();
			}
			portal.setVisible(true);
			portal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (MobiusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static void checkGlobusLocation(String configFilename) {
		try {
			Document doc = XMLUtilities.fileNameToDocument(new File(configFilename).getAbsolutePath());
			Element resource = (Element) doc.getRootElement().getChildren("resource").get(1);
			Element globusConfig = resource.getChild("introduce-portal-config").getChild("globusLocation");
			if (globusConfig.getText() == null || globusConfig.getText().length() == 0) {
				try {
					String globusLocation = System.getenv("GLOBUS_LOCATION");
					globusConfig.setText(globusLocation);
				} catch (Exception ex) {
					ex.printStackTrace();
					// not using PortalUtils.showErrorMessage because at this
					// point,
					// there IS no grid portal instance yet
					String[] error = {"Error getting GLOBUS_LOCATION environment variable: ", ex.getMessage(),
							"Please set GLOBUS_LOCATION in preferences!"};
					// JOptionPane.showMessageDialog(null, error, "Configuration
					// Error", JOptionPane.ERROR_MESSAGE);
					ErrorDialog.showErrorDialog(error);
				}
			}
			// write the configuration back out to disk
			FileWriter fw = new FileWriter(configFilename);
			fw.write(XMLUtilities.formatXML(XMLUtilities.documentToString(doc)));
			fw.flush();
			fw.close();
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
