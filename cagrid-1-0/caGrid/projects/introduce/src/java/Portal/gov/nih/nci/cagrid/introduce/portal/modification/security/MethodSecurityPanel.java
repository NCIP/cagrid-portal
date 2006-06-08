package gov.nih.nci.cagrid.introduce.portal.modification.security;

import gov.nih.nci.cagrid.introduce.beans.security.AnonymousCommunication;
import gov.nih.nci.cagrid.introduce.beans.security.ClientAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.ClientCommunication;
import gov.nih.nci.cagrid.introduce.beans.security.DelegationMode;
import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.NoAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.ProxyCredential;
import gov.nih.nci.cagrid.introduce.beans.security.RunAsMode;
import gov.nih.nci.cagrid.introduce.beans.security.SecureConversation;
import gov.nih.nci.cagrid.introduce.beans.security.SecureMessage;
import gov.nih.nci.cagrid.introduce.beans.security.SecuritySetting;
import gov.nih.nci.cagrid.introduce.beans.security.SelfAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceCredential;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.TransportLevelSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.X509Credential;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class MethodSecurityPanel extends JPanel implements PanelSynchronizer {

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
	private ServiceSecurity serviceSecurity;
	private boolean syncingComm = false;
	private JComboBox anonymousCommunication = null;
	private JTabbedPane transportPanel = null;
	private JPanel clientAuthorization = null;
	private HostAuthorizationPanel hostAuthorization = null;
	private CardLayout clientAuthLayout = new CardLayout();
	private IdentityAuthorizationPanel identityAuthorization = null;
	private JPanel blankPanel = null;
	private JComboBox clientAuth = null;

	private final static String NO_AUTHORIZATION = "No Authorization";
	private final static String HOST_AUTHORIZATION = "Host Authorization";
	private final static String IDENTITY_AUTHORIZATION = "Identity Authorization";
	private final static String SELF_AUTHORIZATION = "Self Authorization";
	private JPanel communicationPanel = null;
	private JPanel clientPanel = null;
	private JLabel jLabel9 = null;
	private JLabel jLabel5 = null;
	private JLabel dele = null;
	private JLabel jLabel7 = null;
	private JPanel generalSecurity = null;
	private JLabel jLabel = null;
	private boolean isSyncingRunAs = false;
	private boolean isInited = false;


	public MethodSecurityPanel(ServiceSecurity sec) {
		super();
		this.serviceSecurity = sec;
		initialize();
	}


	public MethodSecurityPanel(ServiceSecurity sec, MethodSecurity ms) {
		super();
		this.serviceSecurity = sec;
		initialize();
		setMethodSecurity(ms);
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
		applyServiceSettings();

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
		} else if ((serviceSecurity != null) && (serviceSecurity.getTransportLevelSecurity() != null)) {
			return true;
		} else {
			return false;
		}
	}


	private boolean usesSecureConversation() {
		if (secureConversationButton.isSelected()) {
			return true;
		} else if ((serviceSecurity != null) && (serviceSecurity.getSecureConversation() != null)) {
			return true;
		} else {
			return false;
		}
	}


	private boolean usesSecureMessage() {
		if (secureMessageButton.isSelected()) {
			return true;
		} else if ((serviceSecurity != null) && (serviceSecurity.getSecureMessage() != null)) {
			return true;
		} else {
			return false;
		}
	}


	public MethodSecurity getMethodSecurity() throws Exception {
		MethodSecurity ms = new MethodSecurity();
		if (noneButton.isSelected()) {
			if (this.serviceSecurity == null) {
				return null;
			} else {
				ms.setSecuritySetting(SecuritySetting.None);
			}
		} else if (customButton.isSelected()) {
			ms.setSecuritySetting(SecuritySetting.Custom);
			if (!isSecure()) {
				throw new Exception("You must select at least one transport mechanism!!!");
			}

			if (tlsButton.isSelected()) {
				ms.setTransportLevelSecurity(this.tlsPanel.getTransportLevelSecurity());

			}
			if (secureConversationButton.isSelected()) {
				ms.setSecureConversation(this.secureConversationPanel.getSecureConversation());

			}
			if (secureMessageButton.isSelected()) {
				ms.setSecureMessage(this.secureMessagePanel.getSecureMessage());
			}
			if (clientCommunication.isEnabled()) {
				ms.setClientCommunication((ClientCommunication) clientCommunication.getSelectedItem());
			}
			if (runAsMode.isEnabled()) {
				ms.setRunAsMode((RunAsMode) runAsMode.getSelectedItem());
			}
			if (delegationMode.isEnabled()) {
				ms.setDelegationMode((DelegationMode) delegationMode.getSelectedItem());
			} else {
				ms.setDelegationMode(DelegationMode.None);
			}
			if (anonymousCommunication.isEnabled()) {
				ms.setAnonymousClients((AnonymousCommunication) anonymousCommunication.getSelectedItem());
			} else {
				ms.setAnonymousClients(AnonymousCommunication.No);
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
			ms.setClientAuthorization(cli);

		}
		if (CommonTools.equals(serviceSecurity, ms)) {
			return null;
		}
		return ms;
	}


	public void setMethodSecurity(MethodSecurity ms) {
		if (ms != null) {
			if (ms.getSecuritySetting().equals(SecuritySetting.None)) {
				noneButton.setSelected(true);
			} else if (ms.getSecuritySetting().equals(SecuritySetting.Custom)) {
				customButton.setSelected(true);

				TransportLevelSecurity tls = ms.getTransportLevelSecurity();
				if (tls != null) {
					tlsButton.setSelected(true);
					this.tlsPanel.setTransportLevelSecurity(tls);
				}
				SecureConversation sc = ms.getSecureConversation();
				if (sc != null) {
					secureConversationButton.setSelected(true);
					this.secureConversationPanel.setSecureConversation(sc);
				}
				SecureMessage sm = ms.getSecureMessage();
				if (sm != null) {
					secureMessageButton.setSelected(true);
					this.secureMessagePanel.setSecureMessage(sm);
				}
				this.synchClientCommunication();
				ClientCommunication cc = ms.getClientCommunication();
				if (cc != null) {
					clientCommunication.setSelectedItem(cc);
				}
				RunAsMode runas = ms.getRunAsMode();
				if (runas != null) {
					this.runAsMode.setSelectedItem(runas);
				}

				DelegationMode dm = ms.getDelegationMode();
				if (delegationMode != null) {
					this.delegationMode.setSelectedItem(dm);
				}
				AnonymousCommunication anon = ms.getAnonymousClients();
				if (anon != null) {
					anonymousCommunication.setSelectedItem(anon);
				}

				if (ms.getClientAuthorization() != null) {
					ClientAuthorization cli = ms.getClientAuthorization();
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
			}
		}
		synchronize();

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

			syncAnonymousCommunication();

			if (isSecure()) {
				clientAuth.setEnabled(true);
				hostAuthorization.enablePanel();
				identityAuthorization.enablePanel();
			}

		}
	}


	private void syncAnonymousCommunication() {

		if (isSecure()) {
			ClientCommunication comm = (ClientCommunication) clientCommunication.getSelectedItem();
			if (determineCommOk(comm)) {
				// TODO: Check Service Auth
				if (this.serviceSecurity == null) {
					anonymousCommunication.setEnabled(true);
				} else if (this.serviceSecurity.getServiceAuthorization() == null) {
					anonymousCommunication.setEnabled(true);
				} else if (this.serviceSecurity.getServiceAuthorization().getNoAuthorization() != null) {
					anonymousCommunication.setEnabled(true);
				} else {
					anonymousCommunication.setEnabled(false);
				}
			}
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
		if (serviceSecurity != null) {
			if (serviceSecurity.getServiceCredentials() != null) {
				ServiceCredential cred = serviceSecurity.getServiceCredentials();
				if (cred.getX509Credential() != null) {
					X509Credential x509 = cred.getX509Credential();
					if ((x509.getCertificateLocation() != null) && (x509.getPrivateKeyLocation() != null)) {
						return true;
					}
				} else if (cred.getProxyCredential() != null) {
					ProxyCredential proxy = cred.getProxyCredential();
					if (proxy.getProxyLocation() != null) {
						return true;
					}
				}

			}
		}
		return false;
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


	public void applyServiceSettings() {
		if (this.serviceSecurity != null) {
			if (this.isSecure()) {
				this.customButton.setSelected(true);
				if (this.serviceSecurity.getTransportLevelSecurity() != null) {
					TransportLevelSecurity s = this.serviceSecurity.getTransportLevelSecurity();
					this.tlsButton.setSelected(true);
					tlsPanel.setTransportLevelSecurity(s);
				}

				if (this.serviceSecurity.getSecureConversation() != null) {
					SecureConversation s = this.serviceSecurity.getSecureConversation();
					this.secureConversationButton.setSelected(true);
					this.secureConversationPanel.setSecureConversation(s);
				}

				if (this.serviceSecurity.getSecureMessage() != null) {
					SecureMessage s = this.serviceSecurity.getSecureMessage();
					this.secureMessageButton.setSelected(true);
					this.secureMessagePanel.setSecureMessage(s);
				}
				if (this.serviceSecurity.getRunAsMode() != null) {
					this.runAsMode.setSelectedItem(this.serviceSecurity.getRunAsMode());
				}

				if (this.serviceSecurity.getDelegationMode() != null) {
					this.delegationMode.setSelectedItem(this.serviceSecurity.getDelegationMode());
				}

				if (this.serviceSecurity.getClientCommunication() != null) {
					this.synchClientCommunication();
					this.clientCommunication.setSelectedItem(this.serviceSecurity.getClientCommunication());
				}
				if (this.serviceSecurity.getAnonymousClients() != null) {
					this.anonymousCommunication.setSelectedItem(this.serviceSecurity.getAnonymousClients());
				}

				if (this.serviceSecurity.getClientAuthorization() != null) {
					ClientAuthorization cli = this.serviceSecurity.getClientAuthorization();
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

			} else {
				this.noneButton.setSelected(true);
			}
		}
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
	}


	private synchronized void synchClientCommunication() {
		if (!syncingComm) {
			syncingComm = true;
			Object obj = clientCommunication.getSelectedItem();
			clientCommunication.removeAllItems();
			if (isSecure()) {
				clientCommunication.setEnabled(true);

				if ((tlsButton.isSelected())
					|| ((serviceSecurity != null) && (serviceSecurity.getTransportLevelSecurity() != null))) {
					clientCommunication.addItem(ClientCommunication.Transport_Layer_Security);
				}
				if ((secureConversationButton.isSelected())
					|| ((serviceSecurity != null) && (serviceSecurity.getSecureConversation() != null))) {
					clientCommunication.addItem(ClientCommunication.Secure_Conversation);
				}
				if ((secureMessageButton.isSelected())
					|| ((serviceSecurity != null) && (serviceSecurity.getSecureMessage() != null))) {
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
			choicePanel.setLayout(new GridBagLayout());
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
			runAsMode.addItem(RunAsMode.System);
			runAsMode.addItem(RunAsMode.Service);
			runAsMode.addItem(RunAsMode.Caller);
			runAsMode.addItem(RunAsMode.Resource);
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
			transportPanel.addTab("Client", null, getClientPanel(), null);
			transportPanel.addTab("Other", null, getGeneralSecurity(), null);
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
			gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 0;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
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

}
