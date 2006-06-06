package gov.nih.nci.cagrid.dorian.ifs.portal;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.dorian.client.IFSAdministrationClient;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.portal.DorianLookAndFeel;
import gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.portal.CertificatePanel;
import gov.nih.nci.cagrid.gridca.portal.ProxyCaddy;
import gov.nih.nci.cagrid.gridca.portal.ProxyComboBox;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.globus.gsi.GlobusCredential;
import org.projectmobius.common.MobiusRunnable;
import org.projectmobius.portal.GridPortalBaseFrame;
import org.projectmobius.portal.PortalResourceManager;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: TrustedIdPWindow.java,v 1.14 2006-06-06 04:29:18 langella Exp $
 */
public class TrustedIdPWindow extends GridPortalBaseFrame {
	public static final String PASSWORD = SAMLAuthenticationMethod.value1.getValue();
	public static final String KERBEROS = SAMLAuthenticationMethod.value2.getValue();
	public static final String SRP = SAMLAuthenticationMethod.value3.getValue();
	public static final String HARDWARE_TOKEN = SAMLAuthenticationMethod.value4.getValue();
	public static final String TLS = SAMLAuthenticationMethod.value5.getValue();
	public static final String PKI = SAMLAuthenticationMethod.value6.getValue();
	public static final String PGP = SAMLAuthenticationMethod.value7.getValue();
	public static final String SPKI = SAMLAuthenticationMethod.value8.getValue();
	public static final String XKMS = SAMLAuthenticationMethod.value9.getValue();
	public static final String XML_SIGNATURE = SAMLAuthenticationMethod.value10.getValue();
	public static final String UNSPECIFIED = SAMLAuthenticationMethod.value11.getValue();

	private final static String INFO_PANEL = "IdP Information";

	private final static String CERTIFICATE_PANEL = "Certificate";

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel buttonPanel = null;

	private JButton updateTrustedIdP = null;

	private JTabbedPane jTabbedPane = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JLabel jLabel14 = null;

	private String serviceId;

	private JTextField service = null;

	private JPanel infoPanel = null;

	private GlobusCredential cred;

	private JLabel credentialLabel = null;

	private JComboBox proxy = null;

	private TrustedIdP idp;

	private JPanel certificatePanel = null;

	private CertificatePanel credPanel = null;

	private boolean newTrustedIdP;

	private JLabel idLabel = null;

	private JTextField idpId = null;

	private JLabel nameLabel = null;

	private JTextField idpName = null;

	private JLabel statusLabel = null;

	private TrustedIdPStatusComboBox status = null;

	private IFSUserPolicy[] policies;

	private JLabel policyLabel = null;

	private JComboBox userPolicy = null;
	private JPanel authPanel = null;
	private JLabel passwordLabel = null;
	private JCheckBox passwordMethod = null;
	private JCheckBox kerberosMethod = null;
	private JLabel kerberosLabel = null;
	private JCheckBox srpMethod = null;
	private JLabel srpLabel = null;
	private JCheckBox hardwareTokenMethod = null;
	private JLabel tokenLabel = null;
	private JCheckBox tlsMethod = null;
	private JLabel tlsLabel = null;
	private JCheckBox pkiMethod = null;
	private JLabel pkiLabel = null;
	private JCheckBox pgpMethod = null;
	private JLabel pgpLabel = null;
	private JCheckBox spkiMethod = null;
	private JLabel spkiLabel = null;
	private JCheckBox xkmsMethod = null;
	private JLabel xkmsLabel = null;
	private JCheckBox xmlSignatureMethod = null;
	private JLabel xmlSignatureLabel = null;
	private JCheckBox unspecifiedMethod = null;
	private JLabel unspecifiedLabel = null;
	private TrustedIdPsWindow window;


	public TrustedIdPWindow(TrustedIdPsWindow window, String serviceId, GlobusCredential proxy, IFSUserPolicy[] policies) {
		super();
		this.window = window;
		this.serviceId = serviceId;
		this.cred = proxy;
		this.idp = new TrustedIdP();
		this.newTrustedIdP = true;
		this.policies = policies;
		initialize();
	}


	/**
	 * This is the default constructor
	 */
	public TrustedIdPWindow(String serviceId, GlobusCredential proxy, TrustedIdP idp, IFSUserPolicy[] policies)
		throws Exception {
		super();
		this.serviceId = serviceId;
		this.cred = proxy;
		this.idp = idp;
		this.newTrustedIdP = false;
		this.policies = policies;
		initialize();
	}


	public class UserPolicyCaddy {
		private IFSUserPolicy policy;


