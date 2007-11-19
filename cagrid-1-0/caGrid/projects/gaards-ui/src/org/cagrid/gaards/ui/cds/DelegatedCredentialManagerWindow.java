package org.cagrid.gaards.ui.cds;

import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.cagrid.gaards.cds.client.DelegationUserClient;
import org.cagrid.gaards.cds.common.DelegationIdentifier;
import org.cagrid.gaards.cds.common.DelegationRecord;
import org.cagrid.gaards.cds.common.DelegationRecordFilter;
import org.cagrid.gaards.ui.dorian.DorianLookAndFeel;
import org.cagrid.grape.ApplicationComponent;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.ErrorDialog;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: DelegatedCredentialManagerWindow.java,v 1.1 2007-11-19 17:05:26 langella Exp $
 */
public class DelegatedCredentialManagerWindow extends ApplicationComponent {

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel contentPanel = null;

	private JPanel buttonPanel = null;

	private JButton cancel = null;

	private DelegationRecordsTable delegatedCredentialsTable = null;

	private JScrollPane jScrollPane = null;

	private JButton manageDelegatedCredential = null;

	private SessionPanel session = null;

	private JPanel queryPanel = null;

	private JButton query = null;

	private boolean isQuerying = false;

	private Object mutex = new Object();

	private JPanel progressPanel = null;

	private JProgressBar progress = null;

	private JPanel filterPanel = null;

	private JLabel gridLabel = null;

	private JTextField gridIdentity = null;
	
	private boolean isAdmin;

	private JLabel jLabel = null;

	private JTextField delegationId = null;

	private JLabel jLabel1 = null;

	private ExpirationStatusComboBox expirationStatus = null;

	private JLabel jLabel2 = null;

	private DelegationStatusComboBox delegationStatus = null;

