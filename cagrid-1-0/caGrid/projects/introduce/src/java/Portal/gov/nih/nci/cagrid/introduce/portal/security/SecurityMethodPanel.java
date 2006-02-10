package gov.nih.nci.cagrid.introduce.portal.security;

import gov.nih.nci.cagrid.introduce.beans.security.AnonymousCommunication;
import gov.nih.nci.cagrid.introduce.beans.security.ClientCommunication;
import gov.nih.nci.cagrid.introduce.beans.security.DelegationMode;
import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurityType;
import gov.nih.nci.cagrid.introduce.beans.security.RunAsMode;
import gov.nih.nci.cagrid.introduce.beans.security.SecureConversation;
import gov.nih.nci.cagrid.introduce.beans.security.SecureMessage;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.TransportLevelSecurity;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import java.awt.Insets;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SecurityMethodPanel extends JPanel implements PanelSynchronizer {

	private JPanel secureCommunicationPanel = null;
	private JRadioButton defaultButton = null;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton noneButton = null;
	private JRadioButton customButton = null;
	private TransportLevelSecurityPanel tlsPanel = null;
	private JCheckBox tlsButton = null;
	private JCheckBox secureConversationButton = null;
	private JPanel choicePanel = null;
	private JPanel commPanel = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel Custom = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JCheckBox secureMessageButton = null;
	private JLabel jLabel4 = null;
	private SecureConversationPanel secureConversationPanel = null;
	private SecureMessagePanel secureMessagePanel = null;
	private JPanel confPanel = null;
	private JLabel jLabel5 = null;
	private JComboBox clientCommunication = null;
	private JLabel jLabel6 = null;
	private JComboBox runAsMode = null;
	private JLabel jLabel7 = null;
	private JComboBox delegationMode = null;
	private ServiceSecurity serviceSecurity;
	private boolean syncingComm = false;
	private JLabel jLabel8 = null;
	private JComboBox anonymousCommunication = null;
	private JTabbedPane transportPanel = null;
	private JPanel communicationPanel = null;


	public SecurityMethodPanel(ServiceSecurity sec) {
		super();
		initialize();
		this.serviceSecurity = sec;
	}


	public SecurityMethodPanel(ServiceSecurity sec, MethodSecurity ms) {
		super();
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
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
		gridBagConstraints17.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints17.weighty = 1.0;
		gridBagConstraints17.gridx = 0;
		gridBagConstraints17.gridy = 2;
		gridBagConstraints17.insets = new java.awt.Insets(2,2,2,2);
		gridBagConstraints17.weightx = 1.0;
		GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
		gridBagConstraints71.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints71.weightx = 1.0D;
		this.setLayout(new GridBagLayout());
		this.setSize(500, 500);
		this.add(getSecureCommunicationPanel(), gridBagConstraints71);
		setBorder(javax.swing.BorderFactory.createTitledBorder(null,
			"Security Configuration", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
			javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
		this.add(getTransportPanel(), gridBagConstraints17);
		this.add(getConfPanel(), gridBagConstraints);
		synchronize();
	}


	/**
	 * This method initializes secureCommunicationPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSecureCommunicationPanel() {
		if (secureCommunicationPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.gridy = -1;
			gridBagConstraints1.gridx = -1;
			secureCommunicationPanel = new JPanel();
			secureCommunicationPanel.setLayout(new GridBagLayout());
			secureCommunicationPanel.add(getChoicePanel(), gridBagConstraints2);
			secureCommunicationPanel.add(getCommPanel(), gridBagConstraints4);
		}
		return secureCommunicationPanel;
	}


	/**
	 * This method initializes defaultButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getDefaultButton() {
		if (defaultButton == null) {
			defaultButton = new JRadioButton();
			defaultButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					synchronize();
				}
			});
			buttonGroup.add(defaultButton);
			defaultButton.setSelected(true);
		}
		return defaultButton;
	}


	/**
	 * This method initializes noneButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getNoneButton() {
		if (noneButton == null) {
			noneButton = new JRadioButton();
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
		} else if (serviceSecurity.getTransportLevelSecurity() != null) {
			return true;
		} else {
			return false;
		}
	}


	private boolean usesSecureConversation() {
		if (secureConversationButton.isSelected()) {
			return true;
		} else if (serviceSecurity.getSecureConversation() != null) {
			return true;
		} else {
			return false;
		}
	}


	private boolean usesSecureMessage() {
		if (secureMessageButton.isSelected()) {
			return true;
		} else if (serviceSecurity.getSecureMessage() != null) {
			return true;
		} else {
			return false;
		}
	}


	public MethodSecurity getMethodSecurity() throws Exception {
		MethodSecurity ms = new MethodSecurity();
		if (defaultButton.isEnabled()) {
			ms.setMethodSecuritySetting(MethodSecurityType.Default);
		} else if (noneButton.isEnabled()) {
			ms.setMethodSecuritySetting(MethodSecurityType.None);
		} else if (customButton.isEnabled()) {
			ms.setMethodSecuritySetting(MethodSecurityType.Custom);
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
			if(anonymousCommunication.isEnabled()){
				ms.setAnonymousClients((AnonymousCommunication)anonymousCommunication.getSelectedItem());
			}else{
				ms.setAnonymousClients(AnonymousCommunication.No);
			}

		}
		return ms;
	}


	public void setMethodSecurity(MethodSecurity ms) {
		if (ms.getMethodSecuritySetting().equals(MethodSecurityType.Default)) {
			defaultButton.setSelected(true);
		} else if (ms.getMethodSecuritySetting().equals(MethodSecurityType.None)) {
			noneButton.setSelected(true);
		} else if (ms.getMethodSecuritySetting().equals(MethodSecurityType.Custom)) {
			customButton.setSelected(true);

			TransportLevelSecurity tls = ms.getTransportLevelSecurity();
			if (tls != null) {
				tlsButton.setEnabled(true);
				this.tlsPanel.setTransportLevelSecurity(tls);
			}
			SecureConversation sc = ms.getSecureConversation();
			if (sc != null) {
				secureConversationButton.setEnabled(true);
				this.secureConversationPanel.setSecureConversation(sc);
			}
			SecureMessage sm = ms.getSecureMessage();
			if (sm != null) {
				secureMessageButton.setSelected(true);
				this.secureMessagePanel.setSecureConversation(sm);
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
			if(anon!=null){
				anonymousCommunication.setSelectedItem(anon);
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

			if (isSecure()) {
				runAsMode.setEnabled(true);
			}

			if (isSecure()) {
				ClientCommunication comm = (ClientCommunication) clientCommunication.getSelectedItem();
				if(determineCommOk(comm)){
					//TODO: Check Service Auth
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
			} else if (comm.equals(ClientCommunication.Secure_Conversation)) {
				commOk = true;
			} else if (comm.equals(ClientCommunication.Default)) {
				commOk = determineCommOk(serviceSecurity.getClientCommunication());
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
	}


	private synchronized void synchClientCommunication() {
		if (!syncingComm) {
			syncingComm = true;
			Object obj = clientCommunication.getSelectedItem();
			clientCommunication.removeAllItems();
			if (isSecure()) {
				clientCommunication.setEnabled(true);

				if ((tlsButton.isSelected()) || (serviceSecurity.getTransportLevelSecurity() != null)) {
					clientCommunication.addItem(ClientCommunication.Transport_Layer_Security);
				}
				if ((secureConversationButton.isSelected()) || (serviceSecurity.getSecureConversation() != null)) {
					clientCommunication.addItem(ClientCommunication.Secure_Conversation);
				}
				if ((secureMessageButton.isSelected()) || (serviceSecurity.getSecureMessage() != null)) {
					clientCommunication.addItem(ClientCommunication.Secure_Message);
				}
				int count = 0;
				if (tlsButton.isSelected()) {
					count++;
				}
				if (secureConversationButton.isSelected()) {
					count++;
				}
				if (secureMessageButton.isSelected()) {
					count++;
				}
				if (count == 0) {
					clientCommunication.addItem(ClientCommunication.Default);
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
			jLabel = new JLabel();
			jLabel.setText("Default");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridy = -1;
			gridBagConstraints5.gridx = -1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.gridy = -1;
			gridBagConstraints3.gridx = -1;
			choicePanel = new JPanel();
			choicePanel.setLayout(new GridBagLayout());
			choicePanel.add(getDefaultButton());
			choicePanel.add(jLabel, new GridBagConstraints());
			choicePanel.add(getNoneButton(), gridBagConstraints3);
			choicePanel.add(jLabel1, new GridBagConstraints());
			choicePanel.add(getCustomButton(), gridBagConstraints5);
			choicePanel.add(Custom, new GridBagConstraints());
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
	 * This method initializes confPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getConfPanel() {
		if (confPanel == null) {
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.gridy = 3;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints16.gridx = 1;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints15.gridy = 3;
			jLabel8 = new JLabel();
			jLabel8.setText("Anonymous Communication");
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.gridy = 1;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints14.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints14.gridx = 1;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints13.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints13.gridy = 1;
			jLabel7 = new JLabel();
			jLabel7.setText("Delegation Mode");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridy = 0;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 0;
			jLabel6 = new JLabel();
			jLabel6.setText("Run As");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 2;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.gridx = 0;
			jLabel5 = new JLabel();
			jLabel5.setText("Client Communication");
			confPanel = new JPanel();
			confPanel.setLayout(new GridBagLayout());
			confPanel.add(jLabel5, gridBagConstraints8);
			confPanel.add(getClientCommunication(), gridBagConstraints9);
			confPanel.add(jLabel6, gridBagConstraints10);
			confPanel.add(getRunAsMode(), gridBagConstraints12);
			confPanel.add(jLabel7, gridBagConstraints13);
			confPanel.add(getDelegationMode(), gridBagConstraints14);
			confPanel.add(jLabel8, gridBagConstraints15);
			confPanel.add(getAnonymousCommunication(), gridBagConstraints16);
		}
		return confPanel;
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
			runAsMode.addItem(RunAsMode.Default);
			runAsMode.addItem(RunAsMode.Caller);
			runAsMode.addItem(RunAsMode.Resource);
			runAsMode.addItem(RunAsMode.Service);
			runAsMode.addItem(RunAsMode.System);
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
			delegationMode.addItem(DelegationMode.Default);
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
			anonymousCommunication.addItem(AnonymousCommunication.Yes);
			anonymousCommunication.addItem(AnonymousCommunication.No);
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
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 2;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.gridy = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints6.gridy = 0;
			communicationPanel = new JPanel();
			communicationPanel.setLayout(new GridBagLayout());
			communicationPanel.add(getTlsPanel(), gridBagConstraints6);
			communicationPanel.add(getSecureConversationPanel(), gridBagConstraints7);
			communicationPanel.add(getSecureMessagePanel(), gridBagConstraints11);
		}
		return communicationPanel;
	}

}