		public UserPolicyCaddy(String className) {
			this.policy = new IFSUserPolicy(className, "");
		}


		public UserPolicyCaddy(IFSUserPolicy policy) {
			this.policy = policy;
		}


		public IFSUserPolicy getPolicy() {
			return policy;
		}


		public String toString() {
			return policy.getName();
		}


		public boolean equals(Object o) {
			UserPolicyCaddy up = (UserPolicyCaddy) o;
			if (this.getPolicy().getClassName().equals(up.getPolicy().getClassName())) {
				return true;
			} else {
				return false;
			}
		}
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		if (this.newTrustedIdP) {
			this.setTitle("Add Trusted IdP");
		} else {
			this.setTitle("Trusted IdP [" + idp.getName() + "]");
		}
		this.setFrameIcon(DorianLookAndFeel.getTrustedIdPIcon());

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
			buttonPanel.add(getUpdateTrustedIdP(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes manageUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getUpdateTrustedIdP() {
		if (updateTrustedIdP == null) {
			updateTrustedIdP = new JButton();

			if (this.newTrustedIdP) {
				updateTrustedIdP.setText("Add");
			} else {
				updateTrustedIdP.setText("Update");
			}

			updateTrustedIdP.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							updateTrustedIdP();
						}
					};
					try {
						PortalResourceManager.getInstance().getThreadManager().executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}

				}
			});
			updateTrustedIdP.setIcon(DorianLookAndFeel.getTrustedIdPIcon());
		}
		return updateTrustedIdP;
	}


	private synchronized void updateTrustedIdP() {

		try {
			if (getCredPanel().getCertificate() != null) {
				idp.setIdPCertificate(CertUtil.writeCertificate(getCredPanel().getCertificate()));
			}
			idp.setName(getIdPName().getText().trim());
			idp.setStatus(getStatus().getSelectedStatus());
			idp.setUserPolicyClass(((UserPolicyCaddy) getUserPolicy().getSelectedItem()).getPolicy().getClassName());

			List authMethod = new ArrayList();
			if (getPasswordMethod().isSelected()) {
				authMethod.add(SAMLAuthenticationMethod.fromValue(PASSWORD));
			}

			if (getKerberosMethod().isSelected()) {
				authMethod.add(SAMLAuthenticationMethod.fromValue(KERBEROS));
			}

			if (getSrpMethod().isSelected()) {
				authMethod.add(SAMLAuthenticationMethod.fromValue(SRP));
			}

			if (getHardwareTokenMethod().isSelected()) {
				authMethod.add(SAMLAuthenticationMethod.fromValue(HARDWARE_TOKEN));
			}

			if (getTlsMethod().isSelected()) {
				authMethod.add(SAMLAuthenticationMethod.fromValue(TLS));
			}
			if (getPkiMethod().isSelected()) {
				authMethod.add(SAMLAuthenticationMethod.fromValue(PKI));
			}
			if (getPgpMethod().isSelected()) {
				authMethod.add(SAMLAuthenticationMethod.fromValue(PGP));
			}
			if (getSpkiMethod().isSelected()) {
				authMethod.add(SAMLAuthenticationMethod.fromValue(SPKI));
			}

			if (getXkmsMethod().isSelected()) {
				authMethod.add(SAMLAuthenticationMethod.fromValue(XKMS));
			}

			if (getXmlSignatureMethod().isSelected()) {
				authMethod.add(SAMLAuthenticationMethod.fromValue(XML_SIGNATURE));
			}

			if (getUnspecifiedMethod().isSelected()) {
				authMethod.add(SAMLAuthenticationMethod.fromValue(UNSPECIFIED));
			}
			SAMLAuthenticationMethod[] saml = new SAMLAuthenticationMethod[authMethod.size()];
			for (int i = 0; i < authMethod.size(); i++) {
				saml[i] = (SAMLAuthenticationMethod) authMethod.get(i);
			}

			idp.setAuthenticationMethod(saml);

			String service = getService().getText();
			GlobusCredential c = ((ProxyCaddy) getProxy().getSelectedItem()).getProxy();
			IFSAdministrationClient client = new IFSAdministrationClient(service, c);
			if (newTrustedIdP) {
				window.addTrustedIdP(client.addTrustedIdP(idp));
				dispose();
			} else {
				client.updateTrustedIdP(idp);
				PortalUtils.showMessage("The Trusted IdP was updated successfully.");
			}
		} catch (PermissionDeniedFault pdf) {
			PortalUtils.showErrorMessage(pdf);
		} catch (Exception e) {
			e.printStackTrace();
			PortalUtils.showErrorMessage(e);
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
			jTabbedPane.setBorder(BorderFactory.createTitledBorder(null, "Trusted Identity Provider",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, DorianLookAndFeel
					.getPanelLabelColor()));
			jTabbedPane.addTab(INFO_PANEL, DorianLookAndFeel.getTrustedIdPIcon(), getInfoPanel(), null);
			jTabbedPane.addTab(CERTIFICATE_PANEL, DorianLookAndFeel.getCertificateIcon(), getCertificatePanel(), null);
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
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints13.gridwidth = 2;
			gridBagConstraints13.weightx = 1.0D;
			gridBagConstraints13.weighty = 1.0D;
			gridBagConstraints13.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints13.gridy = 4;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridy = 3;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.gridy = 3;
			policyLabel = new JLabel();
			policyLabel.setText("User Policy");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints9.gridy = 2;
			statusLabel = new JLabel();
			statusLabel.setText("Status");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.gridx = 0;
			nameLabel = new JLabel();
			nameLabel.setText("Name");
			nameLabel.setName("Name");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.weightx = 1.0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridx = 0;
			idLabel = new JLabel();
			idLabel.setText("IdP Id");
			if (newTrustedIdP) {

			}
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(getStatus(), gridBagConstraints10);
			jPanel1.setName(INFO_PANEL);
			jPanel1.add(statusLabel, gridBagConstraints9);
			jPanel1.add(idLabel, gridBagConstraints5);
			jPanel1.add(getIdpId(), gridBagConstraints6);
			jPanel1.add(nameLabel, gridBagConstraints7);
			jPanel1.add(getIdPName(), gridBagConstraints8);
			jPanel1.add(policyLabel, gridBagConstraints11);
			jPanel1.add(getUserPolicy(), gridBagConstraints12);
			jPanel1.add(getAuthPanel(), gridBagConstraints13);

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
			credentialLabel.setText("Proxy");
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
			jPanel2.setBorder(BorderFactory.createTitledBorder(null, "Login Information",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, DorianLookAndFeel
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
		}
		return infoPanel;
	}


	/**
	 * This method initializes proxy1
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getProxy() {
		if (proxy == null) {
			proxy = new ProxyComboBox(cred);
		}
		return proxy;
	}


	/**
	 * This method initializes certificatePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCertificatePanel() {
		if (certificatePanel == null) {
			GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
			gridBagConstraints40.gridx = 0;
			gridBagConstraints40.ipadx = 208;
			gridBagConstraints40.weightx = 1.0D;
			gridBagConstraints40.weighty = 1.0D;
			gridBagConstraints40.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints40.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints40.gridy = 0;
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints36.gridy = 0;
			gridBagConstraints36.weightx = 1.0D;
			gridBagConstraints36.weighty = 1.0D;
			gridBagConstraints36.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints36.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints36.gridx = 0;
			certificatePanel = new JPanel();
			certificatePanel.setLayout(new GridBagLayout());
			certificatePanel.add(getCredPanel(), gridBagConstraints40);
		}
		return certificatePanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private CertificatePanel getCredPanel() {
		if (credPanel == null) {
			try {
				credPanel = new CertificatePanel();
				if (idp.getIdPCertificate() != null) {
					credPanel.setCertificate(CertUtil.loadCertificate(idp.getIdPCertificate()));
				}
				// credPanel.setAllowImport(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return credPanel;
	}


	/**
	 * This method initializes idpId
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getIdpId() {
		if (idpId == null) {
			idpId = new JTextField();
			idpId.setEditable(false);
			if (!newTrustedIdP) {
				idpId.setText(String.valueOf(idp.getId()));
			}
		}
		return idpId;
	}


	/**
	 * This method initializes idPName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getIdPName() {
		if (idpName == null) {
			idpName = new JTextField();
			if (!newTrustedIdP) {
				idpName.setText(idp.getName());
			}
		}
		return idpName;
	}


	/**
	 * This method initializes status
	 * 
	 * @return javax.swing.JComboBox
	 */
	private TrustedIdPStatusComboBox getStatus() {
		if (status == null) {
			status = new TrustedIdPStatusComboBox();
			if (!newTrustedIdP) {
				status.setSelectedItem(idp.getStatus());
			}
		}
		return status;
	}


	/**
	 * This method initializes userPolicy
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getUserPolicy() {
		if (userPolicy == null) {
			userPolicy = new JComboBox();
			for (int i = 0; i < policies.length; i++) {
				userPolicy.addItem(new UserPolicyCaddy(policies[i]));

				if (!newTrustedIdP) {

					if (idp.getUserPolicyClass().equals(policies[i].getClassName())) {
						int count = userPolicy.getItemCount();
						userPolicy.setSelectedIndex((count - 1));
					}
				}
			}

		}
		return userPolicy;
	}


	/**
	 * This method initializes authPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAuthPanel() {
		if (authPanel == null) {
			GridBagConstraints gridBagConstraints39 = new GridBagConstraints();
			gridBagConstraints39.gridx = 1;
			gridBagConstraints39.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints39.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints39.gridy = 5;
			unspecifiedLabel = new JLabel();
			unspecifiedLabel.setText("Unspecified");
			GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
			gridBagConstraints38.gridx = 0;
			gridBagConstraints38.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints38.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints38.gridy = 5;
			GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
			gridBagConstraints37.gridx = 3;
			gridBagConstraints37.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints37.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints37.gridy = 4;
			xmlSignatureLabel = new JLabel();
			xmlSignatureLabel.setText("XML Digital Signature");
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.gridx = 2;
			gridBagConstraints35.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints35.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints35.gridy = 4;
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.gridx = 1;
			gridBagConstraints34.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints34.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints34.gridy = 4;
			xkmsLabel = new JLabel();
			xkmsLabel.setText("XML Key Management Specification (XKMS)");
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.gridx = 0;
			gridBagConstraints33.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints33.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints33.gridy = 4;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.gridx = 3;
			gridBagConstraints32.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints32.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints32.gridy = 3;
			spkiLabel = new JLabel();
			spkiLabel.setText("Simple Public Key Infrastructure (SPKI)");
			spkiLabel.setName("");
			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.gridx = 2;
			gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints30.gridy = 3;
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.gridx = 1;
			gridBagConstraints29.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints29.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints29.gridy = 3;
			pgpLabel = new JLabel();
			pgpLabel.setText("Pretty Good Privacy (PGP)");
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.gridx = 0;
			gridBagConstraints26.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints26.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints26.gridy = 3;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.gridx = 3;
			gridBagConstraints25.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints25.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints25.gridy = 2;
			pkiLabel = new JLabel();
			pkiLabel.setText("X509 Public Key Infrastructure (PKI)");
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.gridx = 2;
			gridBagConstraints24.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints24.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints24.gridy = 2;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 1;
			gridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints23.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints23.gridy = 2;
			tlsLabel = new JLabel();
			tlsLabel.setText("Transport Layer Security (TLS)");
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 0;
			gridBagConstraints22.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints22.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints22.gridy = 2;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 3;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints21.gridy = 1;
			tokenLabel = new JLabel();
			tokenLabel.setText("Hardware Token");
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 2;
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints20.gridy = 1;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 1;
			gridBagConstraints19.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints19.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints19.gridy = 1;
			srpLabel = new JLabel();
			srpLabel.setText("Secure Remote Password (SRP)");
			srpLabel.setName("Secure Remote Password (SRP)");
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints18.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints18.gridy = 1;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 3;
			gridBagConstraints17.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints17.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints17.gridy = 0;
			kerberosLabel = new JLabel();
			kerberosLabel.setText("Kerberos");
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 2;
			gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints16.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints16.gridy = 0;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints15.gridy = 0;
			gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints15.gridx = 1;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints14.gridy = 0;
			gridBagConstraints14.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints14.gridx = 0;
			passwordLabel = new JLabel();
			passwordLabel.setText("Password");
			authPanel = new JPanel();
			authPanel.setLayout(new GridBagLayout());
			authPanel.add(getSrpMethod(), gridBagConstraints18);
			authPanel.add(getKerberosMethod(), gridBagConstraints16);
			authPanel.add(kerberosLabel, gridBagConstraints17);
			authPanel.add(srpLabel, gridBagConstraints19);
			authPanel.add(getHardwareTokenMethod(), gridBagConstraints20);
			authPanel.add(tokenLabel, gridBagConstraints21);
			authPanel.add(getTlsMethod(), gridBagConstraints22);
			authPanel.add(tlsLabel, gridBagConstraints23);
			authPanel.add(getPkiMethod(), gridBagConstraints24);
			authPanel.add(pkiLabel, gridBagConstraints25);
			authPanel.add(getPgpMethod(), gridBagConstraints26);
			authPanel.add(pgpLabel, gridBagConstraints29);
			authPanel.add(getSpkiMethod(), gridBagConstraints30);
			authPanel.add(spkiLabel, gridBagConstraints32);
			authPanel.add(getXmlSignatureMethod(), gridBagConstraints35);
			authPanel.add(getXkmsMethod(), gridBagConstraints33);
			authPanel.add(xkmsLabel, gridBagConstraints34);
			authPanel.add(xmlSignatureLabel, gridBagConstraints37);
			authPanel.add(getUnspecifiedMethod(), gridBagConstraints38);
			authPanel.add(unspecifiedLabel, gridBagConstraints39);
			authPanel.setBorder(BorderFactory.createTitledBorder(null, "Accepted Authentication Methods",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, DorianLookAndFeel
					.getPanelLabelColor()));
			authPanel.add(passwordLabel, gridBagConstraints15);
			authPanel.add(getPasswordMethod(), gridBagConstraints14);

		}
		return authPanel;
	}


	/**
	 * This method initializes passwordMethod
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getPasswordMethod() {
		if (passwordMethod == null) {
			passwordMethod = new JCheckBox();
			if (!newTrustedIdP) {
				passwordMethod.setSelected(idpAcceptsMethod(PASSWORD));
			}
		}
		return passwordMethod;
	}


	public boolean idpAcceptsMethod(String method) {
		SAMLAuthenticationMethod[] methods = idp.getAuthenticationMethod();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getValue().equals(method)) {
				return true;
			}
		}
		return false;
	}


	private JCheckBox getKerberosMethod() {
		if (kerberosMethod == null) {
			kerberosMethod = new JCheckBox();
			if (!newTrustedIdP) {
				kerberosMethod.setSelected(idpAcceptsMethod(KERBEROS));
			}
		}
		return kerberosMethod;
	}


	private JCheckBox getSrpMethod() {
		if (srpMethod == null) {
			srpMethod = new JCheckBox();
			if (!newTrustedIdP) {
				srpMethod.setSelected(idpAcceptsMethod(SRP));
			}
		}
		return srpMethod;
	}


	/**
	 * This method initializes hardwareTokenMethod
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getHardwareTokenMethod() {
		if (hardwareTokenMethod == null) {
			hardwareTokenMethod = new JCheckBox();
			if (!newTrustedIdP) {
				hardwareTokenMethod.setSelected(idpAcceptsMethod(HARDWARE_TOKEN));
			}
		}
		return hardwareTokenMethod;
	}


	/**
	 * This method initializes tlsMethod
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getTlsMethod() {
		if (tlsMethod == null) {
			tlsMethod = new JCheckBox();
			if (!newTrustedIdP) {
				tlsMethod.setSelected(idpAcceptsMethod(TLS));
			}
		}
		return tlsMethod;
	}


	/**
	 * This method initializes pkiMethod
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getPkiMethod() {
		if (pkiMethod == null) {
			pkiMethod = new JCheckBox();
			if (!newTrustedIdP) {
				pkiMethod.setSelected(idpAcceptsMethod(PKI));
			}
		}
		return pkiMethod;
	}


	/**
	 * This method initializes pgpMethod
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getPgpMethod() {
		if (pgpMethod == null) {
			pgpMethod = new JCheckBox();
			if (!newTrustedIdP) {
				pgpMethod.setSelected(idpAcceptsMethod(PGP));
			}
		}
		return pgpMethod;
	}


	/**
	 * This method initializes spkiMethod
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getSpkiMethod() {
		if (spkiMethod == null) {
			spkiMethod = new JCheckBox();
			if (!newTrustedIdP) {
				spkiMethod.setSelected(idpAcceptsMethod(SPKI));
			}
		}
		return spkiMethod;
	}


	/**
	 * This method initializes xkmsMethod
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getXkmsMethod() {
		if (xkmsMethod == null) {
			xkmsMethod = new JCheckBox();
			if (!newTrustedIdP) {
				xkmsMethod.setSelected(idpAcceptsMethod(XKMS));
			}
		}
		return xkmsMethod;
	}


	/**
	 * This method initializes xmlSignatureLabel
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getXmlSignatureMethod() {
		if (xmlSignatureMethod == null) {
			xmlSignatureMethod = new JCheckBox();
			if (!newTrustedIdP) {
				xmlSignatureMethod.setSelected(idpAcceptsMethod(XML_SIGNATURE));
			}
		}
		return xmlSignatureMethod;
	}


	/**
	 * This method initializes unspecifiedMethod
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getUnspecifiedMethod() {
		if (unspecifiedMethod == null) {
			unspecifiedMethod = new JCheckBox();
			if (!newTrustedIdP) {
				unspecifiedMethod.setSelected(idpAcceptsMethod(UNSPECIFIED));
			}
		}
		return unspecifiedMethod;
	}

}
