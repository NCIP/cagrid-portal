package gov.nih.nci.cagrid.gts.portal;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.portal.CRLPanel;
import gov.nih.nci.cagrid.gridca.portal.CertificatePanel;
import gov.nih.nci.cagrid.gridca.portal.ProxyCaddy;
import gov.nih.nci.cagrid.gridca.portal.ProxyComboBox;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.client.GTSAdminClient;
import gov.nih.nci.cagrid.gts.client.GTSSearchClient;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.globus.gsi.GlobusCredential;
import org.projectmobius.portal.GridPortalComponent;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TrustedAuthorityWindow extends GridPortalComponent {

	private JPanel jContentPane = null;

	private JPanel topPanel = null;

	private JTabbedPane taPanel = null;

	private JLabel jLabel = null;

	private JComboBox gts = null;

	private JLabel jLabel1 = null;

	private JComboBox proxy = null;

	private JPanel propertiesPanel = null;

	private JLabel jLabel2 = null;

	private JTextField trustedAuthorityName = null;

	private JLabel jLabel3 = null;

	private JComboBox status = null;

	private JPanel propertiesNorthPanel = null;

	private JLabel jLabel4 = null;

	private JComboBox trustLevel = null;

	private JPanel buttonPanel = null;

	private JButton addButton = null;

	private JButton cancelButton = null;

	private CertificatePanel certificatePanel = null;

	private JButton importCertificate = null;

	private CRLPanel crlPanel = null;

	private JButton importCRL = null;

	private boolean update = false;


	/**
	 * This is the default constructor
	 */
	public TrustedAuthorityWindow() {
		super();
		initialize();
		this.updateTrustLevels();
	}


	public TrustedAuthorityWindow(String service, GlobusCredential cred, TrustedAuthority ta) throws Exception {
		super();
		update = true;
		initialize();
		this.gts.setSelectedItem(service);
		this.proxy.setSelectedItem(new ProxyCaddy(cred));
		this.updateTrustLevels();
		this.getTrustedAuthorityName().setText(ta.getTrustedAuthorityName());
		((StatusComboBox) this.getStatus()).setSelectedItem(ta.getStatus());
		trustLevel.setSelectedItem(ta.getTrustLevel());
		this.getCertificatePanel().setCertificate(
			CertUtil.loadCertificate(ta.getCertificate().getCertificateEncodedString()));
		if (ta.getCRL() != null) {
			if (ta.getCRL().getCrlEncodedString() != null) {
				crlPanel.setCRL(CertUtil.loadCRL(ta.getCRL().getCrlEncodedString()));
			}
		}

	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(600, 400);
		this.setContentPane(getJContentPane());
		if (!update) {
			this.setTitle("Add Trusted Authority");
			this.setFrameIcon(GTSLookAndFeel.getAddTrustedAuthorityIcon());
		} else {
			this.setTitle("View/Modify Trusted Authority");
			this.setFrameIcon(GTSLookAndFeel.getTrustedAuthorityIcon());
		}
	}


	private void updateTrustLevels() {
		trustLevel.removeAllItems();
		String service = Utils.clean((String) getGts().getSelectedItem());
		if (service != null) {
			try {
				GTSSearchClient client = new GTSSearchClient(service);
				TrustLevel[] levels = client.getTrustLevels();
				if (levels != null) {
					for (int i = 0; i < levels.length; i++) {
						trustLevel.addItem(levels[i].getName());
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				PortalUtils.showErrorMessage("Error obtaining the trust levels from " + service + ":\n"
					+ e.getMessage());
			}
		}
	}


	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.ipady = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
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
			jContentPane.add(getTaPanel(), gridBagConstraints1);
			jContentPane.add(getButtonPanel(), gridBagConstraints12);
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
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, GTSLookAndFeel.getPanelLabelColor()));
			topPanel.setLayout(new GridBagLayout());
			topPanel.add(jLabel, gridBagConstraints2);
			topPanel.add(getGts(), gridBagConstraints3);
			topPanel.add(jLabel1, gridBagConstraints4);
			topPanel.add(getProxy(), gridBagConstraints5);
		}
		return topPanel;
	}


	/**
	 * This method initializes taPanel
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getTaPanel() {
		if (taPanel == null) {
			taPanel = new JTabbedPane();
			taPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Trusted Authority",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, GTSLookAndFeel.getPanelLabelColor()));
			taPanel.addTab("Properties", GTSLookAndFeel.getTrustedAuthorityIcon(), getPropertiesNorthPanel(), null);
			taPanel.addTab("Certificate", GTSLookAndFeel.getCertificateIcon(), getCertificatePanel(), null);
			taPanel.addTab("Certificate Revocation List", GTSLookAndFeel.getCRLIcon(), getCrlPanel(), null);
		}
		return taPanel;
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
					updateTrustLevels();
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
			proxy = new ProxyComboBox();
		}
		return proxy;
	}


	/**
	 * This method initializes propertiesPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPropertiesPanel() {
		if (propertiesPanel == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 2;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 0;
			jLabel4 = new JLabel();
			jLabel4.setText("Trust Level");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints8.gridy = 1;
			jLabel3 = new JLabel();
			jLabel3.setText("Status");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.weightx = 1.0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.gridx = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("Trusted Authority Name");
			propertiesPanel = new JPanel();
			propertiesPanel.setLayout(new GridBagLayout());
			propertiesPanel.add(jLabel2, gridBagConstraints6);
			propertiesPanel.add(getTrustedAuthorityName(), gridBagConstraints7);
			propertiesPanel.add(jLabel3, gridBagConstraints8);
			propertiesPanel.add(getStatus(), gridBagConstraints9);
			propertiesPanel.add(jLabel4, gridBagConstraints10);
			propertiesPanel.add(getTrustLevel(), gridBagConstraints11);
		}
		return propertiesPanel;
	}


	/**
	 * This method initializes trustedAuthorityName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTrustedAuthorityName() {
		if (trustedAuthorityName == null) {
			trustedAuthorityName = new JTextField();
			trustedAuthorityName.setEditable(false);
		}
		return trustedAuthorityName;
	}


	/**
	 * This method initializes status
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getStatus() {
		if (status == null) {
			status = new StatusComboBox();
		}
		return status;
	}


	/**
	 * This method initializes propertiesNorthPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPropertiesNorthPanel() {
		if (propertiesNorthPanel == null) {
			propertiesNorthPanel = new JPanel();
			propertiesNorthPanel.setLayout(new BorderLayout());
			propertiesNorthPanel.add(getPropertiesPanel(), java.awt.BorderLayout.NORTH);
		}
		return propertiesNorthPanel;
	}


	/**
	 * This method initializes trustLevel
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getTrustLevel() {
		if (trustLevel == null) {
			trustLevel = new JComboBox();
		}
		return trustLevel;
	}


	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			if (!update) {
				buttonPanel.add(getImportCertificate(), null);
			}
			buttonPanel.add(getImportCRL(), null);
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
			if (!update) {
				addButton.setText("Add Trusted Authority");
				addButton.setIcon(GTSLookAndFeel.getAddTrustedAuthorityIcon());
				addButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						addTrustedAuthority();
					}
				});
			} else {
				addButton.setText("Update Trusted Authority");
				addButton.setIcon(GTSLookAndFeel.getModifyTrustedAuthorityIcon());
				addButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						updateTrustedAuthority();
					}
				});
			}

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
			cancelButton.setIcon(GTSLookAndFeel.getCloseIcon());
		}
		return cancelButton;
	}


	/**
	 * This method initializes certificatePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private CertificatePanel getCertificatePanel() {
		if (certificatePanel == null) {
			certificatePanel = new CertificatePanel();
			certificatePanel.setAllowExport(false);
			certificatePanel.setAllowImport(false);
		}
		return certificatePanel;
	}


	/**
	 * This method initializes importCertificate
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getImportCertificate() {
		if (importCertificate == null) {
			importCertificate = new JButton();
			importCertificate.setText("Import Certificate");
			importCertificate.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					importCertificate();
				}
			});
			importCertificate.setIcon(GTSLookAndFeel.getCertificateIcon());

		}
		return importCertificate;
	}


	private void importCertificate() {

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				X509Certificate certificate = CertUtil
					.loadCertificate(new File(fc.getSelectedFile().getAbsolutePath()));
				certificatePanel.clearCertificate();
				crlPanel.clearCRL();
				certificatePanel.setCertificate(certificate);
				this.getTrustedAuthorityName().setText(certificate.getSubjectDN().getName());
			} catch (Exception ex) {
				PortalUtils.showErrorMessage(ex);
			}
		}

	}


	private void importCRL() {
		crlPanel.clearCRL();
		X509Certificate cert = certificatePanel.getCertificate();
		if (cert == null) {
			PortalUtils.showErrorMessage("You must import a certificate before importing a CRL");

		}
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				X509CRL crl = CertUtil.loadCRL(new File(fc.getSelectedFile().getAbsolutePath()));
				try {
					crl.verify(cert.getPublicKey());
				} catch (Exception crle) {
					PortalUtils
						.showErrorMessage("Error verifying CRL, the CRL must be issued and signed by same key is the Trusted Authority's Certificate");
				}
				crlPanel.setCRL(crl);
			} catch (Exception ex) {
				PortalUtils.showErrorMessage(ex);
			}
		}

	}


	/**
	 * This method initializes crlPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private CRLPanel getCrlPanel() {
		if (crlPanel == null) {
			crlPanel = new CRLPanel();
		}
		return crlPanel;
	}


	/**
	 * This method initializes importCRL
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getImportCRL() {
		if (importCRL == null) {
			importCRL = new JButton();
			importCRL.setText("Import CRL");
			importCRL.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					importCRL();
				}
			});
			importCRL.setIcon(GTSLookAndFeel.getCRLIcon());
		}
		return importCRL;
	}


	private void addTrustedAuthority() {
		try {
			getAddButton().setEnabled(false);
			X509Certificate cert = this.certificatePanel.getCertificate();
			if (cert == null) {
				PortalUtils
					.showErrorMessage("No certificate specified, you must specify a certificate to add a Trusted Authority!!!");
				getAddButton().setEnabled(true);
				return;

			}
			TrustedAuthority ta = new TrustedAuthority();
			ta.setTrustedAuthorityName(this.trustedAuthorityName.getText());
			ta.setStatus(((StatusComboBox) getStatus()).getStatus());
			ta.setTrustLevel((String) trustLevel.getSelectedItem());
			ta.setCertificate(new gov.nih.nci.cagrid.gts.bean.X509Certificate(CertUtil.writeCertificate(cert)));
			if (crlPanel.getCRL() != null) {
				ta.setCRL(new gov.nih.nci.cagrid.gts.bean.X509CRL(CertUtil.writeCRL(crlPanel.getCRL())));
			}
			GlobusCredential proxy = ((ProxyComboBox) getProxy()).getSelectedProxy();
			String service = ((GTSServiceListComboBox) getGts()).getSelectedService();
			GTSAdminClient client = new GTSAdminClient(service, proxy);
			client.addTrustedAuthority(ta);
			PortalUtils.showMessage("The Trusted Authority, " + ta.getTrustedAuthorityName()
				+ " was succesfully added!!!");
			dispose();
		} catch (Exception e) {
			getAddButton().setEnabled(true);
			e.printStackTrace();
			PortalUtils.showErrorMessage(e);
		}

	}


	private void updateTrustedAuthority() {
		try {
			getAddButton().setEnabled(false);
			TrustedAuthority ta = new TrustedAuthority();
			ta.setTrustedAuthorityName(this.trustedAuthorityName.getText());
			ta.setStatus(((StatusComboBox) getStatus()).getStatus());
			ta.setTrustLevel((String) trustLevel.getSelectedItem());
			if (crlPanel.getCRL() != null) {
				ta.setCRL(new gov.nih.nci.cagrid.gts.bean.X509CRL(CertUtil.writeCRL(crlPanel.getCRL())));
			}
			GlobusCredential proxy = ((ProxyComboBox) getProxy()).getSelectedProxy();
			String service = ((GTSServiceListComboBox) getGts()).getSelectedService();
			GTSAdminClient client = new GTSAdminClient(service, proxy);
			client.updateTrustedAuthority(ta);
			PortalUtils.showMessage("The Trusted Authority, " + ta.getTrustedAuthorityName()
				+ " was succesfully updated!!!");
			getAddButton().setEnabled(true);
		} catch (Exception e) {
			getAddButton().setEnabled(true);
			e.printStackTrace();
			PortalUtils.showErrorMessage(e);
		}

	}

}
