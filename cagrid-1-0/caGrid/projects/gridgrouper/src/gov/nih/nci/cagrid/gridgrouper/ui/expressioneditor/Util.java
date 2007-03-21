package gov.nih.nci.cagrid.gridgrouper.ui.expressioneditor;

import gov.nih.nci.cagrid.common.Utils;

import javax.swing.JOptionPane;

import org.projectmobius.common.MobiusPoolManager;
import org.projectmobius.common.MobiusRunnable;


public class Util {

	private static MobiusPoolManager threadManager = new MobiusPoolManager();


	public static void executeInBackground(MobiusRunnable r) throws Exception {
		threadManager.executeInBackground(r);
	}


	public static void showErrorMessage(String msg) {
		showErrorMessage("Portal Error", msg);
	}


	public static void showErrorMessage(Exception e) {
		showErrorMessage("Portal Error", e);
	}


	public static void showConfigurationErrorMessage(String msg) {
		showErrorMessage("Portal Configuration Error", new String[]{msg});
	}


	public static void showMessage(String msg) {
		showMessage(new String[]{msg});
	}


	public static void showMessage(String[] msg) {
		showMessage("Information", msg);
	}


	public static void showMessage(String title, String msg) {
		showMessage(title, new String[]{msg});
	}


	public static void showMessage(String title, String[] msg) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}


	public static void showErrorMessage(String title, Exception e) {
		String mess = Utils.getExceptionMessage(e);
		JOptionPane.showMessageDialog(null, mess, title, JOptionPane.ERROR_MESSAGE);
	}


	public static void showErrorMessage(String title, String msg) {
		showErrorMessage(title, new String[]{msg});
	}


	public static void showErrorMessage(String title, String[] msg) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
	}
}
