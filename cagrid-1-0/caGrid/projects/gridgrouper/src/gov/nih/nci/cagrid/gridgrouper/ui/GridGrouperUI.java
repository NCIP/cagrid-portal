package gov.nih.nci.cagrid.gridgrouper.ui;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.projectmobius.common.MobiusException;
import org.projectmobius.portal.GridPortal;
import org.projectmobius.portal.PortalResourceManager;

public final class GridGrouperUI {

	public static final String DEFAULT_CONFIG_FILE = "etc/grid-grouper-ui.xml";

	public static void main(String[] args) {

		if (args.length > 0) {
			showGridPortal(args[0]);
		} else {
			showGridPortal(null);
		}
	}

	private static void showGridPortal(String confFile) {
		try {
			GridPortal portal = null;
			if (confFile == null) {
				confFile = DEFAULT_CONFIG_FILE;
			}
			portal = new GridPortal(confFile);
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
}
