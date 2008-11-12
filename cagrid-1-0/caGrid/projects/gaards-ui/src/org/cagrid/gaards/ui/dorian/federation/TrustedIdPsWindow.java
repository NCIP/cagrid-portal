package org.cagrid.gaards.ui.dorian.federation;

import gov.nih.nci.cagrid.common.Runner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.cagrid.gaards.dorian.client.GridAdministrationClient;
import org.cagrid.gaards.dorian.federation.GridUserPolicy;
import org.cagrid.gaards.dorian.federation.TrustedIdP;
import org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault;
import org.cagrid.gaards.ui.common.TitlePanel;
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
 * @version $Id: TrustedIdPsWindow.java,v 1.3 2008-11-12 19:26:46 langella Exp $
 */
public class TrustedIdPsWindow extends ApplicationComponent {

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel contentPanel = null;

	private JPanel buttonPanel = null;

	private TrustedIdPTable trustedIdPTable = null;

	private JScrollPane jScrollPane = null;

	private JButton viewTrustedIdP = null;

	private SessionPanel session = null;

	private JPanel queryPanel = null;

	private JButton query = null;

	private JPanel progressPanel = null;

	private JProgressBar progress = null;

	private JButton removeTrustedIdPButton = null;

	private JButton addUser = null;

	private JPanel titlePanel = null;

	/**
	 * This is the default constructor
	 */
	public TrustedIdPsWindow() {
		super();
		initialize();
		this.setFrameIcon(DorianLookAndFeel.getTrustedIdPIcon());
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
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.gridy = 0;
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
			gridBagConstraints35.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints35.gridheight = 1;
			gridBagConstraints35.gridwidth = 1;
			gridBagConstraints35.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints35.weighty = 0.0D;
			gridBagConstraints35.ipadx = 0;
			gridBagConstraints35.ipady = 0;
			gridBagConstraints35.gridy = 1;

			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 4;
			gridBagConstraints1.ipadx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 5;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			mainPanel.add(getButtonPanel(), gridBagConstraints2);
			mainPanel.add(getContentPanel(), gridBagConstraints1);
			mainPanel.add(getSession(), gridBagConstraints35);
			mainPanel.add(getQueryPanel(), gridBagConstraints33);
			mainPanel.add(getProgressPanel(), gridBagConstraints32);
			mainPanel.add(getTitlePanel(), gridBagConstraints);
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
									"Trusted IdPs",
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
			buttonPanel.add(getViewTrustedIdP(), null);
			buttonPanel.add(getAddUser(), null);
			buttonPanel.add(getRemoveTrustedIdPButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private TrustedIdPTable getTrustedIdPTable() {
		if (trustedIdPTable == null) {
			trustedIdPTable = new TrustedIdPTable(this);
		}
		return trustedIdPTable;
	}

	public void addTrustedIdP(TrustedIdP idp) {
		getTrustedIdPTable().addTrustedIdP(idp);
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTrustedIdPTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes manageUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getViewTrustedIdP() {
		if (viewTrustedIdP == null) {
			viewTrustedIdP = new JButton();
			viewTrustedIdP.setText("View");
			viewTrustedIdP
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							Runner runner = new Runner() {
								public void execute() {
									showTrustedIdP();
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

		return viewTrustedIdP;
	}

	public void showTrustedIdP() {
		try {
			GridApplication.getContext()
					.addApplicationComponent(
							new TrustedIdPWindow(getSession().getSession(),
									getTrustedIdPTable()
											.getSelectedTrustedIdP(),
									getUserPolicies()), 750, 650);
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}

	public void addTrustedIdP() {
		try {
			GridApplication.getContext().addApplicationComponent(
					new TrustedIdPWindow(getSession().getSession(), this, getUserPolicies()));
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}

	/**
	 * This method initializes session
	 * 
	 * @return javax.swing.JPanel
	 */
	private SessionPanel getSession() {
		if (session == null) {
			session = new SessionPanel(false);
		}
		return session;
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
			query.setText("Search");
			query.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					disableAllButtons();
					Runner runner = new Runner() {
						public void execute() {
							findTrustedIdPs();
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

	private void findTrustedIdPs() {
		this.getTrustedIdPTable().clearTable();
		this.updateProgress(true, "Finding Trusted IdPs...");

		try {
			GridAdministrationClient client = getSession().getAdminClient();
			List<TrustedIdP> idps = client.getTrustedIdPs();

			for (int i = 0; i < idps.size(); i++) {
				this.getTrustedIdPTable().addTrustedIdP(idps.get(i));
			}

			this.updateProgress(false, "Completed [Found " + idps.size()
					+ " IdPs]");

		} catch (PermissionDeniedFault pdf) {
			ErrorDialog.showError(pdf);
			this.updateProgress(false, "Error");
		} catch (Exception e) {
			ErrorDialog.showError(e);
			this.updateProgress(false, "Error");
		}finally{
			enableAllButtons();
		}
	}

	private List<GridUserPolicy> getUserPolicies() throws Exception {
		GridAdministrationClient client = getSession().getAdminClient();
		return client.getUserPolicies();
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
			removeTrustedIdPButton.setText("Remove");
			removeTrustedIdPButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							Runner runner = new Runner() {
								public void execute() {
									removeTrustedIdP();
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
		return removeTrustedIdPButton;
	}

	private void removeTrustedIdP() {
		try {
			GridAdministrationClient client = getSession().getAdminClient();
			client.removeTrustedIdP(getTrustedIdPTable()
					.getSelectedTrustedIdP());
			getTrustedIdPTable().removeSelectedTrustedIdP();
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}

	/**
	 * This method initializes addUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddUser() {
		if (addUser == null) {
			addUser = new JButton();
			addUser.setText("Add");
			addUser.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							addTrustedIdP();
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
		return addUser;
	}

	/**
	 * This method initializes titlePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTitlePanel() {
		if (titlePanel == null) {
			titlePanel = new TitlePanel("Identity Provider(s)","Manage the Identity Provider(s) trusted by the federation.");
		}
		return titlePanel;
	}
	
	private void disableAllButtons(){
		getAddUser().setEnabled(false);
		getViewTrustedIdP().setEnabled(false);
		getRemoveTrustedIdPButton().setEnabled(false);
		getQuery().setEnabled(false);
	}
	
	private void enableAllButtons(){
		getAddUser().setEnabled(true);
		getViewTrustedIdP().setEnabled(true);
		getRemoveTrustedIdPButton().setEnabled(true);
		getQuery().setEnabled(true);
		
	}

}
