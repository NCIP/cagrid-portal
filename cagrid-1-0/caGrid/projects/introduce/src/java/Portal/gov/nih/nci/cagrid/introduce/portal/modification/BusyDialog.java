package gov.nih.nci.cagrid.introduce.portal.modification;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class BusyDialog extends JDialog {

	private JPanel mainPanel = null;

	private JProgressBar progress = null;

	private Runnable r;

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
		this.setSize(new java.awt.Dimension(362, 127));
		this.setContentPane(getMainPanel());
	}

	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getProgress(), gridBagConstraints);
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
			progress.setString("");
			progress.setStringPainted(true);
		}
		return progress;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
