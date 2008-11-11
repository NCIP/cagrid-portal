package org.cagrid.gaards.ui.dorian.federation;

import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.common.Utils;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.cagrid.gaards.dorian.client.GridAdministrationClient;
import org.cagrid.gaards.dorian.federation.HostCertificateRecord;
import org.cagrid.gaards.dorian.federation.HostCertificateStatus;
import org.cagrid.gaards.dorian.federation.HostCertificateUpdate;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.pki.KeyUtil;
import org.cagrid.gaards.ui.common.CertificatePanel;
import org.cagrid.gaards.ui.common.GAARDSLookAndFeel;
import org.cagrid.gaards.ui.dorian.DorianLookAndFeel;
import org.cagrid.gaards.ui.dorian.DorianSession;
import org.cagrid.gaards.ui.dorian.DorianSessionProvider;
import org.cagrid.grape.ApplicationComponent;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.utils.ErrorDialog;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: HostCertificateWindow.java,v 1.1 2007/06/06 20:55:45 langella
 *          Exp $
 */
public class HostCertificateWindow extends ApplicationComponent implements DorianSessionProvider{

	private final static String INFO_PANEL = "Summary";

	private final static String CERTIFICATE_PANEL = "Certificate";
	
	private final static String AUDIT_PANEL = "Audit";

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel buttonPanel = null;

	private JTabbedPane jTabbedPane = null;

	private JPanel jPanel1 = null;

	private JPanel infoPanel = null;

	private JPanel certificatePanel = null;

	private CertificatePanel credPanel = null;

	private HostCertificateRecord record;

	private JLabel jLabel = null;

	private JTextField recordId = null;

	private boolean admin;

	private JLabel jLabel1 = null;

	private JTextField host = null;

	private JLabel jLabel2 = null;

	private JPanel ownerPanel = null;

	private JTextField owner = null;

	private JButton findUser = null;

	private JLabel jLabel3 = null;

	private HostCertificateStatusComboBox status = null;

	private JLabel jLabel6 = null;

	private JTextField strength = null;

	private JButton approve = null;

	private JButton renew = null;

	private JButton save = null;

	private JButton update = null;

	private JLabel jLabel4 = null;

	private JTextField hostGridIdentity = null;

	private DorianSession session;

	private JPanel titlePanel = null;

	private JLabel logo = null;

	private JLabel hostname = null;

	private JLabel hostIdentity = null;

	private JPanel auditPanel = null;

	/**
	 * This is the default constructor
	 */
	public HostCertificateWindow(DorianSession session,
			HostCertificateRecord record, boolean admin) throws Exception {
		super();
		this.session = session;
		this.record = record;
		this.admin = admin;
		initialize();
		this.setFrameIcon(DorianLookAndFeel.getHostIcon());
		loadRecord();
	}

