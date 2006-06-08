package gov.nih.nci.cagrid.introduce.portal.modification.security;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.common.security.ProxyUtil;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.portal.CertificatePanel;
import gov.nih.nci.cagrid.gridca.portal.ProxyPanel;
import gov.nih.nci.cagrid.introduce.beans.security.AnonymousCommunication;
import gov.nih.nci.cagrid.introduce.beans.security.ClientAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.ClientCommunication;
import gov.nih.nci.cagrid.introduce.beans.security.DelegationMode;
import gov.nih.nci.cagrid.introduce.beans.security.GridMapAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.NoAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.ProxyCredential;
import gov.nih.nci.cagrid.introduce.beans.security.RunAsMode;
import gov.nih.nci.cagrid.introduce.beans.security.SecureConversation;
import gov.nih.nci.cagrid.introduce.beans.security.SecureMessage;
import gov.nih.nci.cagrid.introduce.beans.security.SecuritySetting;
import gov.nih.nci.cagrid.introduce.beans.security.SelfAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceCredential;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.TransportLevelSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.X509Credential;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

import org.projectmobius.portal.PortalResourceManager;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class ServiceSecurityPanel extends JPanel implements PanelSynchronizer {

	private JPanel secureCommunicationPanel = null;

	private ButtonGroup buttonGroup = new ButtonGroup();

	private JRadioButton noneButton = null;

	private JRadioButton customButton = null;

	private TransportLevelSecurityPanel tlsPanel = null;

	private JCheckBox tlsButton = null;

	private JCheckBox secureConversationButton = null;

	private JPanel choicePanel = null;

	private JPanel commPanel = null;

	private JLabel jLabel1 = null;

	private JLabel Custom = null;

	private JLabel jLabel2 = null;

	private JLabel jLabel3 = null;

	private JCheckBox secureMessageButton = null;

	private JLabel jLabel4 = null;

	private SecureConversationPanel secureConversationPanel = null;

	private SecureMessagePanel secureMessagePanel = null;

	private JComboBox clientCommunication = null;

	private JComboBox runAsMode = null;

	private JComboBox delegationMode = null;

	private boolean syncingComm = false;

	private JComboBox anonymousCommunication = null;

	private JTabbedPane transportPanel = null;

	private JPanel clientAuthorization = null;

	private HostAuthorizationPanel hostAuthorization = null;

	private CardLayout clientAuthLayout = new CardLayout();

	private IdentityAuthorizationPanel identityAuthorization = null;

	private JPanel blankPanel = null;

	private JComboBox clientAuth = null;

	private boolean isInited = false;

	private final static String NO_AUTHORIZATION = "No Authorization";

	private final static String HOST_AUTHORIZATION = "Host Authorization";

	private final static String IDENTITY_AUTHORIZATION = "Identity Authorization";

	private final static String SELF_AUTHORIZATION = "Self Authorization";

	private final static String GRID_MAP_AUTHORIZATION = "Gridmap";

	private final static String FILE_SYSTEM_PROXY = "Proxy from file system";

	private final static String FILE_SYSTEM_CERT_KEY = "Certificate/Private Key from file system";

	private final static String PROXY_CRED_PANEL = "Proxy Cred Panel";

	private final static String PKI_CRED_PANEL = "PKI Cred Panel";

	private final static String N0_CRED_PANEL = "No Cred Panel";

	private JPanel communicationPanel = null;

	private JPanel clientPanel = null;

	private JLabel jLabel9 = null;

	private JLabel jLabel5 = null;

	private JLabel dele = null;

	private JLabel jLabel7 = null;

	private JPanel generalSecurity = null;

	private JLabel jLabel = null;

	private JPanel serviceCredentials = null;

	private JPanel selectPanel = null;

	private JComboBox credentialLoadMethod = null;

	private JButton loadCredentialsButton = null;

	private String certificateLocation;

	private String privateKeyLocation;

	private String proxyLocation;

	private JPanel credentialsPanel = null;

	private CardLayout credentialPanelLayout;

	private JPanel nonePanel = null;

	private CertificatePanel certificatePanel = null;

	private ProxyPanel proxyPanel = null;

	private boolean isSyncingRunAs = false;

	private JPanel authorizationPanel = null;

	private JPanel authorizationTypePanel = null;

	private JLabel authLabel = null;

	private JComboBox authorizationMechanism = null;

	private CardLayout authLayout;

	private JPanel authPanel = null;

	private JPanel noAuthorizationPanel = null;

	private JPanel gridmapPanel = null;

	private JPanel jPanel = null;

	private JLabel jLabel6 = null;


	public ServiceSecurityPanel() {
		super();
		initialize();
	}


	public ServiceSecurityPanel(ServiceSecurity sec) {
		super();
		initialize();
		try {
			setServiceSecurity(sec);
		} catch (Exception e) {
			PortalUtils.showErrorMessage(e);
		}
	}


	private void initialize() {
		/*
		 * setBorder(javax.swing.BorderFactory.createTitledBorder( null,
		 * "Transport Level Security (TLS)",
		 * javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
		 * javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
		 * IntroduceLookAndFeel.getPanelLabelColor()));
		 */
		GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
		gridBagConstraints17.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints17.weighty = 1.0;
		gridBagConstraints17.gridx = 0;
		gridBagConstraints17.gridy = 2;
		gridBagConstraints17.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints17.weightx = 1.0;
		GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
		gridBagConstraints71.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints71.weightx = 1.0D;
		this.setLayout(new GridBagLayout());
		this.add(getSecureCommunicationPanel(), gridBagConstraints71);
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Security Configuration",
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
			null, IntroduceLookAndFeel.getPanelLabelColor()));
		this.add(getTransportPanel(), gridBagConstraints17);
		synchronize();
		isInited = true;
	}


	/**
	 * This method initializes secureCommunicationPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSecureCommunicationPanel() {
		if (secureCommunicationPanel == null) {
			secureCommunicationPanel = new JPanel();
			secureCommunicationPanel.add(getChoicePanel());
		}
		return secureCommunicationPanel;
	}


	/**
	 * This method initializes noneButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getNoneButton() {
		if (noneButton == null) {
			noneButton = new JRadioButton();
			noneButton.setSelected(true);
			noneButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					synchronize();
				}
			});
			buttonGroup.add(noneButton);
		}
		return noneButton;
	}


	/**
	 * This method initializes customButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getCustomButton() {
		if (customButton == null) {
			customButton = new JRadioButton();
			customButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					synchronize();
				}
			});
			buttonGroup.add(customButton);
		}
		return customButton;
	}


	private boolean isSecure() {
		if (usesTransportSecurity() || usesSecureConversation() || usesSecureMessage()) {
			return true;
		} else {
			return false;
		}

	}


	private boolean usesTransportSecurity() {
		if (tlsButton.isSelected()) {
			return true;
		} else {
			return false;
		}
	}


	private boolean usesSecureConversation() {
		if (secureConversationButton.isSelected()) {
			return true;
		} else {
			return false;
		}
	}


	private boolean usesSecureMessage() {
		if (secureMessageButton.isSelected()) {
			return true;
		} else {
			return false;
		}
	}


	public ServiceSecurity getServiceSecurity() throws Exception {

		if (customButton.isSelected()) {
			ServiceSecurity ss = new ServiceSecurity();
			ss.setSecuritySetting(SecuritySetting.Custom);
			if (!isSecure()) {
				throw new Exception("You must select at least one transport mechanism!!!");
			}

			if (tlsButton.isSelected()) {
				ss.setTransportLevelSecurity(this.tlsPanel.getTransportLevelSecurity());

			}
			if (secureConversationButton.isSelected()) {
				ss.setSecureConversation(this.secureConversationPanel.getSecureConversation());

			}
			if (secureMessageButton.isSelected()) {
				ss.setSecureMessage(this.secureMessagePanel.getSecureMessage());
			}
			if (clientCommunication.isEnabled()) {
				ss.setClientCommunication((ClientCommunication) clientCommunication.getSelectedItem());
			}
			if (runAsMode.isEnabled()) {
				ss.setRunAsMode((RunAsMode) runAsMode.getSelectedItem());
			}
			if (delegationMode.isEnabled()) {
				ss.setDelegationMode((DelegationMode) delegationMode.getSelectedItem());
			} else {
				ss.setDelegationMode(DelegationMode.None);
			}
			if (anonymousCommunication.isEnabled()) {
				ss.setAnonymousClients((AnonymousCommunication) anonymousCommunication.getSelectedItem());
			} else {
				ss.setAnonymousClients(AnonymousCommunication.No);
			}

			ClientAuthorization cli = new ClientAuthorization();
			if (clientAuth.isEnabled()) {
				if (this.clientAuth.getSelectedItem().equals(HOST_AUTHORIZATION)) {
					cli.setHostAuthorization(hostAuthorization.getHostAuthorization());
				} else if (this.clientAuth.getSelectedItem().equals(IDENTITY_AUTHORIZATION)) {
					cli.setIdentityAuthorization(identityAuthorization.getIdentitytAuthorization());
				} else if (this.clientAuth.getSelectedItem().equals(SELF_AUTHORIZATION)) {
					cli.setSelfAuthorization(new SelfAuthorization());
				} else {
					cli.setNoAuthorization(new NoAuthorization());
				}
			} else {
				cli.setNoAuthorization(new NoAuthorization());
			}
			ss.setClientAuthorization(cli);
			if ((this.certificateLocation != null) && (this.privateKeyLocation != null)) {
				ServiceCredential cred = new ServiceCredential();
				X509Credential x509 = new X509Credential();
				x509.setCertificateLocation(this.certificateLocation);
				x509.setPrivateKeyLocation(this.privateKeyLocation);
				cred.setX509Credential(x509);
				ss.setServiceCredentials(cred);
			} else if (this.proxyLocation != null) {
				ServiceCredential cred = new ServiceCredential();
				ProxyCredential proxy = new ProxyCredential();
				proxy.setProxyLocation(this.proxyLocation);
				cred.setProxyCredential(proxy);
				ss.setServiceCredentials(cred);
			}

			String authType = (String) this.authorizationMechanism.getSelectedItem();
			ServiceAuthorization sa = new ServiceAuthorization();
			if (authType.equals(GRID_MAP_AUTHORIZATION)) {
				GridMapAuthorization gma = new GridMapAuthorization();
				gma.setGridMapFileLocation(((GridMapPanel) gridmapPanel).saveGridMapAndGetLocation());
				sa.setGridMapAuthorization(gma);
			} else {
				sa.setNoAuthorization(new NoAuthorization());
			}
			ss.setServiceAuthorization(sa);

			return ss;
		} else {
			return null;
		}
	}


	public void setServiceSecurity(ServiceSecurity ss) throws Exception {
		if (ss != null && ss.getSecuritySetting() != null) {
			if (ss.getSecuritySetting().equals(SecuritySetting.None)) {
				noneButton.setSelected(true);
			} else if (ss.getSecuritySetting().equals(SecuritySetting.Custom)) {
				customButton.setSelected(true);

				TransportLevelSecurity tls = ss.getTransportLevelSecurity();
				if (tls != null) {
					tlsButton.setSelected(true);
					this.tlsPanel.setTransportLevelSecurity(tls);
				}
				SecureConversation sc = ss.getSecureConversation();
				if (sc != null) {
					secureConversationButton.setSelected(true);
					this.secureConversationPanel.setSecureConversation(sc);
				}
				SecureMessage sm = ss.getSecureMessage();
				if (sm != null) {
					secureMessageButton.setSelected(true);
					this.secureMessagePanel.setSecureMessage(sm);
				}
				this.synchClientCommunication();
				ClientCommunication cc = ss.getClientCommunication();
				if (cc != null) {
					clientCommunication.setSelectedItem(cc);
				}
				RunAsMode runas = ss.getRunAsMode();
				if (runas != null) {
					this.runAsMode.setSelectedItem(runas);
				}

				DelegationMode dm = ss.getDelegationMode();
				if (delegationMode != null) {
					this.delegationMode.setSelectedItem(dm);
				}
				AnonymousCommunication anon = ss.getAnonymousClients();
				if (anon != null) {
					anonymousCommunication.setSelectedItem(anon);
				}

				if (ss.getClientAuthorization() != null) {
					ClientAuthorization cli = ss.getClientAuthorization();
					if (cli.getNoAuthorization() != null) {
						this.clientAuth.setSelectedItem(NO_AUTHORIZATION);
					}

					if (cli.getSelfAuthorization() != null) {
						this.clientAuth.setSelectedItem(SELF_AUTHORIZATION);
					}
					if (cli.getHostAuthorization() != null) {
						this.clientAuth.setSelectedItem(HOST_AUTHORIZATION);
						hostAuthorization.setHostAuthorization(cli.getHostAuthorization());
					}

					if (cli.getIdentityAuthorization() != null) {
						this.clientAuth.setSelectedItem(IDENTITY_AUTHORIZATION);
						identityAuthorization.setIdentityAuthorization(cli.getIdentityAuthorization());
					}
				}
				ServiceCredential scred = ss.getServiceCredentials();
				if (scred != null) {
					this.setCredentials(scred.getX509Credential());
					this.setProxy(scred.getProxyCredential());
				}

				ServiceAuthorization sa = ss.getServiceAuthorization();
				if (sa != null) {
					if (sa.getGridMapAuthorization() != null) {
						String location = sa.getGridMapAuthorization().getGridMapFileLocation();
						if (location != null) {
							((GridMapPanel) gridmapPanel).setGridMapFile(location);
							this.authorizationMechanism.setSelectedItem(GRID_MAP_AUTHORIZATION);
						} else {
							PortalUtils.showErrorMessage("No GridMap file specified!!!");
							this.authorizationMechanism.setSelectedItem(NO_AUTHORIZATION);
						}
					} else {
						this.authorizationMechanism.setSelectedItem(NO_AUTHORIZATION);
					}
				}

				synchronize();
			}
		}

	}


	public void synchronize() {
		disableAll();
		if (customButton.isSelected()) {
			tlsButton.setEnabled(true);
			secureConversationButton.setEnabled(true);
			secureMessageButton.setEnabled(true);

			if (tlsButton.isSelected()) {
				tlsPanel.enablePanel();
			}
			if (secureConversationButton.isSelected()) {
				secureConversationPanel.enablePanel();
			}
			if (secureMessageButton.isSelected()) {
				secureMessagePanel.enablePanel();
			}

			synchClientCommunication();
			if (isSecure()) {
				clientCommunication.setEnabled(true);
			}
			if (isSecure()) {
				delegationMode.setEnabled(true);
				if ((usesTransportSecurity()) || (usesSecureMessage())) {
					delegationMode.setEnabled(false);
				}
			}

			synchRunAsMode();
			if (isSecure()) {
				runAsMode.setEnabled(true);
			}

			this.syncServiceCredentials();

			if (isSecure()) {
				clientAuth.setEnabled(true);
				hostAuthorization.enablePanel();
				identityAuthorization.enablePanel();
			}
			this.syncAuthorization();
			this.syncAnonymousCommunication();

		}
	}


	private void syncAnonymousCommunication() {
		if (isSecure()) {
			ClientCommunication comm = (ClientCommunication) clientCommunication.getSelectedItem();
			if (determineCommOk(comm)) {
				if ((this.getAuthorizationMechanism().isEnabled())
					&& (!this.getAuthorizationMechanism().getSelectedItem().equals(NO_AUTHORIZATION))) {
					anonymousCommunication.setEnabled(false);
				} else {
					anonymousCommunication.setEnabled(true);
				}
			}
		}
	}


	private boolean determineCommOk(ClientCommunication comm) {
		boolean commOk = false;
		if (comm != null) {
			if (comm.equals(ClientCommunication.Secure_Conversation)) {
				commOk = true;
			} else if (comm.equals(ClientCommunication.Transport_Layer_Security)) {
				commOk = true;
			}
		}
		return commOk;
	}


	public void disableAll() {
		tlsPanel.disablePanel();
		tlsButton.setEnabled(false);
		secureConversationButton.setEnabled(false);
		secureMessageButton.setEnabled(false);
		secureConversationPanel.disablePanel();
		secureMessagePanel.disablePanel();
		clientCommunication.setEnabled(false);
		runAsMode.setEnabled(false);
		delegationMode.setEnabled(false);
		anonymousCommunication.setEnabled(false);
		clientAuth.setEnabled(false);
		hostAuthorization.disablePanel();
		identityAuthorization.disablePanel();
		credentialLoadMethod.setEnabled(false);
		loadCredentialsButton.setEnabled(false);
		credentialPanelLayout.show(credentialsPanel, N0_CRED_PANEL);
		authorizationMechanism.setEnabled(false);
		authLayout.show(authPanel, NO_AUTHORIZATION);
	}


	private void syncServiceCredentials() {
		if ((secureConversationButton.isSelected()) || (secureMessageButton.isSelected())) {
			credentialLoadMethod.setEnabled(true);
			loadCredentialsButton.setEnabled(true);
			if ((this.certificateLocation != null) && (this.privateKeyLocation != null)) {
				credentialPanelLayout.show(credentialsPanel, PKI_CRED_PANEL);
			} else if (this.proxyLocation != null) {
				credentialPanelLayout.show(credentialsPanel, PROXY_CRED_PANEL);
			} else {
				credentialPanelLayout.show(credentialsPanel, N0_CRED_PANEL);
			}
		} else {
			credentialLoadMethod.setEnabled(false);
			credentialPanelLayout.show(credentialsPanel, N0_CRED_PANEL);
		}
	}


	private void syncAuthorization() {
		if (isSecure()) {
			authorizationMechanism.setEnabled(true);
			String mech = (String) this.authorizationMechanism.getSelectedItem();
			if (mech.equals(GRID_MAP_AUTHORIZATION)) {
				authLayout.show(authPanel, GRID_MAP_AUTHORIZATION);
			} else {
				authLayout.show(authPanel, NO_AUTHORIZATION);
			}
		} else {
			authorizationMechanism.setEnabled(false);
			authLayout.show(authPanel, NO_AUTHORIZATION);
		}
	}


	private synchronized void synchRunAsMode() {
		if (!isSyncingRunAs) {
			isSyncingRunAs = true;
			this.runAsMode.removeAllItems();
			if (isSecure()) {
				runAsMode.setEnabled(true);
				this.runAsMode.addItem(RunAsMode.System);
				if (hasServiceCredentials()) {
					this.runAsMode.addItem(RunAsMode.Service);
				}
				if (this.getDelegationMode().isEnabled()) {
					if ((this.getDelegationMode().getSelectedItem().equals(DelegationMode.Limited))
						|| (this.getDelegationMode().getSelectedItem().equals(DelegationMode.Full))) {

						if (!getAnonymousCommunication().isEnabled()) {
							this.runAsMode.addItem(RunAsMode.Caller);
						} else if (getAnonymousCommunication().getSelectedItem().equals(AnonymousCommunication.No)) {
							this.runAsMode.addItem(RunAsMode.Caller);
						}
					}
				}

			}
			isSyncingRunAs = false;
		}

	}


	private boolean hasServiceCredentials() {
		if ((this.certificateLocation != null) && (this.privateKeyLocation != null)) {
			return true;
		} else if (this.proxyLocation != null) {
			return true;
		} else {
			return false;
		}
	}


	private synchronized void synchClientCommunication() {
		if (!syncingComm) {
			syncingComm = true;
			Object obj = clientCommunication.getSelectedItem();
			clientCommunication.removeAllItems();
			if (isSecure()) {
				clientCommunication.setEnabled(true);

				if (tlsButton.isSelected()) {
					clientCommunication.addItem(ClientCommunication.Transport_Layer_Security);
				}
				if (secureConversationButton.isSelected()) {
					clientCommunication.addItem(ClientCommunication.Secure_Conversation);
				}
				if (secureMessageButton.isSelected()) {
					clientCommunication.addItem(ClientCommunication.Secure_Message);
				}

				if (obj != null) {
					clientCommunication.setSelectedItem(obj);
				}
			}
			syncingComm = false;
		}
	}


	/**
	 * This method initializes tlsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTlsPanel() {
		if (tlsPanel == null) {
			tlsPanel = new TransportLevelSecurityPanel();
		}
		return tlsPanel;
	}


	/**
	 * This method initializes tlsButton
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getTlsButton() {
		if (tlsButton == null) {
			tlsButton = new JCheckBox();
			tlsButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					synchronize();
				}
			});
		}
		return tlsButton;
	}


	/**
	 * This method initializes secureConversationButton
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getSecureConversationButton() {
		if (secureConversationButton == null) {
			secureConversationButton = new JCheckBox();
			secureConversationButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					synchronize();
				}
			});

		}
		return secureConversationButton;
	}


	/**
	 * This method initializes choicePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getChoicePanel() {
		if (choicePanel == null) {
			Custom = new JLabel();
			Custom.setText("Custom");
			jLabel1 = new JLabel();
			jLabel1.setText("None");
			choicePanel = new JPanel();
			choicePanel.add(getNoneButton());
			choicePanel.add(jLabel1);
			choicePanel.add(getCustomButton());
			choicePanel.add(Custom);
		}
		return choicePanel;
	}


	/**
	 * This method initializes commPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCommPanel() {
		if (commPanel == null) {
			jLabel4 = new JLabel();
			jLabel4.setText("Secure Message");
			jLabel3 = new JLabel();
			jLabel3.setText("Secure Conversation");
			jLabel2 = new JLabel();
			jLabel2.setText("Transport Level Security");
			commPanel = new JPanel();
			commPanel.add(getTlsButton(), null);
			commPanel.add(jLabel2, null);
			commPanel.add(getSecureConversationButton(), null);
			commPanel.add(jLabel3, null);
			commPanel.add(getSecureMessageButton(), null);
			commPanel.add(jLabel4, null);
		}
		return commPanel;
	}


	/**
	 * This method initializes secureMessageButton
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getSecureMessageButton() {
		if (secureMessageButton == null) {
			secureMessageButton = new JCheckBox();
			secureMessageButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					synchronize();
				}
			});
		}
		return secureMessageButton;
	}


	/**
	 * This method initializes secureConversationPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSecureConversationPanel() {
		if (secureConversationPanel == null) {
			secureConversationPanel = new SecureConversationPanel();
		}
		return secureConversationPanel;
	}


	/**
	 * This method initializes secureMessagePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSecureMessagePanel() {
		if (secureMessagePanel == null) {
			secureMessagePanel = new SecureMessagePanel();
		}
		return secureMessagePanel;
	}


	/**
	 * This method initializes clientCommunication
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getClientCommunication() {
		if (clientCommunication == null) {
			clientCommunication = new JComboBox();
			clientCommunication.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					synchronize();
				}
			});
		}
		return clientCommunication;
	}


	/**
	 * This method initializes runAsMode
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getRunAsMode() {
		if (runAsMode == null) {
			runAsMode = new JComboBox();
		}
		return runAsMode;
	}


	/**
	 * This method initializes delegationMode
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getDelegationMode() {
		if (delegationMode == null) {
			delegationMode = new JComboBox();
			delegationMode.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (isInited) {
						synchronize();
					}
				}
			});
			delegationMode.addItem(DelegationMode.None);
			delegationMode.addItem(DelegationMode.Limited);
			delegationMode.addItem(DelegationMode.Full);
		}
		return delegationMode;
	}


	/**
	 * This method initializes anonymousCommunication
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getAnonymousCommunication() {
		if (anonymousCommunication == null) {
			anonymousCommunication = new JComboBox();
			anonymousCommunication.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (isInited) {
						synchronize();
					}
				}
			});
			anonymousCommunication.addItem(AnonymousCommunication.No);
			anonymousCommunication.addItem(AnonymousCommunication.Yes);
		}
		return anonymousCommunication;
	}


	/**
	 * This method initializes transportPanel
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getTransportPanel() {
		if (transportPanel == null) {
			transportPanel = new JTabbedPane();
			transportPanel.addTab("Secure Communication", null, getCommunicationPanel(), null);
			transportPanel.addTab("Authorization", null, getAuthorizationPanel(), null);
			transportPanel.addTab("Client", null, getClientPanel(), null);
			transportPanel.addTab("Service Credentials", null, getServiceCredentials(), null);
		}
		return transportPanel;
	}


	/**
	 * This method initializes clientAuthorization
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getClientAuthorization() {
		if (clientAuthorization == null) {
			clientAuthorization = new JPanel();
			clientAuthorization.setLayout(clientAuthLayout);
			clientAuthorization.add(getHostAuthorization(), getHostAuthorization().getName());
			clientAuthorization.add(getIdentityAuthorization(), getIdentityAuthorization().getName());
			clientAuthorization.add(getBlankPanel(), getBlankPanel().getName());
		}
		return clientAuthorization;
	}


	/**
	 * This method initializes hostAuthorization
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getHostAuthorization() {
		if (hostAuthorization == null) {
			hostAuthorization = new HostAuthorizationPanel();
			hostAuthorization.setName(HOST_AUTHORIZATION);
		}
		return hostAuthorization;
	}


	/**
	 * This method initializes identityAuthorization
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getIdentityAuthorization() {
		if (identityAuthorization == null) {
			identityAuthorization = new IdentityAuthorizationPanel();
			identityAuthorization.setName(IDENTITY_AUTHORIZATION);
		}
		return identityAuthorization;
	}


	/**
	 * This method initializes blankPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getBlankPanel() {
		if (blankPanel == null) {
			blankPanel = new JPanel();
			blankPanel.setName(NO_AUTHORIZATION);
		}
		return blankPanel;
	}


	/**
	 * This method initializes clientAuth
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getClientAuth() {
		if (clientAuth == null) {
			clientAuth = new JComboBox();
			clientAuth.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (clientAuth.getSelectedItem().equals(NO_AUTHORIZATION)) {
						clientAuthLayout.show(clientAuthorization, NO_AUTHORIZATION);
					}

					if (clientAuth.getSelectedItem().equals(SELF_AUTHORIZATION)) {
						clientAuthLayout.show(clientAuthorization, NO_AUTHORIZATION);
					}
					if (clientAuth.getSelectedItem().equals(HOST_AUTHORIZATION)) {
						clientAuthLayout.show(clientAuthorization, HOST_AUTHORIZATION);
					}
					if (clientAuth.getSelectedItem().equals(IDENTITY_AUTHORIZATION)) {
						clientAuthLayout.show(clientAuthorization, IDENTITY_AUTHORIZATION);
					}
				}
			});
			clientAuth.addItem(NO_AUTHORIZATION);
			clientAuth.addItem(HOST_AUTHORIZATION);
			clientAuth.addItem(IDENTITY_AUTHORIZATION);
			clientAuth.addItem(SELF_AUTHORIZATION);
		}
		return clientAuth;
	}


	/**
	 * This method initializes communicationPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCommunicationPanel() {
		if (communicationPanel == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints11.gridy = 3;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.gridx = 0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints7.gridy = 2;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.weighty = 1.0D;
			gridBagConstraints7.gridx = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.weighty = 1.0D;
			gridBagConstraints6.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridx = 0;
			communicationPanel = new JPanel();
			communicationPanel.setLayout(new GridBagLayout());
			communicationPanel.add(getCommPanel(), gridBagConstraints4);
			communicationPanel.add(getTlsPanel(), gridBagConstraints6);
			communicationPanel.add(getSecureConversationPanel(), gridBagConstraints7);
			communicationPanel.add(getSecureMessagePanel(), gridBagConstraints11);
		}
		return communicationPanel;
	}


	/**
	 * This method initializes clientPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getClientPanel() {
		if (clientPanel == null) {
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.insets = new java.awt.Insets(10, 10, 10, 10);
			gridBagConstraints18.weightx = 1.0D;
			gridBagConstraints18.weighty = 1.0D;
			gridBagConstraints18.gridwidth = 2;
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.gridy = 4;
			gridBagConstraints18.fill = java.awt.GridBagConstraints.BOTH;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.anchor = GridBagConstraints.WEST;
			gridBagConstraints19.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints19.gridx = 1;
			gridBagConstraints19.gridy = 3;
			gridBagConstraints19.weightx = 1.0;
			gridBagConstraints19.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints13.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints13.gridy = 3;
			jLabel7 = new JLabel();
			jLabel7.setText("Client Authorization");
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints14.gridx = 1;
			gridBagConstraints14.gridy = 2;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints15.gridy = 2;
			dele = new JLabel();
			dele.setText("Delegation Mode");
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.gridx = 1;
			gridBagConstraints16.gridy = 1;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints8.gridy = 1;
			jLabel5 = new JLabel();
			jLabel5.setText("Anonymous Communication");
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints20.gridy = 0;
			jLabel9 = new JLabel();
			jLabel9.setText("Client Communication");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
			clientPanel = new JPanel();
			clientPanel.setLayout(new GridBagLayout());
			clientPanel.add(getClientCommunication(), gridBagConstraints9);
			clientPanel.add(jLabel9, gridBagConstraints20);
			clientPanel.add(jLabel5, gridBagConstraints8);
			clientPanel.add(getAnonymousCommunication(), gridBagConstraints16);
			clientPanel.add(dele, gridBagConstraints15);
			clientPanel.add(getDelegationMode(), gridBagConstraints14);
			clientPanel.add(jLabel7, gridBagConstraints13);

			clientPanel.add(getClientAuthorization(), gridBagConstraints18);
			clientPanel.add(getClientAuth(), gridBagConstraints19);
		}
		return clientPanel;
	}


	/**
	 * This method initializes generalSecurity
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGeneralSecurity() {
		if (generalSecurity == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints12.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 0;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints21.gridy = 0;
			gridBagConstraints21.gridx = 0;
			jLabel = new JLabel();
			jLabel.setText("Run As");
			generalSecurity = new JPanel();
			generalSecurity.setLayout(new GridBagLayout());
			generalSecurity.add(jLabel, gridBagConstraints21);
			generalSecurity.add(getRunAsMode(), gridBagConstraints12);
		}
		return generalSecurity;
	}


	/**
	 * This method initializes serviceCredentials
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getServiceCredentials() {
		if (serviceCredentials == null) {
			serviceCredentials = new JPanel();
			serviceCredentials.setLayout(new BorderLayout());
			serviceCredentials.add(getCredentialsPanel(), java.awt.BorderLayout.CENTER);
			serviceCredentials.add(getJPanel(), java.awt.BorderLayout.NORTH);
		}
		return serviceCredentials;
	}


	/**
	 * This method initializes selectPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSelectPanel() {
		if (selectPanel == null) {
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints24.gridy = 0;
			gridBagConstraints24.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints24.gridx = 2;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints23.gridx = 1;
			gridBagConstraints23.gridy = 0;
			gridBagConstraints23.weightx = 1.0;
			gridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints23.insets = new java.awt.Insets(5, 5, 5, 5);
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints22.gridy = 0;
			gridBagConstraints22.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints22.gridx = 0;
			jLabel6 = new JLabel();
			jLabel6.setText("Import Credentials");
			jLabel6.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 14));
			jLabel6.setForeground(IntroduceLookAndFeel.getPanelLabelColor());
			selectPanel = new JPanel();

			selectPanel.setLayout(new GridBagLayout());
			selectPanel.add(jLabel6, gridBagConstraints22);
			selectPanel.add(getCredentialLoadMethod(), gridBagConstraints23);
			selectPanel.add(getLoadCredentialsButton(), gridBagConstraints24);
		}
		return selectPanel;
	}


	/**
	 * This method initializes credentialLoadMethod
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCredentialLoadMethod() {
		if (credentialLoadMethod == null) {
			credentialLoadMethod = new JComboBox();
			credentialLoadMethod.addItem(FILE_SYSTEM_CERT_KEY);
			credentialLoadMethod.addItem(FILE_SYSTEM_PROXY);
		}
		return credentialLoadMethod;
	}


	/**
	 * This method initializes loadCredentialsButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getLoadCredentialsButton() {
		if (loadCredentialsButton == null) {
			loadCredentialsButton = new JButton();
			loadCredentialsButton.setText("Import");
			loadCredentialsButton.setIcon(IntroduceLookAndFeel.getLoadCredentialsIcon());
			loadCredentialsButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					loadCredentials();
				}
			});
		}
		return loadCredentialsButton;
	}


	private void loadCredentials() {
		String method = (String) credentialLoadMethod.getSelectedItem();
		if (method.equals(FILE_SYSTEM_CERT_KEY)) {
			PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
				new LoadCredentialsFromFileSystemWindow(this), 500, 200);
		}
		if (method.equals(FILE_SYSTEM_PROXY)) {
			PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
				new LoadProxyFromFileSystemWindow(this), 500, 200);
		}

	}


	public void setProxy(ProxyCredential proxy) throws Exception {
		if (proxy != null) {
			this.certificateLocation = null;
			this.privateKeyLocation = null;
			this.proxyLocation = proxy.getProxyLocation();
			try {
				this.proxyPanel.clearProxy();
				this.proxyPanel.showProxy(ProxyUtil.loadProxy(this.proxyLocation));
			} catch (Exception e) {
				PortalUtils.showErrorMessage("Invalid proxy specified!!!");
			}
			syncServiceCredentials();
			synchRunAsMode();
		}

	}


	public void setCredentials(X509Credential cred) throws Exception {
		if (cred != null) {
			this.certificateLocation = cred.getCertificateLocation();
			this.privateKeyLocation = cred.getPrivateKeyLocation();
			this.proxyLocation = null;
			try {
				this.certificatePanel.setCertificate(CertUtil.loadCertificate(new File(cred.getCertificateLocation())));
			} catch (Exception e) {
				PortalUtils.showErrorMessage("Invalid certificate specified!!!");
			}
			syncServiceCredentials();
			synchRunAsMode();
		}
	}


	/**
	 * This method initializes credentialsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCredentialsPanel() {
		if (credentialsPanel == null) {
			credentialsPanel = new JPanel();
			this.credentialPanelLayout = new CardLayout();
			credentialsPanel.setLayout(this.credentialPanelLayout);
			credentialsPanel.add(getNonePanel(), N0_CRED_PANEL);
			credentialsPanel.add(getCertificatePanel(), PKI_CRED_PANEL);
			credentialsPanel.add(getProxyPanel(), PROXY_CRED_PANEL);
		}
		return credentialsPanel;
	}


	/**
	 * This method initializes nonePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getNonePanel() {
		if (nonePanel == null) {
			nonePanel = new JPanel();
			nonePanel.setLayout(new BorderLayout());
			nonePanel.setName(N0_CRED_PANEL);
		}
		return nonePanel;
	}


	/**
	 * This method initializes certificatePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private CertificatePanel getCertificatePanel() {
		if (certificatePanel == null) {
			certificatePanel = new CertificatePanel();
			certificatePanel.setName(PKI_CRED_PANEL);
			certificatePanel.setAllowExport(false);
			certificatePanel.setAllowImport(false);

		}
		return certificatePanel;
	}


	/**
	 * This method initializes proxyPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private ProxyPanel getProxyPanel() {
		if (proxyPanel == null) {
			proxyPanel = new ProxyPanel();
		}
		return proxyPanel;
	}


	/**
	 * This method initializes authorizationPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAuthorizationPanel() {
		if (authorizationPanel == null) {
			authorizationPanel = new JPanel();
			authorizationPanel.setLayout(new BorderLayout());
			authorizationPanel.add(getAuthorizationTypePanel(), java.awt.BorderLayout.NORTH);
			authorizationPanel.add(getAuthPanel(), java.awt.BorderLayout.CENTER);
		}
		return authorizationPanel;
	}


	/**
	 * This method initializes authorizationTypePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAuthorizationTypePanel() {
		if (authorizationTypePanel == null) {
			authLabel = new JLabel();
			authLabel.setText("Authorization Mechanism");
			authLabel.setForeground(IntroduceLookAndFeel.getPanelLabelColor());
			authLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 14));
			authorizationTypePanel = new JPanel();
			authorizationTypePanel.setLayout(new FlowLayout());
			authorizationTypePanel.add(authLabel, null);
			authorizationTypePanel.add(getAuthorizationMechanism(), null);
		}
		return authorizationTypePanel;
	}


	/**
	 * This method initializes authorizationMechanism
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getAuthorizationMechanism() {
		if (authorizationMechanism == null) {
			authorizationMechanism = new JComboBox();
			authorizationMechanism.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (isInited) {
						synchronize();
					}
				}
			});
			authorizationMechanism.addItem(NO_AUTHORIZATION);
			authorizationMechanism.addItem(GRID_MAP_AUTHORIZATION);
		}
		return authorizationMechanism;
	}


	/**
	 * This method initializes authPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAuthPanel() {
		if (authPanel == null) {
			authPanel = new JPanel();
			authLayout = new CardLayout();
			authPanel.setLayout(authLayout);
			authPanel.add(getNoAuthorizationPanel(), getNoAuthorizationPanel().getName());
			authPanel.add(getGridmapPanel(), getGridmapPanel().getName());
		}
		return authPanel;
	}


	/**
	 * This method initializes noAuthorizationPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getNoAuthorizationPanel() {
		if (noAuthorizationPanel == null) {
			noAuthorizationPanel = new JPanel();
			noAuthorizationPanel.setName(NO_AUTHORIZATION);
		}
		return noAuthorizationPanel;
	}


	/**
	 * This method initializes gridmapPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGridmapPanel() {
		if (gridmapPanel == null) {
			gridmapPanel = new GridMapPanel();
			gridmapPanel.setName(GRID_MAP_AUTHORIZATION);
		}
		return gridmapPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.insets = new java.awt.Insets(2, 5, 2, 5);
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.weightx = 1.0D;
			gridBagConstraints10.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.gridy = 1;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getSelectPanel(), gridBagConstraints10);
			jPanel.add(getGeneralSecurity(), gridBagConstraints);
		}
		return jPanel;
	}

}
