package org.cagrid.gaards.ui.cds;

import gov.nih.nci.cagrid.common.Runner;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.cagrid.gaards.cds.client.DelegationAdminClient;
import org.cagrid.gaards.cds.common.DelegationRecord;
import org.cagrid.gaards.ui.common.ProxyComboBox;
import org.cagrid.grape.ApplicationComponent;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.ErrorDialog;
import org.globus.gsi.GlobusCredential;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: DelegatedCredentialWindow.java,v 1.2 2007/11/27 20:09:50
 *          langella Exp $
 */
public class DelegatedCredentialWindow extends ApplicationComponent {

	private final static String INFO_PANEL = "General Information";

	private final static String POLICY_PANEL = "Delegation Policy";

	private final static String CERTIFICATE_PANEL = "Certificate Chain";

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel buttonPanel = null;

	private JButton cancel = null;

	private JButton updateStatus = null;

	private JTabbedPane jTabbedPane = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JLabel jLabel14 = null;

	private String serviceId;

	private JTextField service = null;

	private JPanel infoPanel = null;

	private GlobusCredential cred;

	private JLabel credentialLabel = null;

	private ProxyComboBox proxy = null;

	private JLabel gridIdLabel = null;

	private JTextField gridIdentity = null;

	private JPanel certificatePanel = null;

	private DelegationRecord record;

	private JLabel jLabel = null;

	private JTextField delegationIdentifier = null;

	private JLabel jLabel1 = null;

	private JTextField initiated = null;

	private JLabel jLabel2 = null;

	private JTextField approved = null;

	private JLabel jLabel3 = null;

	private JTextField expires = null;

	private JLabel jLabel4 = null;

	private JTextField lifetime = null;

	private JLabel jLabel5 = null;

	private DelegationStatusComboBox status = null;

	private JScrollPane jScrollPane = null;

	private CertificateChainTable certificateChainTable = null;

	private JButton viewCertificate = null;

	private JPanel policyPanel = null;

	private JLabel jLabel6 = null;

	private JTextField issuedCredentialPathLength = null;

	private JPanel policyTypePanel = null;

	private JLabel jLabel7 = null;

	private JTextField delegationPolicyType = null;

	private DelegationPolicyPanel delegationPolicyPanel;

	private JPanel infoButtonPanel = null;

	/**
	 * This is the default constructor
	 */
	public DelegatedCredentialWindow(String serviceId, GlobusCredential proxy,
			DelegationRecord record) throws Exception {
		super();
		this.serviceId = serviceId;
		this.cred = proxy;
		this.record = record;
		initialize();
		this.setFrameIcon(CDSLookAndFeel.getDelegateCredentialsIcon());
		setDelegationRecord();
	}

	private void setDelegationRecord() throws Exception {
		getGridIdentity().setText(record.getGridIdentity());
		getDelegationIdentifier().setText(
				String.valueOf(record.getDelegationIdentifier()
						.getDelegationId()));
		getInitiated().setText(getDateString(record.getDateInitiated()));
		getApproved().setText(getDateString(record.getDateApproved()));
		getExpires().setText(getDateString(record.getExpiration()));
		String str = record.getIssuedCredentialLifetime().getHours()
				+ " hour(s), "
				+ record.getIssuedCredentialLifetime().getMinutes()
				+ " minute(s) "
				+ record.getIssuedCredentialLifetime().getSeconds()
				+ " second(s)";
		getLifetime().setText(str);
		getIssuedCredentialPathLength().setText(
				String.valueOf(record.getIssuedCredentialPathLength()));
		getStatus().setSelectedItem(record.getDelegationStatus());
		getCertificateChainTable().setCertificateChain(
				record.getCertificateChain());
		String policyType = CDSUIUtils.getDelegationPolicyType(record
				.getDelegationPolicy());
		getDelegationPolicyType().setText(policyType);

		GridBagConstraints policyPanelConstraints = new GridBagConstraints();
		policyPanelConstraints.gridx = 0;
		policyPanelConstraints.fill = GridBagConstraints.BOTH;
		policyPanelConstraints.insets = new Insets(5, 5, 5, 5);
		policyPanelConstraints.weightx = 1.0D;
		policyPanelConstraints.weighty = 1.0D;
		policyPanelConstraints.gridy = 1;
		if (this.delegationPolicyPanel != null) {
			getPolicyPanel().remove(delegationPolicyPanel);
		}
		this.delegationPolicyPanel = CDSUIUtils.getPolicyPanel(policyType,
				false);
		getPolicyPanel()
				.add(this.delegationPolicyPanel, policyPanelConstraints);
		this.delegationPolicyPanel.setPolicy(record.getDelegationPolicy());
	}