	private void loadRecord() {
		getRecordId().setText(String.valueOf(record.getId()));
		getHost().setText(record.getHost());
		getOwner().setText(record.getOwner());
		getStatus().setSelectedItem(record.getStatus());
		if (record.getStatus().equals(HostCertificateStatus.Compromised)) {
			getStatus().setEnabled(false);
			getOwner().setEditable(false);
			getFindUser().setVisible(false);
			getFindUser().setEnabled(false);
		}
		try {
			PublicKey key = KeyUtil.loadPublicKey(record.getPublicKey()
					.getKeyAsString());
			strength.setText(String.valueOf(((RSAPublicKey) key).getModulus()
					.bitLength()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if ((record.getCertificate() != null)
					&& (Utils.clean(record.getCertificate()
							.getCertificateAsString()) != null)) {
				X509Certificate cert = CertUtil.loadCertificate(record
						.getCertificate().getCertificateAsString());
				getCredPanel().setCertificate(cert);
				hostGridIdentity.setText(CertUtil.subjectToIdentity(cert
						.getSubjectDN().getName()));
				getSave().setEnabled(true);
				getSave().setVisible(true);
			} else {
				getSave().setEnabled(false);
				getSave().setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (record.getStatus().equals(HostCertificateStatus.Pending) && admin) {
			getApprove().setEnabled(true);
			getApprove().setVisible(true);
		} else {
			getApprove().setEnabled(false);
			getApprove().setVisible(false);
		}

		if (record.getStatus().equals(HostCertificateStatus.Active) && admin) {
			getRenew().setEnabled(true);
			getRenew().setVisible(true);
		} else {
			getRenew().setEnabled(false);
			getRenew().setVisible(false);
		}
		detectUpdate();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("Host Certificate [" + record.getHost() + "]");
	}
	
	

	public DorianSession getSession() throws Exception {
		return this.session;
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
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.weighty = 1.0D;
			gridBagConstraints4.gridx = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			mainPanel.add(getButtonPanel(), gridBagConstraints2);
			mainPanel.add(getJTabbedPane(), gridBagConstraints4);
			mainPanel.add(getTitlePanel(), gridBagConstraints);
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
			buttonPanel.add(getApprove(), null);
			buttonPanel.add(getUpdate(), null);
			buttonPanel.add(getRenew(), null);
			buttonPanel.add(getSave(), null);
		}
		return buttonPanel;
	}

	private void approveHostCertificate() {
		try {
			getApprove().setEnabled(false);
			GridAdministrationClient client = this.session.getAdminClient();
			record = client.approveHostCertificate(record.getId());
			loadRecord();
			JOptionPane.showMessageDialog(this,
					"The host certificate has been succesfully approved.");
		} catch (Exception e) {
			ErrorDialog.showError(e);
			getApprove().setEnabled(true);
		}

	}

	private void renewHostCertificate() {
		try {
			getRenew().setEnabled(false);
			GridAdministrationClient client = this.session.getAdminClient();
			record = client.renewHostCertificate(record.getId());
			loadRecord();
			JOptionPane.showMessageDialog(this,
					"The host certificate has been succesfully renewed.");
		} catch (Exception e) {
			ErrorDialog.showError(e);
			getRenew().setEnabled(true);
		}

	}

	private void detectUpdate() {
		if (admin) {
			if (!record.getStatus().equals(getStatus().getSelectedItem())) {
				getUpdate().setEnabled(true);
			} else if (!record.getOwner().equals(getOwner().getText())) {
				getUpdate().setEnabled(true);
			} else {
				getUpdate().setEnabled(false);
			}
		}
	}

	private void updateHostCertificate() {
		try {
			getUpdate().setEnabled(false);

			boolean performUpdate = false;
			HostCertificateUpdate certUpdate = new HostCertificateUpdate();
			certUpdate.setId(record.getId());
			if (!record.getStatus().equals(getStatus().getSelectedItem())) {
				certUpdate.setStatus((HostCertificateStatus) getStatus()
						.getSelectedItem());
				performUpdate = true;
			}

			if (!record.getOwner().equals(getOwner().getText())) {
				certUpdate.setOwner(getOwner().getText());
				performUpdate = true;
			}

			if (performUpdate) {
				GridAdministrationClient client = this.session.getAdminClient();
				client.updateHostCertificateRecord(certUpdate);
				if (!record.getStatus().equals(getStatus().getSelectedItem())) {
					record.setStatus((HostCertificateStatus) getStatus()
							.getSelectedItem());
				}

				if (!record.getOwner().equals(getOwner().getText())) {
					record.setOwner(getOwner().getText());
				}
				loadRecord();
				JOptionPane.showMessageDialog(this,
						"The host certificate has been succesfully updated.");
			} else {
				JOptionPane.showMessageDialog(this,
						"There are no changes to update.");
			}

		} catch (Exception e) {
			ErrorDialog.showError(e);
			getUpdate().setEnabled(true);
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
			jTabbedPane.addTab(INFO_PANEL, null, getInfoPanel(), null);
			jTabbedPane.addTab(CERTIFICATE_PANEL, null, getCertificatePanel(),
					null);
			jTabbedPane.addTab(AUDIT_PANEL, null, getAuditPanel(), null);
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
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints18.gridy = 5;
			gridBagConstraints18.weightx = 1.0;
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints18.gridx = 1;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints15.gridy = 5;
			jLabel4 = new JLabel();
			jLabel4.setText("Host Grid Identity");
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints22.gridy = 4;
			gridBagConstraints22.weightx = 1.0;
			gridBagConstraints22.anchor = GridBagConstraints.WEST;
			gridBagConstraints22.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints22.gridx = 1;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints21.gridy = 4;
			jLabel6 = new JLabel();
			jLabel6.setText("Strength");
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.gridy = 3;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints14.gridx = 1;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints13.gridy = 3;
			jLabel3 = new JLabel();
			jLabel3.setText("Status");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints10.weightx = 1.0D;
			gridBagConstraints10.gridy = 2;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 2;
			gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
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
			jLabel1 = new JLabel();
			jLabel1.setText("Host");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints5.weightx = 1.0;
			jLabel = new JLabel();
			jLabel.setText("Record Id");
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.gridy = 7;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints16.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints16.gridx = 1;
			jPanel1 = new JPanel();
			jPanel1.setName(INFO_PANEL);
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(jLabel, gridBagConstraints6);
			jPanel1.add(getRecordId(), gridBagConstraints5);
			jPanel1.add(jLabel1, gridBagConstraints7);
			jPanel1.add(getHost(), gridBagConstraints8);
			jPanel1.add(jLabel2, gridBagConstraints9);
			jPanel1.add(getOwnerPanel(), gridBagConstraints10);
			jPanel1.add(jLabel3, gridBagConstraints13);
			jPanel1.add(getStatus(), gridBagConstraints14);
			jPanel1.add(jLabel6, gridBagConstraints21);
			jPanel1.add(getStrength(), gridBagConstraints22);
			jPanel1.add(jLabel4, gridBagConstraints15);
			jPanel1.add(getHostGridIdentity(), gridBagConstraints18);
		}
		return jPanel1;
	}

	/**
	 * This method initializes infoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInfoPanel() {
		if (infoPanel == null) {
			jLabel2 = new JLabel();
			jLabel2.setText("Owner");
			infoPanel = new JPanel();
			infoPanel.setLayout(new BorderLayout());
			infoPanel.add(getJPanel1(), java.awt.BorderLayout.NORTH);
		}
		return infoPanel;
	}

	/**
	 * This method initializes certificatePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCertificatePanel() {
		if (certificatePanel == null) {
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints17.gridy = 0;
			gridBagConstraints17.weightx = 1.0D;
			gridBagConstraints17.weighty = 1.0D;
			gridBagConstraints17.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints17.gridx = 0;
			certificatePanel = new JPanel();
			certificatePanel.setLayout(new GridBagLayout());
			certificatePanel.add(getCredPanel(), gridBagConstraints17);
		}
		return certificatePanel;
	}

	/**
	 * This method initializes credPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private CertificatePanel getCredPanel() {
		if (credPanel == null) {
			try {
				credPanel = new CertificatePanel();
				credPanel.setAllowImport(false);
				credPanel.setAllowExport(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return credPanel;
	}

	/**
	 * This method initializes recordId
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getRecordId() {
		if (recordId == null) {
			recordId = new JTextField();
			recordId.setEditable(false);
		}
		return recordId;
	}

	/**
	 * This method initializes host
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getHost() {
		if (host == null) {
			host = new JTextField();
			host.setEditable(false);
		}
		return host;
	}

	/**
	 * This method initializes ownerPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getOwnerPanel() {
		if (ownerPanel == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.gridy = 0;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.weightx = 1.0;
			ownerPanel = new JPanel();
			ownerPanel.setLayout(new GridBagLayout());
			ownerPanel.add(getOwner(), gridBagConstraints11);
			ownerPanel.add(getFindUser(), gridBagConstraints12);
		}
		return ownerPanel;
	}

	/**
	 * This method initializes owner
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getOwner() {
		if (owner == null) {
			owner = new JTextField();
			owner.addCaretListener(new javax.swing.event.CaretListener() {
				public void caretUpdate(javax.swing.event.CaretEvent e) {
					detectUpdate();
				}
			});
			if (!admin) {
				owner.setEditable(false);

			}
		}
		return owner;
	}

	/**
	 * This method initializes findUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getFindUser() {
		if (findUser == null) {
			findUser = new JButton();
			findUser.setText("Find...");
			if (!admin) {
				findUser.setVisible(false);
			} else {
				findUser.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						FindUserDialog dialog = new FindUserDialog();
						dialog.setModal(true);
						GridApplication.getContext().showDialog(dialog);
						if (dialog.getSelectedUser() != null) {
							owner.setText(dialog.getSelectedUser());
						}
					}
				});
			}
		}
		return findUser;
	}

	/**
	 * This method initializes status
	 * 
	 * @return gov.nih.nci.cagrid.dorian.ui.ifs.HostCertificateStatusComboBox
	 */
	private HostCertificateStatusComboBox getStatus() {
		if (status == null) {
			status = new HostCertificateStatusComboBox(false);
			status.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					detectUpdate();
				}
			});
			if (!admin) {
				status.setEnabled(false);

			}
		}
		return status;
	}

