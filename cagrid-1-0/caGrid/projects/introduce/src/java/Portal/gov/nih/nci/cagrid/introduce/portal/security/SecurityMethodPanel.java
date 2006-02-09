package gov.nih.nci.cagrid.introduce.portal.security;

import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurityType;
import gov.nih.nci.cagrid.introduce.beans.security.SecureConversation;
import gov.nih.nci.cagrid.introduce.beans.security.SecureMessage;
import gov.nih.nci.cagrid.introduce.beans.security.TransportLevelSecurity;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


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
	public SecurityMethodPanel() {
		super();
		initialize();
	}


	public SecurityMethodPanel(MethodSecurity ms) {
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
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.weightx = 1.0D;
		gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
		gridBagConstraints6.gridy = 4;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.insets = new java.awt.Insets(2,2,2,2);
		gridBagConstraints11.weightx = 1.0D;
		gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.gridy = 3;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.gridy = 1;
		this.setLayout(new GridBagLayout());
		this.setSize(500, 400);
		this.add(getSecureCommunicationPanel(), new GridBagConstraints());
		this.add(getTlsPanel(), gridBagConstraints);
		this.add(getSecureConversationPanel(), gridBagConstraints11);
		this.add(getSecureMessagePanel(), gridBagConstraints6);
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
			secureCommunicationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				"Security Configuration", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
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


	public MethodSecurity getMethodSecurity() {
		MethodSecurity ms = new MethodSecurity();
		 if (defaultButton.isEnabled()) {
			ms.setMethodSecuritySetting(MethodSecurityType.Default);
		} else if (noneButton.isEnabled()) {
			ms.setMethodSecuritySetting(MethodSecurityType.None);
		}else if (customButton.isEnabled()) {
			ms.setMethodSecuritySetting(MethodSecurityType.Custom);
			if(tlsButton.isSelected()){
				ms.setTransportLevelSecurity(this.tlsPanel.getTransportLevelSecurity());
			}
			if(secureConversationButton.isSelected()){
				ms.setSecureConversation(this.secureConversationPanel.getSecureConversation());
			}
			if(secureMessageButton.isSelected()){
				ms.setSecureMessage(this.secureMessagePanel.getSecureMessage());
			}
		}
		return ms;
	}


	public void setMethodSecurity(MethodSecurity ms) {
		if(ms.getMethodSecuritySetting().equals(MethodSecurityType.Default)){
			defaultButton.setSelected(true);
		}else if(ms.getMethodSecuritySetting().equals(MethodSecurityType.None)){
			noneButton.setSelected(true);
		}else if(ms.getMethodSecuritySetting().equals(MethodSecurityType.Custom)){
			customButton.setSelected(true);
			TransportLevelSecurity tls = ms.getTransportLevelSecurity();
			if(tls!=null){
				tlsButton.setEnabled(true);
				this.tlsPanel.setTransportLevelSecurity(tls);
			}
			SecureConversation sc = ms.getSecureConversation();
			if(sc!=null){
				secureConversationButton.setEnabled(true);
				this.secureConversationPanel.setSecureConversation(sc);
			}
			SecureMessage sm = ms.getSecureMessage();
			if(sm!=null){
				secureMessageButton.setSelected(true);
				this.secureMessagePanel.setSecureConversation(sm);
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
			if(secureConversationButton.isSelected()){
				secureConversationPanel.enablePanel();
			}
			if(secureMessageButton.isSelected()){
				secureMessagePanel.enablePanel();
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
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.gridy = -1;
			gridBagConstraints5.gridx = -1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
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

}
