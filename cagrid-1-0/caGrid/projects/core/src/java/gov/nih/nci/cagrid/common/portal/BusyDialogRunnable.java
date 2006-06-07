package gov.nih.nci.cagrid.common.portal;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public abstract class BusyDialogRunnable implements Runnable {

	final private BusyDialog dialog;

	private String errorMessage = "";

	private boolean valid = true;

	private JFrame owner;

	public BusyDialogRunnable(JFrame owner, String title) {
		this.owner = owner;
		dialog = new BusyDialog(owner, "Progress (" + title + ")");
	}

	public void setProgressText(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				dialog.getProgress().setString(text);
			}
		});
	}

	public void run() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				dialog.setVisible(true);
			}
		});
		thread.start();
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					dialog.getProgress().setIndeterminate(true);
				}
			});
			process();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					dialog.getProgress().setIndeterminate(false);
				}
			});
		} catch (Throwable e) {
			e.printStackTrace();
			valid = false;
			errorMessage = e.getMessage();
		}

		if (!valid) {
			JOptionPane.showMessageDialog(owner, errorMessage);
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				dialog.dispose();
			}
		});

	}

	public abstract void process();

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String message) {
		this.valid = false;
		this.errorMessage = message;
	}

}
