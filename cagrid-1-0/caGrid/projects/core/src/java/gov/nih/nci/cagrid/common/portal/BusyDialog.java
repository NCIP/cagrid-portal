package gov.nih.nci.cagrid.common.portal;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


public class BusyDialog extends JDialog {

	private JPanel mainPanel = null;
	private JProgressBar progress = null;


	/**
	 * This method initializes
	 */
	public BusyDialog(JFrame owner, String title) {
		super(owner, title, true);
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(new java.awt.Dimension(362, 85));
		this.setContentPane(getMainPanel());
		this.getOwner();
		PortalUtils.centerWindow(this);
	}


	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.weightx = 1.0D;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getProgress(), gridBagConstraints4);
		}
		return mainPanel;
	}


	/**
	 * This method initializes progress
	 * 
	 * @return javax.swing.JProgressBar
	 */
	public JProgressBar getProgress() {
		if (progress == null) {
			progress = new JProgressBar();
			progress.setStringPainted(true);
			progress.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
			progress.setForeground(new java.awt.Color(153, 153, 255));
			progress.setString("");
		}
		return progress;
	}
}
