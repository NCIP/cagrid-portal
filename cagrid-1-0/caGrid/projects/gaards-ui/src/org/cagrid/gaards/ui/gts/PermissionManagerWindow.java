package org.cagrid.gaards.ui.gts;

import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.gts.bean.Permission;
import gov.nih.nci.cagrid.gts.bean.PermissionFilter;
import gov.nih.nci.cagrid.gts.client.GTSAdminClient;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.cagrid.gaards.ui.common.CredentialComboBox;
import org.cagrid.grape.ApplicationComponent;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.ErrorDialog;
import org.globus.gsi.GlobusCredential;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: TrustedAuthoritiesWindow.java,v 1.2 2006/03/27 19:05:40
 *          langella Exp $
 */
public class PermissionManagerWindow extends ApplicationComponent implements PermissionRefresher {
	
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel contentPanel = null;

	private JPanel buttonPanel = null;

	private PermissionsTable permissionsTable = null;

	private JScrollPane jScrollPane = null;

	private JButton addPermission = null;

	private JPanel jPanel = null;

	private JPanel jPanel2 = null;

	private JLabel jLabel14 = null;

	private JPanel queryPanel = null;

	private JButton query = null;

	private JComboBox service = null;

	private JLabel proxyLabel = null;

	private JComboBox proxy = null;

	private JPanel progressPanel = null;

	private JProgressBar progress = null;

	private JButton removePermissionButton = null;

	private PermissionPanel filterPanel = null;

	private String currentService = null;

	private boolean searchDone = false;