	/**
	 * This method initializes strength
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getStrength() {
		if (strength == null) {
			strength = new JTextField();
			strength.setEditable(false);
		}
		return strength;
	}

	/**
	 * This method initializes approve
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getApprove() {
		if (approve == null) {
			approve = new JButton();
			approve.setText("Approve Certificate");
			approve.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							approveHostCertificate();
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
		return approve;
	}

	/**
	 * This method initializes renew
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRenew() {
		if (renew == null) {
			renew = new JButton();
			renew.setText("Renew Certificate");
			renew.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							renewHostCertificate();
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
		return renew;
	}

	/**
	 * This method initializes save
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSave() {
		if (save == null) {
			save = new JButton();
			save.setText("Save Certificate");
			save.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					exportCertificate();
				}
			});
		}
		return save;
	}

	private void exportCertificate() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				X509Certificate cert = CertUtil.loadCertificate(record
						.getCertificate().getCertificateAsString());
				CertUtil.writeCertificate(cert, new File(fc.getSelectedFile()
						.getAbsolutePath()));
			} catch (Exception ex) {
				ErrorDialog.showError(ex);
			}
		}

	}

	/**
	 * This method initializes update
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getUpdate() {
		if (update == null) {
			update = new JButton();
			update.setText("Update Certificate");
			update.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							updateHostCertificate();
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
			if (!admin) {
				update.setEnabled(false);
				update.setVisible(false);

			}
		}
		return update;
	}

	/**
	 * This method initializes hostGridIdentity
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getHostGridIdentity() {
		if (hostGridIdentity == null) {
			hostGridIdentity = new JTextField();
			hostGridIdentity.setEnabled(true);
			hostGridIdentity.setEditable(false);
		}
		return hostGridIdentity;
	}

	/**
	 * This method initializes titlePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTitlePanel() {
		if (titlePanel == null) {
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 1;
			gridBagConstraints19.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints19.anchor = GridBagConstraints.WEST;
			gridBagConstraints19.weightx = 1.0D;
			gridBagConstraints19.gridy = 1;
			hostIdentity = new JLabel();
			if (this.record != null) {
				hostIdentity.setText(CertUtil.subjectToIdentity(this.record
						.getSubject()));
			} else {
				hostIdentity.setText("");
			}
			hostIdentity.setFont(new Font("Arial", Font.ITALIC, 12));
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 0;
			hostname = new JLabel();
			hostname.setText(this.record.getHost());
			hostname.setFont(new Font("Arial", Font.BOLD, 14));
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints1.weightx = 0.0D;
			gridBagConstraints1.gridheight = 2;
			gridBagConstraints1.gridx = 0;
			logo = new JLabel(GAARDSLookAndFeel.getLogoNoText32x32());
			titlePanel = new JPanel();
			titlePanel.setLayout(new GridBagLayout());
			titlePanel.add(logo, gridBagConstraints1);
			titlePanel.add(hostname, gridBagConstraints3);
			titlePanel.add(hostIdentity, gridBagConstraints19);
		}
		return titlePanel;
	}

	/**
	 * This method initializes auditPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAuditPanel() {
		if (auditPanel == null) {
			auditPanel = new FederationAuditPanel(this,FederationAuditPanel.HOST_MODE,String.valueOf(this.record.getId()));
		}
		return auditPanel;
	}

}