	private String getDateString(long l) {
		if (l <= 0) {
			return "";
		} else {
			Calendar c = new GregorianCalendar();
			c.setTimeInMillis(l);
			return c.getTime().toString();
		}
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("Delegate Credential [" + record.getGridIdentity() + "]");
		this.setSize(600, 400);

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
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.weighty = 1.0D;
			gridBagConstraints4.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			mainPanel.add(getButtonPanel(), gridBagConstraints2);
			mainPanel.add(getJPanel2(), gridBagConstraints1);
			mainPanel.add(getJTabbedPane(), gridBagConstraints4);
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
	 * This method initializes manageUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getUpdateStatus() {
		if (updateStatus == null) {
			updateStatus = new JButton();
			updateStatus.setText("Update Status");
			updateStatus.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							updateDelegationStatus();
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
			updateStatus.setIcon(CDSLookAndFeel.getDelegateCredentialsIcon());
		}
		return updateStatus;
	}

	private synchronized void updateDelegationStatus() {
		try {
			DelegationAdminClient client = new DelegationAdminClient(
					getService().getText(), getProxy().getSelectedProxy());
			client.updateDelegationStatus(record.getDelegationIdentifier(),
					getStatus().getDelegationStatus());
			GridApplication.getContext().showMessage("The status was succesfully updated.");
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}

	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setBorder(BorderFactory.createTitledBorder(null,
					"Delegated Credential", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, LookAndFeel
							.getPanelLabelColor()));
			jTabbedPane.addTab(INFO_PANEL, CDSLookAndFeel
					.getDelegateCredentialsIcon(), getInfoPanel(), null);
			jTabbedPane.addTab(POLICY_PANEL, CDSLookAndFeel
					.getDelegationPolicyIcon(), getPolicyPanel(), null);
			jTabbedPane.addTab(CERTIFICATE_PANEL, CDSLookAndFeel
					.getCertificateIcon(), getCertificatePanel(), null);

		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints23.gridy = 6;
			gridBagConstraints23.weightx = 1.0;
			gridBagConstraints23.anchor = GridBagConstraints.WEST;
			gridBagConstraints23.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints23.gridx = 1;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 0;
			gridBagConstraints22.anchor = GridBagConstraints.WEST;
			gridBagConstraints22.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints22.gridy = 6;
			jLabel6 = new JLabel();
			jLabel6.setText("Issued Credential Path Length");
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints19.gridy = 7;
			gridBagConstraints19.weightx = 1.0;
			gridBagConstraints19.anchor = GridBagConstraints.WEST;
			gridBagConstraints19.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints19.gridx = 1;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints18.gridy = 7;
			jLabel5 = new JLabel();
			jLabel5.setText("Delegation Status");
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.gridy = 5;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints17.gridx = 1;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.gridy = 5;
			jLabel4 = new JLabel();
			jLabel4.setText("Issued Credential Lifetime");
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.gridy = 4;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints14.gridx = 1;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridy = 4;
			jLabel3 = new JLabel();
			jLabel3.setText("Expires On");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridy = 3;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints11.gridy = 3;
			jLabel2 = new JLabel();
			jLabel2.setText("Approved On");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints9.gridy = 2;
			jLabel1 = new JLabel();
			jLabel1.setText("Initiated On");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints7.gridy = 1;
			jLabel = new JLabel();
			jLabel.setText("Delegation Identifier");
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.gridy = 7;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints16.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints16.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridx = 0;
			gridIdLabel = new JLabel();
			gridIdLabel.setText("Grid Identity");
			jPanel1 = new JPanel();
			jPanel1.setName(INFO_PANEL);
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(gridIdLabel, gridBagConstraints5);
			jPanel1.add(getGridIdentity(), gridBagConstraints6);
			jPanel1.add(jLabel, gridBagConstraints7);
			jPanel1.add(getDelegationIdentifier(), gridBagConstraints8);
			jPanel1.add(jLabel1, gridBagConstraints9);
			jPanel1.add(getInitiated(), gridBagConstraints10);
			jPanel1.add(jLabel2, gridBagConstraints11);
			jPanel1.add(getApproved(), gridBagConstraints12);
			jPanel1.add(jLabel3, gridBagConstraints13);
			jPanel1.add(getExpires(), gridBagConstraints14);
			jPanel1.add(jLabel4, gridBagConstraints15);
			jPanel1.add(getLifetime(), gridBagConstraints17);
			jPanel1.add(jLabel5, gridBagConstraints18);
			jPanel1.add(getStatus(), gridBagConstraints19);
			jPanel1.add(jLabel6, gridBagConstraints22);
			jPanel1.add(getIssuedCredentialPathLength(), gridBagConstraints23);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints.gridy = 1;
			credentialLabel = new JLabel();
			credentialLabel.setText("Credential");
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints27.weightx = 1.0;
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
			jPanel2.add(getService(), gridBagConstraints27);
			jPanel2.add(credentialLabel, gridBagConstraints);
			jPanel2.add(getProxy(), gridBagConstraints3);
		}
		return jPanel2;
	}

