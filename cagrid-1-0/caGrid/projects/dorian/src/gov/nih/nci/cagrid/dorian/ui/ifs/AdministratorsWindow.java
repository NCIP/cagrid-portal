package gov.nih.nci.cagrid.dorian.ui.ifs;

import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.dorian.client.IFSAdministrationClient;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.ui.DorianLookAndFeel;
import gov.nih.nci.cagrid.dorian.ui.DorianServiceListComboBox;
import gov.nih.nci.cagrid.gridca.ui.ProxyComboBox;

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

import org.cagrid.grape.ApplicationComponent;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.ErrorDialog;
import org.globus.gsi.GlobusCredential;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: AdministratorsWindow.java,v 1.1 2007-04-26 18:43:49 langella Exp $
 */
public class AdministratorsWindow extends ApplicationComponent {

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel contentPanel = null;

	private JPanel buttonPanel = null;

	private AdminsTable adminsTable = null;

	private JScrollPane jScrollPane = null;

	private JButton viewEditAdmin = null;

	private JPanel jPanel = null;

	private JPanel jPanel2 = null;

	private JLabel jLabel14 = null;

	private JPanel queryPanel = null;

	private JButton query = null;

	private JComboBox service = null;

	private boolean isQuerying = false;

	private Object mutex = new Object();

	private JLabel proxyLabel = null;

	private JComboBox proxy = null;

	private JPanel progressPanel = null;

	private JProgressBar progress = null;

	private JButton removeTrustedIdPButton = null;

	private JButton addAdmin = null;

	/**
	 * This is the default constructor
	 */
	public AdministratorsWindow() {
		super();
		initialize();
		this.setFrameIcon(DorianLookAndFeel.getAdminIcon());
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("Trusted Identity Provider Management");

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
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.gridx = 0;
			gridBagConstraints32.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints32.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints32.weightx = 1.0D;
			gridBagConstraints32.gridy = 2;
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.gridx = 0;
			gridBagConstraints33.gridy = 1;
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.gridx = 0;
			gridBagConstraints35.weightx = 1.0D;
			gridBagConstraints35.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints35.gridy = 0;

			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 3;
			gridBagConstraints1.ipadx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 6;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			mainPanel.add(getButtonPanel(), gridBagConstraints2);
			mainPanel.add(getContentPanel(), gridBagConstraints1);
			mainPanel.add(getJPanel(), gridBagConstraints35);
			mainPanel.add(getQueryPanel(), gridBagConstraints33);
			mainPanel.add(getProgressPanel(), gridBagConstraints32);
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
			contentPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Administrators",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, LookAndFeel.getPanelLabelColor()));
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
			buttonPanel.add(getViewEditAdmin(), null);
			buttonPanel.add(getAddAdmin(), null);
			buttonPanel.add(getRemoveTrustedIdPButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private AdminsTable getAdminsTable() {
		if (adminsTable == null) {
			adminsTable = new AdminsTable();
		}
		return adminsTable;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getAdminsTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes manageUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getViewEditAdmin() {
		if (viewEditAdmin == null) {
			viewEditAdmin = new JButton();
			viewEditAdmin.setText("View/Edit Admin");
			viewEditAdmin.setIcon(DorianLookAndFeel.getAdminIcon());
			viewEditAdmin
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							Runner runner = new Runner() {
								public void execute() {
									showAdmin();
								}
							};
							try {
								GridApplication.getContext()
										.executeInBackground(runner);
							} catch (Exception t) {
								t.getMessage();
							}
						}

					});
		}

		return viewEditAdmin;
	}

