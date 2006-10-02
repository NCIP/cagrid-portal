package gov.nih.nci.cagrid.gridgrouper.ui;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.projectmobius.common.MobiusException;
import org.projectmobius.portal.GridPortal;
import org.projectmobius.portal.PortalResourceManager;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
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
}
