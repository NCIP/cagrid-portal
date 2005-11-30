package gov.nih.nci.cagrid.common.portal;

import gov.nih.nci.cagrid.gums.common.IOUtils;

import javax.swing.JOptionPane;

import org.apache.axis.AxisFault;
import org.projectmobius.portal.PortalResourceManager;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class PortalUtils {

	public static void showErrorMessage(String msg) {
		showErrorMessage("Portal Error", msg);
	}

	public static void showErrorMessage(Exception e) {
		showErrorMessage("Portal Error", e);
	}

	public static void showConfigurationErrorMessage(String msg) {
		showErrorMessage("Portal Configuration Error", msg);
	}

	public static void showMessage(String msg) {
		showMessage("Information", msg);
	}

	public static void showMessage(String title, String msg) {
		JOptionPane.showMessageDialog(PortalResourceManager.getInstance()
				.getGridPortal(), msg, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showErrorMessage(String title, Exception e) {
		String mess = IOUtils.getExceptionMessage(e);
		JOptionPane.showMessageDialog(PortalResourceManager.getInstance()
				.getGridPortal(), mess, title, JOptionPane.ERROR_MESSAGE);
	}

	public static void showErrorMessage(String title, String msg) {
		JOptionPane.showMessageDialog(PortalResourceManager.getInstance()
				.getGridPortal(), msg, title, JOptionPane.ERROR_MESSAGE);
	}
}