	/**
	 * This method initializes service1
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getService() {
		if (service == null) {
			service = new JTextField();
			service.setText(serviceId);
			service.setEditable(false);
		}
		return service;
	}

	/**
	 * This method initializes infoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInfoPanel() {
		if (infoPanel == null) {
			infoPanel = new JPanel();
			infoPanel.setLayout(new BorderLayout());
			infoPanel.add(getJPanel1(), java.awt.BorderLayout.NORTH);
			infoPanel.add(getInfoButtonPanel(), BorderLayout.SOUTH);
		}
		return infoPanel;
	}

	/**
	 * This method initializes proxy1
	 * 
	 * @return javax.swing.JComboBox
	 */
	private ProxyComboBox getProxy() {
		if (proxy == null) {
			proxy = new ProxyComboBox(cred);
		}
		return proxy;
	}

	/**
	 * This method initializes gridIdentity
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getGridIdentity() {
		if (gridIdentity == null) {
			gridIdentity = new JTextField();
			gridIdentity.setEditable(false);
		}
		return gridIdentity;
	}

	/**
	 * This method initializes certificatePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCertificatePanel() {
		if (certificatePanel == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints21.gridy = 1;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.fill = GridBagConstraints.BOTH;
			gridBagConstraints20.weighty = 1.0;
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.gridy = 0;
			gridBagConstraints20.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints20.weightx = 1.0;
			certificatePanel = new JPanel();
			certificatePanel.setLayout(new GridBagLayout());
			certificatePanel.add(getJScrollPane(), gridBagConstraints20);
			certificatePanel.setBorder(BorderFactory.createTitledBorder(null,
					"Certificate Chain", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, LookAndFeel
							.getPanelLabelColor()));
			certificatePanel.add(getViewCertificate(), gridBagConstraints21);
		}
		return certificatePanel;
	}

	/**
	 * This method initializes delegationIdentifier
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDelegationIdentifier() {
		if (delegationIdentifier == null) {
			delegationIdentifier = new JTextField();
			delegationIdentifier.setEditable(false);
		}
		return delegationIdentifier;
	}

	/**
	 * This method initializes initiated
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getInitiated() {
		if (initiated == null) {
			initiated = new JTextField();
			initiated.setEditable(false);
		}
		return initiated;
	}

	/**
	 * This method initializes approved
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getApproved() {
		if (approved == null) {
			approved = new JTextField();
			approved.setEditable(false);
		}
		return approved;
	}

	/**
	 * This method initializes expires
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getExpires() {
		if (expires == null) {
			expires = new JTextField();
			expires.setEditable(false);
		}
		return expires;
	}

	/**
	 * This method initializes lifetime
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getLifetime() {
		if (lifetime == null) {
			lifetime = new JTextField();
			lifetime.setEditable(false);
		}
		return lifetime;
	}

	/**
	 * This method initializes status
	 * 
	 * @return javax.swing.JComboBox
	 */
	private DelegationStatusComboBox getStatus() {
		if (status == null) {
			status = new DelegationStatusComboBox(false);
		}
		return status;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getCertificateChainTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes certificateChainTable
	 * 
	 * @return javax.swing.JTable
	 */
	private CertificateChainTable getCertificateChainTable() {
		if (certificateChainTable == null) {
			certificateChainTable = new CertificateChainTable();
		}
		return certificateChainTable;
	}

	/**
	 * This method initializes viewCertificate
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getViewCertificate() {
		if (viewCertificate == null) {
			viewCertificate = new JButton();
			viewCertificate.setText("View Certificate");
			viewCertificate.setIcon(CDSLookAndFeel.getCertificateIcon());
			viewCertificate
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							getCertificateChainTable().doubleClick();
						}
					});
		}
		return viewCertificate;
	}

	/**
	 * This method initializes policyPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPolicyPanel() {
		if (policyPanel == null) {
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints24.gridy = 0;
			gridBagConstraints24.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints24.anchor = GridBagConstraints.NORTH;
			gridBagConstraints24.weightx = 1.0D;
			gridBagConstraints24.gridx = 0;
			policyPanel = new JPanel();
			policyPanel.setLayout(new GridBagLayout());
			policyPanel.add(getPolicyTypePanel(), gridBagConstraints24);
		}
		return policyPanel;
	}

	/**
	 * This method initializes issuedCredentialPathLength
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getIssuedCredentialPathLength() {
		if (issuedCredentialPathLength == null) {
			issuedCredentialPathLength = new JTextField();
			issuedCredentialPathLength.setEditable(false);
		}
		return issuedCredentialPathLength;
	}

	/**
	 * This method initializes policyTypePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPolicyTypePanel() {
		if (policyTypePanel == null) {
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.gridx = 0;
			gridBagConstraints26.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints26.gridy = 0;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints25.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints25.gridx = 1;
			gridBagConstraints25.gridy = 0;
			gridBagConstraints25.weightx = 1.0;
			jLabel7 = new JLabel();
			jLabel7.setText("Delegation Policy");
			jLabel7.setFont(new Font("Dialog", Font.BOLD, 14));
			policyTypePanel = new JPanel();
			policyTypePanel.setLayout(new GridBagLayout());
			policyTypePanel.add(jLabel7, gridBagConstraints26);
			policyTypePanel
					.add(getDelegationPolicyType(), gridBagConstraints25);
		}
		return policyTypePanel;
	}

	/**
	 * This method initializes delegationPolicyType
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDelegationPolicyType() {
		if (delegationPolicyType == null) {
			delegationPolicyType = new JTextField();
			delegationPolicyType.setEditable(false);
		}
		return delegationPolicyType;
	}

	/**
	 * This method initializes infoButtonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInfoButtonPanel() {
		if (infoButtonPanel == null) {
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.gridx = 0;
			gridBagConstraints29.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints29.gridy = 0;
			infoButtonPanel = new JPanel();
			infoButtonPanel.setLayout(new GridBagLayout());
			infoButtonPanel.add(getUpdateStatus(), gridBagConstraints29);
		}
		return infoButtonPanel;
	}

}
