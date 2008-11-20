package org.cagrid.gaards.ui.gts;

import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.gts.client.GTSAdminClient;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.cagrid.gaards.ui.common.CredentialCaddy;
import org.cagrid.gaards.ui.common.CredentialComboBox;
import org.cagrid.grape.ApplicationComponent;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.ErrorDialog;
import org.globus.gsi.GlobusCredential;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class AddPermissionWindow extends ApplicationComponent {
	
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel topPanel = null;

	private JLabel jLabel = null;

	private JComboBox gts = null;

	private JLabel jLabel1 = null;

	private JComboBox proxy = null;

	private JPanel buttonPanel = null;

	private JButton addButton = null;

	private JButton cancelButton = null;

	private PermissionPanel permissionPanel = null;

	private PermissionRefresher refresher;


	/**
	 * This is the default constructor
	 */
	public AddPermissionWindow(String service, GlobusCredential cred, PermissionRefresher refresher) {
		super();
		this.refresher = refresher;
		initialize();
		this.gts.setSelectedItem(service);
		this.proxy.setSelectedItem(new CredentialCaddy(cred));
		syncServices();
	}


	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(600, 400);
		this.setContentPane(getJContentPane());
		this.setTitle("Add Permission");
		this.setFrameIcon(GTSLookAndFeel.getAdminIcon());
	}


	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.gridy = 2;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.gridy = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getTopPanel(), gridBagConstraints);
			jContentPane.add(getButtonPanel(), gridBagConstraints12);
			jContentPane.add(getPermissionPanel(), gridBagConstraints1);
		}
		return jContentPane;
	}


	/**
	 * This method initializes topPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTopPanel() {
		if (topPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 1;
			jLabel1 = new JLabel();
			jLabel1.setText("Select Proxy");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Grid Trust Service (GTS)");
			topPanel = new JPanel();
			topPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Service/Login Information",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, LookAndFeel.getPanelLabelColor()));
			topPanel.setLayout(new GridBagLayout());
			topPanel.add(jLabel, gridBagConstraints2);
			topPanel.add(getGts(), gridBagConstraints3);
			topPanel.add(jLabel1, gridBagConstraints4);
			topPanel.add(getProxy(), gridBagConstraints5);
		}
		return topPanel;
	}


	/**
	 * This method initializes gts
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getGts() {
		if (gts == null) {
			gts = new GTSServiceListComboBox();
			gts.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							syncServices();
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
		return gts;
	}


	/**
	 * This method initializes proxy
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getProxy() {
		if (proxy == null) {
			proxy = new CredentialComboBox();
		}
		return proxy;
	}


	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getAddButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes addButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.setText("Add Permission");
			addButton.setIcon(GTSLookAndFeel.getAddIcon());
			addButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							addPermission();
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

		return addButton;
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
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
			cancelButton.setIcon(LookAndFeel.getCloseIcon());
		}
		return cancelButton;
	}


	private void addPermission() {
		try {
			getAddButton().setEnabled(false);
			GlobusCredential selectedProxy = ((CredentialComboBox) getProxy()).getSelectedCredential();
			String service = ((GTSServiceListComboBox) getGts()).getSelectedService();
			GTSAdminClient client = new GTSAdminClient(service, selectedProxy);
			client.addPermission(permissionPanel.getPermission());
			refresher.refreshPermissions();
			dispose();
			GridApplication.getContext().showMessage("Succesfully add the permission!!!");
		} catch (Exception e) {
			getAddButton().setEnabled(true);
			ErrorDialog.showError(e);
		}

	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private PermissionPanel getPermissionPanel() {
		if (permissionPanel == null) {
			permissionPanel = new PermissionPanel(false);
			permissionPanel.setBorder(BorderFactory.createTitledBorder(null, "Permission",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, 
                LookAndFeel.getPanelLabelColor()));
		}
		return permissionPanel;
	}


	private synchronized void syncServices() {
		String service = ((GTSServiceListComboBox) getGts()).getSelectedService();
		try {
			permissionPanel.syncWithService(service);
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}

}
