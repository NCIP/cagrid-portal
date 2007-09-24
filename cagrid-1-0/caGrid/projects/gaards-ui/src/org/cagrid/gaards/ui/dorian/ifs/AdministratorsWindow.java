package org.cagrid.gaards.ui.dorian.ifs;

import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.dorian.client.IFSAdministrationClient;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.cagrid.gaards.ui.dorian.DorianLookAndFeel;
import org.cagrid.gaards.ui.dorian.SessionPanel;
import org.cagrid.grape.ApplicationComponent;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.ErrorDialog;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: AdministratorsWindow.java,v 1.1 2007/04/26 18:43:49 langella
 *          Exp $
 */
public class AdministratorsWindow extends ApplicationComponent {

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel contentPanel = null;

	private JPanel buttonPanel = null;

	private AdminsTable adminsTable = null;

	private JScrollPane jScrollPane = null;

	private JButton viewEditAdmin = null;

	private SessionPanel sessionPanel = null;

	private JPanel queryPanel = null;

	private JButton query = null;

	private boolean isQuerying = false;

	private Object mutex = new Object();

	private JPanel progressPanel = null;

	private JProgressBar progress = null;

	private JButton removeTrustedIdPButton = null;

	private JButton addAdmin = null;

	private boolean loaded = false;

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
			mainPanel.add(getSessionPanel(), gridBagConstraints35);
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
			adminsTable = new AdminsTable(this);
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
			IFSAdministrationClient client = getSessionPanel().getAdminClient();
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
						new UserWindow(getSessionPanel().getServiceURI(),
								getSessionPanel().getCredential(), user, tidp));
			}
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}

	public void addAdmin() {
		try {
			AddAdminWindow window = new AddAdminWindow(getSessionPanel()
					.getServiceURI(), getSessionPanel().getCredential());
			window.setModal(true);
			GridApplication.getContext().showDialog(window);
			if (loaded) {
				this.listAdmins();
			}
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}

	/**
	 * This method initializes sessionPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private SessionPanel getSessionPanel() {
		if (sessionPanel == null) {
			sessionPanel = new SessionPanel();
		}
		return sessionPanel;
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
			IFSAdministrationClient client = getSessionPanel().getAdminClient();
			String[] admins = client.getAdmins();

			int length = 0;
			if (admins != null) {
				length = admins.length;
				for (int i = 0; i < admins.length; i++) {
					this.getAdminsTable().addAdmin(admins[i]);
				}
			}

			loaded = true;
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
			removeTrustedIdPButton.setIcon(LookAndFeel.getRemoveIcon());
		}
		return removeTrustedIdPButton;
	}

	private void removeAdmin() {
		try {
			IFSAdministrationClient client = getSessionPanel().getAdminClient();
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
			addAdmin.setIcon(LookAndFeel.getAddIcon());
		}
		return addAdmin;
	}

}
