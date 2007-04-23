package gov.nih.nci.cagrid.introduce.portal.modification.security;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipExpression;
import gov.nih.nci.cagrid.gridgrouper.ui.expressioneditor.GridGrouperExpressionEditor;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.security.AnonymousCommunication;
import gov.nih.nci.cagrid.introduce.beans.security.MethodAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.NoAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.ProxyCredential;
import gov.nih.nci.cagrid.introduce.beans.security.RunAsMode;
import gov.nih.nci.cagrid.introduce.beans.security.SecureConversation;
import gov.nih.nci.cagrid.introduce.beans.security.SecureMessage;
import gov.nih.nci.cagrid.introduce.beans.security.SecuritySetting;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceCredential;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.TransportLevelSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.X509Credential;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
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

	private final static String NO_AUTHORIZATION = "No Authorization";

	private final static String GRID_GROUPER_AUTHORIZATION = "Grid Grouper";

	private final static String CSM_AUTHORIZATION = "Common Security Module (CSM)";

	private JPanel secureCommunicationPanel = null;

	private ButtonGroup buttonGroup = new ButtonGroup();

	private JRadioButton noneButton = null;

	private JRadioButton customButton = null;

	private TransportLayerSecurityPanel tlsPanel = null;

	private JCheckBox tlsButton = null;

	private JCheckBox secureConversationButton = null;

	private JPanel choicePanel = null;

	private JPanel commPanel = null;

	private JLabel noneLabel = null;

	private JLabel customLabel = null;

	private JLabel transportLayerSecurityLabel = null;

	private JLabel secureConversationLabel = null;

	private JCheckBox secureMessageButton = null;

	private JLabel secureMessageLabel = null;

	private SecureConversationPanel secureConversationPanel = null;

	private SecureMessagePanel secureMessagePanel = null;

	private JComboBox runAsMode = null;

	private ServiceSecurity serviceSecurity;

	private JComboBox anonymousCommunication = null;

	private JTabbedPane transportPanel = null;

	private JPanel communicationPanel = null;

	private JPanel generalSecurity = null;

	private JLabel runAsLabel = null;

	private boolean isSyncingRunAs = false;

	private boolean isInited = false;

	private JLabel anonClientsLabel = null;

	private JPanel authorizationPanel = null;

	private JPanel authorizationTypePanel = null;

	private JPanel jPanel1 = null;

	private JLabel authLabel = null;

	private JComboBox authorizationMechanism = null;

	private JPanel authPanel = null;

	private JPanel noAuthorizationPanel = null;

	private CardLayout authLayout;

	private ServiceType service;

	private MethodType method;

	private GridGrouperExpressionEditor gridGrouper = null;

	private CSMPanel csmPanel = null;

	private ServiceDescription description;


	public MethodSecurityPanel(ServiceDescription description, ServiceType service, MethodType method) {
		super();
		this.description = description;
		this.service = service;
		serviceSecurity = this.service.getServiceSecurity();
		this.method = method;
		initialize();
		if (this.method.getMethodSecurity() != null) {
			setMethodSecurity(this.method.getMethodSecurity());
		}
	}


	private void initialize() {
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
			null, PortalLookAndFeel.getPanelLabelColor()));
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


	public MethodSecurity getMethodSecurity(String methodName) throws Exception {
		MethodSecurity ms = new MethodSecurity();
		if (noneButton.isSelected()) {
			if (serviceSecurity == null) {
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
				ms.setTransportLevelSecurity(tlsPanel.getTransportLevelSecurity());

			}
			if (secureConversationButton.isSelected()) {
				ms.setSecureConversation(secureConversationPanel.getSecureConversation());

			}
			if (secureMessageButton.isSelected()) {
				ms.setSecureMessage(secureMessagePanel.getSecureMessage());
			}

			if (runAsMode.isEnabled()) {
				ms.setRunAsMode((RunAsMode) runAsMode.getSelectedItem());
			}

			if (anonymousCommunication.isEnabled()) {
				ms.setAnonymousClients((AnonymousCommunication) anonymousCommunication.getSelectedItem());
			} else {
				ms.setAnonymousClients(AnonymousCommunication.No);
			}
			String authType = (String) authorizationMechanism.getSelectedItem();
			MethodAuthorization ma = new MethodAuthorization();
			if (authType.equals(GRID_GROUPER_AUTHORIZATION)) {
				// TODO: Validate the expression
				MembershipExpression exp = getGridGrouper().getMembershipExpression();
				ma.setGridGrouperAuthorization(exp);
			} else if (authType.equals(CSM_AUTHORIZATION)) {
				CommonTools.setServiceProperty(description, CSMPanel.CSM_CONFIGURATION_FILE, "", false);
				ma.setCSMAuthorization(getCsmPanel().getAuthorization());
			} else {
				ma.setNoAuthorization(new NoAuthorization());
			}
			ms.setMethodAuthorization(ma);
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
					tlsPanel.setTransportLevelSecurity(tls);
				}
				SecureConversation sc = ms.getSecureConversation();
				if (sc != null) {
					secureConversationButton.setSelected(true);
					secureConversationPanel.setSecureConversation(sc);
				}
				SecureMessage sm = ms.getSecureMessage();
				if (sm != null) {
					secureMessageButton.setSelected(true);
					secureMessagePanel.setSecureMessage(sm);
				}

				RunAsMode runas = ms.getRunAsMode();
				if (runas != null) {
					runAsMode.setSelectedItem(runas);
				}

				AnonymousCommunication anon = ms.getAnonymousClients();
				if (anon != null) {
					anonymousCommunication.setSelectedItem(anon);
				}

				MethodAuthorization ma = ms.getMethodAuthorization();
				if (ma != null) {
					if (ma.getGridGrouperAuthorization() != null) {
						this.getGridGrouper().setExpression(ma.getGridGrouperAuthorization());
						authorizationMechanism.setSelectedItem(GRID_GROUPER_AUTHORIZATION);
					} else if (ma.getCSMAuthorization() != null) {
						this.getCsmPanel().setAuthorization(ma.getCSMAuthorization());
						authorizationMechanism.setSelectedItem(CSM_AUTHORIZATION);
					} else {
						authorizationMechanism.setSelectedItem(NO_AUTHORIZATION);
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

			synchRunAsMode();
			syncAuthorization();
			syncAnonymousCommunication();
		}
	}


	private void syncAuthorization() {
		if (isSecure()) {
			authorizationMechanism.setEnabled(true);
			String mech = (String) authorizationMechanism.getSelectedItem();
			authLayout.show(authPanel, mech);
		} else {
			authorizationMechanism.setEnabled(false);
			authLayout.show(authPanel, NO_AUTHORIZATION);
		}
	}


	private void syncAnonymousCommunication() {
		if (usesTransportSecurity() || usesSecureConversation()) {
			if (serviceSecurity == null) {
				anonymousCommunication.setEnabled(true);
			} else if (serviceSecurity.getServiceAuthorization() == null) {
				anonymousCommunication.setEnabled(true);
			} else if (serviceSecurity.getServiceAuthorization().getNoAuthorization() != null) {
				anonymousCommunication.setEnabled(true);
			} else {
				anonymousCommunication.setEnabled(false);
			}
		} else {
			anonymousCommunication.setEnabled(false);
		}
	}


	private synchronized void synchRunAsMode() {
		if (!isSyncingRunAs) {
			isSyncingRunAs = true;
			runAsMode.removeAllItems();
			if (isSecure()) {
				runAsMode.setEnabled(true);
				runAsMode.addItem(RunAsMode.System);
				if (hasServiceCredentials()) {
					runAsMode.addItem(RunAsMode.Service);
				}

				if (!getAnonymousCommunication().isEnabled()) {
					runAsMode.addItem(RunAsMode.Caller);
				} else if (getAnonymousCommunication().getSelectedItem().equals(AnonymousCommunication.No)) {
					runAsMode.addItem(RunAsMode.Caller);
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


	public void applyServiceSettings() {
		if (serviceSecurity != null) {
			if (this.isSecure()) {
				customButton.setSelected(true);
				if (serviceSecurity.getTransportLevelSecurity() != null) {
					TransportLevelSecurity s = serviceSecurity.getTransportLevelSecurity();
					tlsButton.setSelected(true);
					tlsPanel.setTransportLevelSecurity(s);
				}

				if (serviceSecurity.getSecureConversation() != null) {
					SecureConversation s = serviceSecurity.getSecureConversation();
					secureConversationButton.setSelected(true);
					secureConversationPanel.setSecureConversation(s);
				}

				if (serviceSecurity.getSecureMessage() != null) {
					SecureMessage s = serviceSecurity.getSecureMessage();
					secureMessageButton.setSelected(true);
					secureMessagePanel.setSecureMessage(s);
				}
				if (serviceSecurity.getRunAsMode() != null) {
					runAsMode.setSelectedItem(serviceSecurity.getRunAsMode());
				}

				if (serviceSecurity.getAnonymousClients() != null) {
					anonymousCommunication.setSelectedItem(serviceSecurity.getAnonymousClients());
				}

			} else {
				noneButton.setSelected(true);
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
		runAsMode.setEnabled(false);
		anonymousCommunication.setEnabled(false);
		authorizationMechanism.setEnabled(false);
		authLayout.show(authPanel, NO_AUTHORIZATION);
	}


	/**
	 * This method initializes tlsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTlsPanel() {
		if (tlsPanel == null) {
			tlsPanel = new TransportLayerSecurityPanel();
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
			customLabel = new JLabel();
			customLabel.setText("Custom");
			noneLabel = new JLabel();
			noneLabel.setText("None");
			choicePanel = new JPanel();
			choicePanel.setLayout(new GridBagLayout());
			choicePanel.add(getNoneButton());
			choicePanel.add(noneLabel);
			choicePanel.add(getCustomButton());
			choicePanel.add(customLabel);
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
			secureMessageLabel = new JLabel();
			secureMessageLabel.setText("Secure Message");
			secureConversationLabel = new JLabel();
			secureConversationLabel.setText("Secure Conversation");
			transportLayerSecurityLabel = new JLabel();
			transportLayerSecurityLabel.setText("Transport Layer Security");
			commPanel = new JPanel();
			commPanel.add(getTlsButton(), null);
			commPanel.add(transportLayerSecurityLabel, null);
			commPanel.add(getSecureConversationButton(), null);
			commPanel.add(secureConversationLabel, null);
			commPanel.add(getSecureMessageButton(), null);
			commPanel.add(secureMessageLabel, null);
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
			transportPanel.addTab("Other", null, getGeneralSecurity(), null);
		}
		return transportPanel;
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
	 * This method initializes generalSecurity
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGeneralSecurity() {
		if (generalSecurity == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints.gridy = 0;
			anonClientsLabel = new JLabel();
			anonClientsLabel.setText("Anonymous Clients");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 1;
			gridBagConstraints12.weightx = 1.0D;
			gridBagConstraints12.weighty = 0.0D;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints21.gridy = 1;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.gridx = 0;
			runAsLabel = new JLabel();
			runAsLabel.setText("Run As");
			generalSecurity = new JPanel();
			generalSecurity.setLayout(new GridBagLayout());
			generalSecurity.add(runAsLabel, gridBagConstraints21);
			generalSecurity.add(getRunAsMode(), gridBagConstraints12);
			generalSecurity.add(anonClientsLabel, gridBagConstraints);
			generalSecurity.add(getAnonymousCommunication(), gridBagConstraints1);
		}
		return generalSecurity;
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
			authorizationTypePanel = new JPanel();
			authorizationTypePanel.setLayout(new FlowLayout());
			authorizationTypePanel.add(getJPanel1(), null);
		}
		return authorizationTypePanel;
	}


	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.gridx = 0;
			authLabel = new JLabel();
			authLabel.setFont(new Font("Dialog", Font.BOLD, 14));
			authLabel.setText("Authorization Mechanism");
			authLabel.setForeground(PortalLookAndFeel.getPanelLabelColor());
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(authLabel, gridBagConstraints3);
			jPanel1.add(getAuthorizationMechanism(), gridBagConstraints5);
		}
		return jPanel1;
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
			authorizationMechanism.addItem(GRID_GROUPER_AUTHORIZATION);
			authorizationMechanism.addItem(CSM_AUTHORIZATION);
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
			authPanel.add(getNoAuthorizationPanel(), NO_AUTHORIZATION);
			authPanel.add(getGridGrouper(), getGridGrouper().getName());
			authPanel.add(getCsmPanel(), getCsmPanel().getName());
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
			noAuthorizationPanel.setName("No Authorization");
		}
		return noAuthorizationPanel;
	}


	/**
	 * This method initializes gridGrouper
	 * 
	 * @return gov.nih.nci.cagrid.gridgrouper.ui.expressioneditor.GridGrouperExpressionEditor
	 */
	private GridGrouperExpressionEditor getGridGrouper() {
		if (gridGrouper == null) {
			gridGrouper = new GridGrouperExpressionEditor(GridGrouperURLManager.getGridGroupers(),
				GridGrouperURLManager.getLoadOnStartup());
			gridGrouper.setName(GRID_GROUPER_AUTHORIZATION);
		}
		return gridGrouper;
	}


	/**
	 * This method initializes csmPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private CSMPanel getCsmPanel() {
		if (csmPanel == null) {
			csmPanel = new CSMPanel(service.getName(), method.getName());
			csmPanel.setName(CSM_AUTHORIZATION);
		}
		return csmPanel;
	}
}
