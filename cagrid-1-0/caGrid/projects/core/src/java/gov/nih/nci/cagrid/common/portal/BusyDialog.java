package gov.nih.nci.cagrid.common.portal;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class BusyDialog extends JDialog {

	private JPanel mainPanel = null;

	private Runnable r;

	private JPanel progressPanel = null;

	private JProgressBar progress = null;

	/**
	 * This method initializes
	 * 
	 */
	public BusyDialog(JFrame owner, String title, Runnable r) {
		super(owner, title, true);
		this.r = r;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(new java.awt.Dimension(362, 85));
		this.setContentPane(getMainPanel());
		this.getOwner();
		// centers is to it's parent
		// Get the size of the screen
		Dimension dim = this.getOwner().getSize();

		// Determine the new location of the window
		int w = this.getSize().width;
		int h = this.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;

		// Move the window
		this.setLocation(x, y);
	}

	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getProgressPanel(), gridBagConstraints);
			// mainPanel.add(getDetailsPanel(), gridBagConstraints2);
		}
		return mainPanel;
	}

	/**
	 * This method initializes progressPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getProgressPanel() {
		if (progressPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.gridy = 0;
			progressPanel = new JPanel();
			progressPanel.setLayout(new GridBagLayout());
			progressPanel.add(getProgress(), gridBagConstraints4);
		}
		return progressPanel;
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
			progress
					.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
			progress.setForeground(new java.awt.Color(153, 153, 255));
			progress.setString("");
		}
		return progress;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