	public void showAdmin() {
		try {

			String serviceUrl = ((DorianServiceListComboBox) getService())
					.getSelectedService();
			GlobusCredential proxyCred = ((ProxyComboBox) getProxy())
					.getSelectedProxy();
			IFSAdministrationClient client = new IFSAdministrationClient(
					serviceUrl, proxyCred);
			IFSUserFilter f = new IFSUserFilter();
			f.setGridId(getAdminsTable().getSelectedAdmin());
			IFSUser[] users = client.findUsers(f);
			if ((users == null) || (users.length == 0)) {
				throw new Exception(
						"The administrator selected does not have an account with this Dorian.");
			} else {
				IFSUser user = users[0];
				TrustedIdP[] idps = client.getTrustedIdPs();
				TrustedIdP tidp = null;
				for (int i = 0; i < idps.length; i++) {
					if (idps[i].getId() == user.getIdPId()) {
						tidp = idps[i];
						break;
					}
				}
				GridApplication.getContext().addApplicationComponent(
						new UserWindow(serviceUrl, proxyCred, user, tidp));
			}
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}

	public void addAdmin() {
		try {
			String serviceUrl = ((DorianServiceListComboBox) getService())
					.getSelectedService();
			GlobusCredential proxyCred = ((ProxyComboBox) getProxy())
					.getSelectedProxy();
			GridApplication.getContext().addApplicationComponent(
					new AddAdminWindow(serviceUrl, proxyCred), 500, 200);

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
			jPanel2.setBorder(BorderFactory.createTitledBorder(null,
					"Login Information", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, LookAndFeel
							.getPanelLabelColor()));
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
			query.setText("List Administrators");
			query.setIcon(LookAndFeel.getQueryIcon());
			query.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							listAdmins();
						}
					};
					try {
						GridApplication.getContext()
								.executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}

				}
			});
		}
		return query;
	}

	private void listAdmins() {

		synchronized (mutex) {
			if (isQuerying) {
				ErrorDialog
						.showError("Query Already in Progress",
								"Please wait until the current query is finished before executing another.");
				return;
			} else {
				isQuerying = true;
			}
		}

		this.getAdminsTable().clearTable();
		this.updateProgress(true, "Finding Administrators...");

		try {
			GlobusCredential proxyCred = ((ProxyComboBox) getProxy())
					.getSelectedProxy();
			String serviceUrl = ((DorianServiceListComboBox) getService())
					.getSelectedService();
			IFSAdministrationClient client = new IFSAdministrationClient(
					serviceUrl, proxyCred);
			String[] admins = client.getAdmins();

			int length = 0;
			if (admins != null) {
				length = admins.length;
				for (int i = 0; i < admins.length; i++) {
					this.getAdminsTable().addAdmin(admins[i]);
				}
			}
			this.updateProgress(false, "Completed [Found " + length
					+ " Administrators]");

		} catch (PermissionDeniedFault pdf) {
			ErrorDialog.showError(pdf);
			this.updateProgress(false, "Error");
		} catch (Exception e) {
			ErrorDialog.showError(e);
			this.updateProgress(false, "Error");
		}
		isQuerying = false;

	}

	private IFSUserPolicy[] getUserPolicies() throws Exception {
		GlobusCredential proxyCred = ((ProxyComboBox) getProxy())
				.getSelectedProxy();
		String serviceUrl = ((DorianServiceListComboBox) getService())
				.getSelectedService();
		IFSAdministrationClient client = new IFSAdministrationClient(
				serviceUrl, proxyCred);
		return client.getUserPolicies();
	}

	/**
	 * This method initializes service
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getService() {
		if (service == null) {
			service = new DorianServiceListComboBox();
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
			proxy = new ProxyComboBox();
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
	private JButton getRemoveTrustedIdPButton() {
		if (removeTrustedIdPButton == null) {
			removeTrustedIdPButton = new JButton();
			removeTrustedIdPButton.setText("Remove Admin");
			removeTrustedIdPButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							Runner runner = new Runner() {
								public void execute() {
									removeAdmin();
								}
							};
							try {
								GridApplication.getContext()
										.executeInBackground(runner);
							} catch (Exception t) {
								t.getMessage();
							}
						}
					});
			removeTrustedIdPButton.setIcon(DorianLookAndFeel
					.getRemoveIcon());
		}
		return removeTrustedIdPButton;
	}

	private void removeAdmin() {
		try {
			String serviceUrl = ((DorianServiceListComboBox) getService())
					.getSelectedService();
			GlobusCredential proxyCred = ((ProxyComboBox) getProxy())
					.getSelectedProxy();
			IFSAdministrationClient client = new IFSAdministrationClient(
					serviceUrl, proxyCred);
			client.removeAdmin(getAdminsTable().getSelectedAdmin());
			getAdminsTable().removeSelectedAdmin();
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}

	/**
	 * This method initializes addAdmin
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddAdmin() {
		if (addAdmin == null) {
			addAdmin = new JButton();
			addAdmin.setText("Add Admin");
			addAdmin.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							addAdmin();
						}
					};
					try {
						GridApplication.getContext()
								.executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}

				}
			});
			addAdmin.setIcon(DorianLookAndFeel.getAddIcon());
		}
		return addAdmin;
	}

}
