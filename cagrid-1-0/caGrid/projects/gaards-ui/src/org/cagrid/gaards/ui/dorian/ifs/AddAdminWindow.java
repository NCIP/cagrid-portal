package org.cagrid.gaards.ui.dorian.ifs;

import gov.nih.nci.cagrid.common.Runner;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cagrid.gaards.dorian.client.IFSAdministrationClient;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.ErrorDialog;
import org.globus.gsi.GlobusCredential;


public class AddAdminWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel servicePanel = null;

	private JPanel buttonPanel = null;

	private JButton addAdminButton = null;

	private JButton cancelButton = null;

	private JLabel jLabel = null;

	private JTextField serviceURI = null;

	private String uri;

	private GlobusCredential cred;

	private JLabel jLabel1 = null;

	private JTextField credential = null;

	private JPanel userPanel = null;

	private JLabel jLabel2 = null;

	private JTextField gridIdentity = null;

	private JButton findUserButton = null;


	/**
	 * This is the default constructor
	 */
	public AddAdminWindow(String uri, GlobusCredential cred) {
		super(GridApplication.getContext().getApplication());
		this.uri = uri;
		this.cred = cred;
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(500, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("Add Administrator");
		// this.setIconImage(DorianLookAndFeel.getAdminIcon().getImage());
	}


	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridheight = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 0.0D;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getServicePanel(), gridBagConstraints);
			jContentPane.add(getButtonPanel(), gridBagConstraints1);
			jContentPane.add(getUserPanel(), gridBagConstraints11);
		}
		return jContentPane;
	}


	/**
	 * This method initializes servicePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getServicePanel() {
		if (servicePanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 1;
			jLabel1 = new JLabel();
			jLabel1.setText("Credential");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints3.gridx = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			jLabel = new JLabel();
			jLabel.setText("Service URI");
			servicePanel = new JPanel();
			servicePanel.setLayout(new GridBagLayout());
			servicePanel.add(jLabel, gridBagConstraints3);
			servicePanel.add(getServiceURI(), gridBagConstraints2);
			servicePanel.add(jLabel1, gridBagConstraints4);
			servicePanel.add(getCredential(), gridBagConstraints5);
		}
		return servicePanel;
	}


	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());
			buttonPanel.add(getAddAdminButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes addAdmin
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddAdminButton() {
		if (addAdminButton == null) {
			addAdminButton = new JButton();
			addAdminButton.setText("Add Admin");
			addAdminButton.setIcon(LookAndFeel.getAddIcon());
			addAdminButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							addAdmin();
						}
					};
					try {
						GridApplication.getContext().executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}

				}
			});
		}
		return addAdminButton;
	}


	private void addAdmin() {
		try {
			addAdminButton.setEnabled(false);
			IFSAdministrationClient client = new IFSAdministrationClient(uri, cred);
			client.addAdmin(getGridIdentity().getText());
			dispose();
		} catch (Exception e) {
			ErrorDialog.showError(e);
			addAdminButton.setEnabled(true);
		}
	}


	/**
	 * This method initializes cancelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.setIcon(LookAndFeel.getCloseIcon());
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancelButton;
	}


	/**
	 * This method initializes serviceURI
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getServiceURI() {
		if (serviceURI == null) {
			serviceURI = new JTextField();
			serviceURI.setEditable(false);
			serviceURI.setText(uri);
		}
		return serviceURI;
	}


	/**
	 * This method initializes credential
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCredential() {
		if (credential == null) {
			credential = new JTextField();
			credential.setEditable(false);
			credential.setText(cred.getIdentity());
		}
		return credential;
	}


	/**
	 * This method initializes userPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getUserPanel() {
		if (userPanel == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints7.weightx = 1.0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridwidth = 2;
			gridBagConstraints6.gridy = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("Grid Identity");
			jLabel2.setForeground(LookAndFeel.getPanelLabelColor());
			jLabel2.setFont(new Font("Dialog", Font.BOLD, 14));
			userPanel = new JPanel();
			userPanel.setLayout(new GridBagLayout());
			userPanel.add(jLabel2, gridBagConstraints6);
			userPanel.add(getGridIdentity(), gridBagConstraints7);
			userPanel.add(getFindUserButton(), gridBagConstraints8);
		}
		return userPanel;
	}


	/**
	 * This method initializes gridIdentity
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getGridIdentity() {
		if (gridIdentity == null) {
			gridIdentity = new JTextField();
		}
		return gridIdentity;
	}


	/**
	 * This method initializes findUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getFindUserButton() {
		if (findUserButton == null) {
			findUserButton = new JButton();
			findUserButton.setText("Find...");
			findUserButton.setIcon(LookAndFeel.getQueryIcon());
			findUserButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					FindUserDialog dialog = new FindUserDialog();
					dialog.setModal(true);
					GridApplication.getContext().showDialog(dialog);
					if (dialog.getSelectedUser() != null) {
						gridIdentity.setText(dialog.getSelectedUser());
					}
				}
			});
		}
		return findUserButton;
	}

}
