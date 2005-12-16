package gov.nih.nci.cagrid.introduce.portal.deployment;

import gov.nih.nci.cagrid.common.portal.BusyDialogRunnable;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;

/**
 * CreationViewer
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class DeploymentViewer extends GridPortalComponent {


	private JPanel inputPanel = null;

	private JPanel mainPanel = null;

	private JPanel buttonPanel = null;

	private JButton deployButton = null;

	private JButton closeButton = null;

	/**
	 * This method initializes
	 */
	public DeploymentViewer() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getMainPanel());
		this.setSize(469, 446);
		this.setFrameIcon(IntroduceLookAndFeel.getCreateIcon());
		this.setTitle("Deploy Grid Service");

	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInputPanel() {
		if (inputPanel == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridy = 3;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridwidth = 2;
			gridBagConstraints10.weighty = 1.0D;
			gridBagConstraints10.gridx = 1;
			inputPanel = new JPanel();
			inputPanel.setLayout(new GridBagLayout());
			inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Deployment Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					IntroduceLookAndFeel.getPanelLabelColor()));
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 3;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
		}
		return inputPanel;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints1.gridheight = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridheight = 1;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridwidth = 1;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getInputPanel(), gridBagConstraints);
			mainPanel.add(getButtonPanel(), gridBagConstraints1);
		}
		return mainPanel;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getDeployButton(), null);
			buttonPanel.add(getCloseButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDeployButton() {
		if (deployButton == null) {
			deployButton = new JButton();
			deployButton.setText("Deploy");
			deployButton.setIcon(IntroduceLookAndFeel.getCreateIcon());
			deployButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					BusyDialogRunnable r = new BusyDialogRunnable(
							PortalResourceManager.getInstance().getGridPortal(),
							"Creating") {

						public void process() {
							try {
								
							} catch (Exception ex) {
								ex.printStackTrace();
								PortalUtils.showErrorMessage(ex.getMessage());
							}

						}

					};

					Thread th = new Thread(r);
					th.start();
				}
			});
		}

		return deployButton;
	}

	private String promptDir() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select Attribute File");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		} else {
			return "";
		}
	}

	private String promptFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select Attribute File");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		} else {
			return "";
		}
	}

	/**
	 * This method initializes closeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setIcon(IntroduceLookAndFeel.getCloseIcon());
			closeButton.setText("Cancel");
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return closeButton;
	}

	public static void main(String[] args) {
		System.out.println();

	}

}  //  @jve:decl-index=0:visual-constraint="10,4"
