package org.cagrid.gaards.ui.gts;

import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.gts.bean.AuthorityGTS;
import gov.nih.nci.cagrid.gts.client.GTSAdminClient;
import gov.nih.nci.cagrid.gts.client.GTSPublicClient;

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
public class AuthorityManagerWindow extends ApplicationComponent implements AuthorityRefresher {
	
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel contentPanel = null;

	private JPanel buttonPanel = null;

	private AuthorityTable authorityTable = null;

	private JScrollPane jScrollPane = null;

	private JButton addAuthority = null;

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

	private JButton removeAuthority = null;

	private JButton viewModifyButton = null;

	private JPanel priorityPanel = null;

	private JButton increasePriority = null;

	private JButton decreasePriority = null;

	private JButton updatePriorities = null;

	private boolean searchDone = false;


	/**
	 * This is the default constructor
	 */
	public AuthorityManagerWindow() {
		super();
		initialize();
		this.setFrameIcon(GTSLookAndFeel.getAuthorityIcon());
	}


	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(700, 500);
		this.setContentPane(getJContentPane());
		this.setTitle("GTS Authority Management");
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
			gridBagConstraints21.gridy = 3;
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
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 4;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			mainPanel.add(getButtonPanel(), gridBagConstraints2);
			mainPanel.add(getJPanel(), gridBagConstraints35);
			mainPanel.add(getQueryPanel(), gridBagConstraints33);
			mainPanel.add(getProgressPanel(), gridBagConstraints32);
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
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints.gridy = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());
			contentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Authority(s)",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, LookAndFeel.getPanelLabelColor()));
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			contentPanel.add(getJScrollPane(), gridBagConstraints4);
			contentPanel.add(getPriorityPanel(), gridBagConstraints);
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
			buttonPanel.add(getAddAuthority(), null);
			buttonPanel.add(getViewModifyButton(), null);
			buttonPanel.add(getRemoveAuthority(), null);
			buttonPanel.add(getUpdatePriorities(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private AuthorityTable getAuthorityTable() {
		if (authorityTable == null) {
			authorityTable = new AuthorityTable(this);
		}
		return authorityTable;
	}


	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getAuthorityTable());
		}
		return jScrollPane;
	}


	/**
	 * This method initializes manageUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddAuthority() {
		if (addAuthority == null) {
			addAuthority = new JButton();
			addAuthority.setText("Add Authority");
			addAuthority.setIcon(GTSLookAndFeel.getAddIcon());
			addAuthority.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							disableAllActions();
							addAuthority();
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

		return addAuthority;
	}


	public void addAuthority() {
		try {

			String selectedService = ((GTSServiceListComboBox) getService()).getSelectedService();
			GlobusCredential selectedProxy = ((CredentialComboBox) getProxy()).getSelectedCredential();
			GridApplication.getContext().addApplicationComponent(new AuthorityWindow(selectedService, selectedProxy, this), 700, 450);
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}


	public void viewModifyAuthority() {
		try {
			String selectedService = ((GTSServiceListComboBox) getService()).getSelectedService();
			GlobusCredential selectedProxy = ((CredentialComboBox) getProxy()).getSelectedCredential();
			GridApplication.getContext().addApplicationComponent(
				new AuthorityWindow(selectedService, selectedProxy, getAuthorityTable().getSelectedAuthority(), this), 700, 450);
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
			query.setText("Find Authorities");
			query.setIcon(LookAndFeel.getQueryIcon());
			query.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					disableAllActions();
					Runner runner = new Runner() {
						public void execute() {
							getAuthorities();
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


	private void getAuthorities() {

		this.getAuthorityTable().clearTable();
		this.updateProgress(true, "Finding Authorities...");

		try {
			String selectedService = ((GTSServiceListComboBox) getService()).getSelectedService();
			GTSPublicClient client = new GTSPublicClient(selectedService);
			AuthorityGTS[] auth = client.getAuthorities();
			int length = 0;
			if (auth != null) {
				length = auth.length;
				for (int i = 0; i < auth.length; i++) {
					this.getAuthorityTable().addAuthority(auth[i]);
				}
			}
			searchDone = true;
			this.updateProgress(false, "Completed [Found " + length + " Authority(s)]");

		} catch (Exception e) {
			ErrorDialog.showError(e);
			this.updateProgress(false, "Error");
		} finally {
			enableAllActions();
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
	private JButton getRemoveAuthority() {
		if (removeAuthority == null) {
			removeAuthority = new JButton();
			removeAuthority.setText("Remove Authority");
			removeAuthority.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					disableAllActions();
					Runner runner = new Runner() {
						public void execute() {
							removeAuthority();
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
			removeAuthority.setIcon(GTSLookAndFeel.getRemoveIcon());
		}
		return removeAuthority;
	}


	private void removeAuthority() {
		try {
			String selectedService = ((GTSServiceListComboBox) getService()).getSelectedService();
			GlobusCredential selectedProxy = ((CredentialComboBox) getProxy()).getSelectedCredential();
			GTSAdminClient client = new GTSAdminClient(selectedService, selectedProxy);
			AuthorityGTS gts = this.getAuthorityTable().getSelectedAuthority();
			client.removeAuthority(gts.getServiceURI());
			getAuthorities();
			GridApplication.getContext().showMessage(
				"Successfully removed the authority " + gts.getServiceURI() + "!!!");
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}


	private void updatePriorities() {
		try {
			disableAllActions();
			String selectedService = ((GTSServiceListComboBox) getService()).getSelectedService();
			GlobusCredential selectedProxy = ((CredentialComboBox) getProxy()).getSelectedCredential();
			GTSAdminClient client = new GTSAdminClient(selectedService, selectedProxy);
			client.updateAuthorityPriorities(getAuthorityTable().getPriorityUpdate());
			GridApplication.getContext().showMessage("Successfully updated the authority priorities!!!");
		} catch (Exception e) {
			ErrorDialog.showError(e);
		} finally {
			enableAllActions();
		}
	}


	private void disableAllActions() {
		getQuery().setEnabled(false);
		getAddAuthority().setEnabled(false);
		getViewModifyButton().setEnabled(false);
		getRemoveAuthority().setEnabled(false);
		getUpdatePriorities().setEnabled(false);
		getIncreasePriority().setEnabled(false);
		getDecreasePriority().setEnabled(false);
	}


	private void enableAllActions() {
		getQuery().setEnabled(true);
		getAddAuthority().setEnabled(true);
		getViewModifyButton().setEnabled(true);
		getRemoveAuthority().setEnabled(true);
		getUpdatePriorities().setEnabled(true);
		getIncreasePriority().setEnabled(true);
		getDecreasePriority().setEnabled(true);
	}


	/**
	 * This method initializes viewModifyButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getViewModifyButton() {
		if (viewModifyButton == null) {
			viewModifyButton = new JButton();
			viewModifyButton.setText("View / Modify Authority");
			viewModifyButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							disableAllActions();
							try {
								getAuthorityTable().doubleClick();
							} catch (Exception ex) {
								ErrorDialog.showError(ex);
							}
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
			viewModifyButton.setIcon(GTSLookAndFeel.getQueryIcon());
		}
		return viewModifyButton;
	}


	/**
	 * This method initializes priorityPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPriorityPanel() {
		if (priorityPanel == null) {
			priorityPanel = new JPanel();
			priorityPanel.add(getIncreasePriority(), null);
			priorityPanel.add(getDecreasePriority(), null);
		}
		return priorityPanel;
	}


	/**
	 * This method initializes increasePriority
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getIncreasePriority() {
		if (increasePriority == null) {
			increasePriority = new JButton();
			increasePriority.setText("Increase Priority");
			increasePriority.setIcon(GTSLookAndFeel.getIncreasePriorityIcon());
			increasePriority.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						getAuthorityTable().increasePriority();
					} catch (Exception ex) {
						ErrorDialog.showError(ex);
					}
				}
			});
		}
		return increasePriority;
	}


	/**
	 * This method initializes decreasePriority
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDecreasePriority() {
		if (decreasePriority == null) {
			decreasePriority = new JButton();
			decreasePriority.setText("Decrease Priority");
			decreasePriority.setIcon(GTSLookAndFeel.getDecresePriorityIcon());
			decreasePriority.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						getAuthorityTable().decreasePriority();
					} catch (Exception ex) {
						ErrorDialog.showError(ex);
					}
				}
			});
		}
		return decreasePriority;
	}


	/**
	 * This method initializes updatePriorities
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getUpdatePriorities() {
		if (updatePriorities == null) {
			updatePriorities = new JButton();
			updatePriorities.setText("Update Priorities");
			updatePriorities.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					disableAllActions();
					Runner runner = new Runner() {
						public void execute() {
							updatePriorities();
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
			updatePriorities.setIcon(GTSLookAndFeel.getRefreshIcon());
		}
		return updatePriorities;
	}


	public void refeshAuthorities() {
		if (searchDone) {
			disableAllActions();
			getAuthorities();
			enableAllActions();
		}
	}

}