	/**
	 * This is the default constructor
	 */
	public PermissionManagerWindow() {
		super();
		initialize();
		this.setFrameIcon(GTSLookAndFeel.getAdminIcon());
		syncServices();
	}


	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(700, 500);
		this.setContentPane(getJContentPane());
		this.setTitle("GTS Access Management");
	}


	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getMainPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints21.weightx = 1.0D;
			gridBagConstraints21.weighty = 1.0D;
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints21.gridy = 4;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints.gridy = 1;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.gridx = 0;
			gridBagConstraints32.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints32.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints32.weightx = 1.0D;
			gridBagConstraints32.gridy = 3;
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.gridx = 0;
			gridBagConstraints33.gridy = 2;
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.gridx = 0;
			gridBagConstraints35.weightx = 1.0D;
			gridBagConstraints35.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints35.gridy = 0;

			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 5;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			mainPanel.add(getButtonPanel(), gridBagConstraints2);
			mainPanel.add(getJPanel(), gridBagConstraints35);
			mainPanel.add(getQueryPanel(), gridBagConstraints33);
			mainPanel.add(getProgressPanel(), gridBagConstraints32);
			mainPanel.add(getFilterPanel(), gridBagConstraints);
			mainPanel.add(getContentPanel(), gridBagConstraints21);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());
			contentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Permission(s)",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, LookAndFeel.getPanelLabelColor()));
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			contentPanel.add(getJScrollPane(), gridBagConstraints4);
		}
		return contentPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getAddPermission(), null);
			buttonPanel.add(getRemovePermissionButton(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private PermissionsTable getPermissionsTable() {
		if (permissionsTable == null) {
			permissionsTable = new PermissionsTable();
		}
		return permissionsTable;
	}


	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getPermissionsTable());
		}
		return jScrollPane;
	}


	/**
	 * This method initializes manageUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddPermission() {
		if (addPermission == null) {
			addPermission = new JButton();
			addPermission.setText("Add Permission");
			addPermission.setIcon(GTSLookAndFeel.getAddIcon());
			addPermission.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							disableAllActions();
							addPermission();
							enableAllActions();
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

		return addPermission;
	}


	public void addPermission() {
		try {
			String selectedService = ((GTSServiceListComboBox) getService()).getSelectedService();
			GlobusCredential selectedProxy = ((CredentialComboBox) getProxy()).getSelectedCredential();
			GridApplication.getContext().addApplicationComponent(
                new AddPermissionWindow(selectedService, selectedProxy, this), 700, 350);
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints34.gridy = 0;
			gridBagConstraints34.weightx = 1.0D;
			gridBagConstraints34.gridx = 0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getJPanel2(), gridBagConstraints34);
		}
		return jPanel;
	}


	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints30.gridx = 1;
			gridBagConstraints30.gridy = 1;
			gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints30.weightx = 1.0;
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.gridx = 0;
			gridBagConstraints29.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints29.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints29.gridy = 1;
			proxyLabel = new JLabel();
			proxyLabel.setText("Proxy");
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints28.gridx = 1;
			gridBagConstraints28.gridy = 0;
			gridBagConstraints28.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints28.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints28.weightx = 1.0;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.gridwidth = 1;
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.gridy = 0;
			gridBagConstraints31.insets = new Insets(2, 2, 2, 2);
			jLabel14 = new JLabel();
			jLabel14.setText("Service");
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GridBagLayout());
			jPanel2.setBorder(BorderFactory.createTitledBorder(null, "GTS/Login Information",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, 
                LookAndFeel.getPanelLabelColor()));
			jPanel2.add(jLabel14, gridBagConstraints31);
			jPanel2.add(getService(), gridBagConstraints28);
			jPanel2.add(proxyLabel, gridBagConstraints29);
			jPanel2.add(getProxy(), gridBagConstraints30);
		}
		return jPanel2;
	}


	/**
	 * This method initializes queryPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getQueryPanel() {
		if (queryPanel == null) {
			queryPanel = new JPanel();
			queryPanel.add(getQuery(), null);
		}
		return queryPanel;
	}


	/**
	 * This method initializes query
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getQuery() {
		if (query == null) {
			query = new JButton();
			query.setText("Find Permissions");
			query.setIcon(LookAndFeel.getQueryIcon());
			query.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							disableAllActions();
							findPermissions();
							enableAllActions();
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
		return query;
	}


	private void findPermissions() {

		this.getPermissionsTable().clearTable();
		this.updateProgress(true, "Finding Permissions...");

		try {
			String selectedService = ((GTSServiceListComboBox) getService()).getSelectedService();
			GlobusCredential selectedProxy = ((CredentialComboBox) getProxy()).getSelectedCredential();
			PermissionFilter f = filterPanel.getPermissionFilter();
			GTSAdminClient client = new GTSAdminClient(selectedService, selectedProxy);
			Permission[] perms = client.findPermissions(f);
			int length = 0;
			if (perms != null) {
				length = perms.length;
				for (int i = 0; i < perms.length; i++) {
					this.permissionsTable.addPermission(perms[i]);
				}
			}
			searchDone = true;
			this.updateProgress(false, "Completed [Found " + length + " Permission(s)]");

		} catch (Exception e) {
			ErrorDialog.showError(e);
			this.updateProgress(false, "Error");
		}

	}


	/**
	 * This method initializes service
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getService() {
		if (service == null) {
			service = new GTSServiceListComboBox();
			service.addActionListener(new java.awt.event.ActionListener() {
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
		return service;
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
	 * This method initializes progressPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getProgressPanel() {
		if (progressPanel == null) {
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.insets = new java.awt.Insets(2, 20, 2, 20);
			gridBagConstraints36.gridy = 0;
			gridBagConstraints36.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints36.weightx = 1.0D;
			gridBagConstraints36.gridx = 0;
			progressPanel = new JPanel();
			progressPanel.setLayout(new GridBagLayout());
			progressPanel.add(getProgress(), gridBagConstraints36);
		}
		return progressPanel;
	}


	/**
	 * This method initializes progress
	 * 
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getProgress() {
		if (progress == null) {
			progress = new JProgressBar();
			progress.setForeground(LookAndFeel.getPanelLabelColor());
			progress.setString("");
			progress.setStringPainted(true);
		}
		return progress;
	}


	public void updateProgress(final boolean working, final String s) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getProgress().setString(s);
				getProgress().setIndeterminate(working);
			}
		});

	}


	/**
	 * This method initializes removeUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemovePermissionButton() {
		if (removePermissionButton == null) {
			removePermissionButton = new JButton();
			removePermissionButton.setText("Remove Permission");
			removePermissionButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							disableAllActions();
							removePermission();
							enableAllActions();
						}
					};
					try {
						GridApplication.getContext().executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}
				}
			});
			removePermissionButton.setIcon(GTSLookAndFeel.getRemoveIcon());
		}
		return removePermissionButton;
	}


	private void removePermission() {
		try {
			String selectedService = ((GTSServiceListComboBox) getService()).getSelectedService();
			GlobusCredential selectedProxy = ((CredentialComboBox) getProxy()).getSelectedCredential();
			GTSAdminClient client = new GTSAdminClient(selectedService, selectedProxy);
			client.revokePermission(this.permissionsTable.getSelectedPermission());
			this.refreshPermissions();

		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}


	/**
	 * This method initializes filterPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private PermissionPanel getFilterPanel() {
		if (filterPanel == null) {
			filterPanel = new PermissionPanel(true);
			filterPanel.setBorder(BorderFactory.createTitledBorder(null, "Search Criteria",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, 
                LookAndFeel.getPanelLabelColor()));
		}
		return filterPanel;
	}


	private synchronized void syncServices() {
		this.getPermissionsTable().clearTable();
		String selectedService = ((GTSServiceListComboBox) getService()).getSelectedService();
		if ((currentService == null) || (!currentService.equals(selectedService))) {
			try {
				currentService = selectedService;
				updateProgress(true, "Discovery Trusted Authorities...");
				int length = filterPanel.syncWithService(selectedService);
				this.updateProgress(false, "Completed [Found " + length + " Trusted Authority(s)]");
			} catch (Exception e) {
				ErrorDialog.showError(e);
				updateProgress(false, "Error!!!");
			}
		}

	}


	private void disableAllActions() {
		getQuery().setEnabled(false);
		getAddPermission().setEnabled(false);
		getRemovePermissionButton().setEnabled(false);
	}


	private void enableAllActions() {
		getQuery().setEnabled(true);
		getAddPermission().setEnabled(true);
		getRemovePermissionButton().setEnabled(true);
	}


	public void refreshPermissions() {
		if (searchDone) {
			disableAllActions();
			findPermissions();
			enableAllActions();
		}

	}

}