	/**
	 * This is the default constructor
	 */
	public DelegatedCredentialManagerWindow(boolean isAdmin) {
		super();
		this.isAdmin = isAdmin;
		initialize();
		this.setFrameIcon(DorianLookAndFeel.getUsersIcon());
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		if (isAdmin) {
			this.setTitle("Delegated Credential Manager");
		} else {
			this.setTitle("My Delegated Credentials");
		}
		setSize(600, 600);
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
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints.gridy = 1;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.gridx = 0;
			gridBagConstraints32.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints32.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints32.weightx = 1.0D;
			gridBagConstraints32.gridy = 4;
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.gridx = 0;
			gridBagConstraints33.gridy = 3;
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
			gridBagConstraints1.gridy = 5;
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
			mainPanel.add(getSession(), gridBagConstraints35);
			mainPanel.add(getQueryPanel(), gridBagConstraints33);
			mainPanel.add(getProgressPanel(), gridBagConstraints32);
			mainPanel.add(getFilterPanel(), gridBagConstraints);
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
									"Delegated Credential(s)",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, LookAndFeel.getPanelLabelColor()));
			gridBagConstraints4.weightx = 1.0;
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
			buttonPanel.add(getManageDelegatedCredential(), null);
			buttonPanel.add(getCancel(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancel() {
		if (cancel == null) {
			cancel = new JButton();
			cancel.setText("Close");
			cancel.setIcon(LookAndFeel.getCloseIcon());
			cancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancel;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private DelegationRecordsTable getDelegatedCredentialsTable() {
		if (delegatedCredentialsTable == null) {
			delegatedCredentialsTable = new DelegationRecordsTable();
		}
		return delegatedCredentialsTable;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getDelegatedCredentialsTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes manageDelegatedCredential
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getManageDelegatedCredential() {
		if (manageDelegatedCredential == null) {
			manageDelegatedCredential = new JButton();
			manageDelegatedCredential.setText("View Record");
			manageDelegatedCredential.setIcon(DorianLookAndFeel.getUserIcon());
			manageDelegatedCredential
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							getDelegatedCredentialsTable().doubleClick();
						}

					});
		}

		return manageDelegatedCredential;
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
			query.setText("Find Delegated Credentials");
			query.setIcon(LookAndFeel.getQueryIcon());
			query.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							findDelegatedCredentials();
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

	private void findDelegatedCredentials() {

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

		this.getDelegatedCredentialsTable().clearTable();
		this.updateProgress(true, "Querying...");

		try {
			DelegationRecordFilter f = new DelegationRecordFilter();

			String idStr = Utils.clean(this.getDelegationId().getText());
			if (idStr != null) {
				try {
					DelegationIdentifier id = new DelegationIdentifier();
					id.setDelegationId(Integer.valueOf(idStr).intValue());
					f.setDelegationIdentifier(id);
				} catch (Exception e) {
					ErrorDialog
							.showError("A Delegation Identifier must be an integer.");
					return;
				}
			}

			if (isAdmin) {
				f.setGridIdentity(Utils.clean(getGridIdentity().getText()));

			} else {
				f.setGridIdentity(getSession().getCredential().getIdentity());
			}

			f.setDelegationStatus(getDelegationStatus().getDelegationStatus());
			f.setExpirationStatus(getExpirationStatus().getExpirationStatus());

			List<DelegationRecord> records;

			if (isAdmin) {
				// TODO: ADD ADMIN CALL
				records = new ArrayList<DelegationRecord>();
			} else {
				DelegationUserClient client = new DelegationUserClient(
						getSession().getServiceURI(), getSession()
								.getCredential());
				records = client.findMyDelegatedCredentials(f);
			}

			for (int i = 0; i < records.size(); i++) {
				this.getDelegatedCredentialsTable().addRecord(records.get(i));
			}

			this.updateProgress(false, "Querying Completed [" + records.size()
					+ " records found]");

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
	 * This method initializes filterPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getFilterPanel() {
		if (filterPanel == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridy = 3;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints10.gridy = 3;
			jLabel2 = new JLabel();
			jLabel2.setText("Delegation Status");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = 2;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.gridy = 2;
			jLabel1 = new JLabel();
			jLabel1.setText("Expiration Status");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints5.weightx = 1.0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints3.gridy = 1;
			jLabel = new JLabel();
			jLabel.setText("Delegation Identifier");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 7;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.gridy = 0;
			gridLabel = new JLabel();
			gridLabel.setText("Grid Identity");
			if (isAdmin) {
				gridLabel.setVisible(true);
				gridLabel.setEnabled(true);
			} else {
				gridLabel.setVisible(false);
				gridLabel.setEnabled(false);
			}
			filterPanel = new JPanel();
			filterPanel.setLayout(new GridBagLayout());
			filterPanel.setBorder(BorderFactory.createTitledBorder(null,
					"Search Criteria", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, LookAndFeel
							.getPanelLabelColor()));
			filterPanel.add(gridLabel, gridBagConstraints6);
			filterPanel.add(getGridIdentity(), gridBagConstraints7);
			filterPanel.add(jLabel, gridBagConstraints3);
			filterPanel.add(getDelegationId(), gridBagConstraints5);
			filterPanel.add(jLabel1, gridBagConstraints8);
			filterPanel.add(getExpirationStatus(), gridBagConstraints9);
			filterPanel.add(jLabel2, gridBagConstraints10);
			filterPanel.add(getDelegationStatus(), gridBagConstraints12);
		}
		return filterPanel;
	}

	/**
	 * This method initializes gridIdentity
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getGridIdentity() {
		if (gridIdentity == null) {
			gridIdentity = new JTextField();
			if (isAdmin) {
				gridIdentity.setVisible(true);
				gridIdentity.setEnabled(true);
			} else {
				gridIdentity.setVisible(false);
				gridIdentity.setEnabled(false);
			}
		}
		return gridIdentity;
	}

	public class TrustedIdPCaddy {
		private TrustedIdP trustedIdp;

		public TrustedIdPCaddy(TrustedIdP idp) {
			this.trustedIdp = idp;
		}

		public TrustedIdP getTrustedIdP() {
			return trustedIdp;
		}

		public String toString() {
			return "[IdP Id: " + trustedIdp.getId() + "] "
					+ trustedIdp.getName();
		}

	}

	/**
	 * This method initializes delegationId
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDelegationId() {
		if (delegationId == null) {
			delegationId = new JTextField();
		}
		return delegationId;
	}

	/**
	 * This method initializes expirationStatus
	 * 
	 * @return javax.swing.JComboBox
	 */
	private ExpirationStatusComboBox getExpirationStatus() {
		if (expirationStatus == null) {
			expirationStatus = new ExpirationStatusComboBox(true);
		}
		return expirationStatus;
	}

	/**
	 * This method initializes delegationStatus
	 * 
	 * @return javax.swing.JComboBox
	 */
	private DelegationStatusComboBox getDelegationStatus() {
		if (delegationStatus == null) {
			delegationStatus = new DelegationStatusComboBox(true);
		}
		return delegationStatus;
	}

}
