package org.cagrid.gaards.ui.dorian.ifs;

import gov.nih.nci.cagrid.common.Runner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.cagrid.gaards.dorian.client.GridUserClient;
import org.cagrid.gaards.dorian.federation.HostCertificateRecord;
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
 * @version $Id: HostCertificatesWindow.java,v 1.1 2007/06/06 19:27:54 langella
 *          Exp $
 */
public class MyHostCertificatesWindow extends ApplicationComponent implements
		HostCertificateLauncher {

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel contentPanel = null;

	private JPanel buttonPanel = null;

	private HostCertificatesTable hostCertificatesTable = null;

	private JScrollPane jScrollPane = null;

	private JButton viewHostCertificate = null;

	private SessionPanel session = null;

	private JPanel queryPanel = null;

	private JButton query = null;

	private boolean isQuerying = false;

	private Object mutex = new Object();

	private JPanel progressPanel = null;

	private JProgressBar progress = null;

	/**
	 * This is the default constructor
	 */
	public MyHostCertificatesWindow() {
		super();
		initialize();
		this.setFrameIcon(DorianLookAndFeel.getTrustedIdPIcon());
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("My Host Certificates");
		this.setFrameIcon(DorianLookAndFeel.getHostsIcon());
		this.setSize(500, 500);

	}

	public void viewHostCertificate(HostCertificateRecord record) {
		try {
			HostCertificateWindow window = new HostCertificateWindow(
					getSession().getServiceURI(), getSession().getCredential(),
					getHostCertificatesTable().getSelectedHostCertificate(),
					false);
			GridApplication.getContext().addApplicationComponent(window, 600,
					500);
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}

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
									"Host Certificates",
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
			buttonPanel.add(getViewHostCertificate(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private HostCertificatesTable getHostCertificatesTable() {
		if (hostCertificatesTable == null) {
			hostCertificatesTable = new HostCertificatesTable(this);
		}
		return hostCertificatesTable;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getHostCertificatesTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes manageUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getViewHostCertificate() {
		if (viewHostCertificate == null) {
			viewHostCertificate = new JButton();
			viewHostCertificate.setText("View Host Certificate");
			viewHostCertificate.setIcon(DorianLookAndFeel.getHostIcon());
			viewHostCertificate
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							Runner runner = new Runner() {
								public void execute() {
									try {
										viewHostCertificate(getHostCertificatesTable()
												.getSelectedHostCertificate());
									} catch (Exception ex) {
										ErrorDialog.showError(ex);
									}
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

		return viewHostCertificate;
	}

	/**
	 * This method initializes session
	 * 
	 * @return javax.swing.JPanel
	 */
	private SessionPanel getSession() {
		if (session == null) {
			session = new SessionPanel();
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
			query.setText("Find Host Certificates");
			query.setIcon(LookAndFeel.getQueryIcon());
			query.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							findHostCertificates();
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

	private void findHostCertificates() {
		this.getQuery().setEnabled(false);
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

		this.getHostCertificatesTable().clearTable();
		this.updateProgress(true, "Finding Host Certificates...");

		try {

			GridUserClient client = getSession().getUserClientWithCredentials();
			List<HostCertificateRecord> certs = client
					.getOwnedHostCertificates();

			for (int i = 0; i < certs.size(); i++) {
				this.getHostCertificatesTable()
						.addHostCertificate(certs.get(i));
			}

			this.updateProgress(false, "Completed [Found " + certs.size()
					+ " host certificates]");

		} catch (Exception e) {
			ErrorDialog.showError(e);
			this.updateProgress(false, "Error");
		} finally {
			this.getQuery().setEnabled(true);
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
}
