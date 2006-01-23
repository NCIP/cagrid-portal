package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.introduce.beans.method.AnonymousClientsType;
import gov.nih.nci.cagrid.introduce.beans.method.AuthenticationMethodType;
import gov.nih.nci.cagrid.introduce.beans.method.ClientAuthorizationType;
import gov.nih.nci.cagrid.introduce.beans.method.SecureCommunicationConfiguration;
import gov.nih.nci.cagrid.introduce.beans.method.SecureCommunicationMethodType;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class SecurityConfigurationPanel extends JPanel {

	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JComboBox communicationMethod = null;
	private JLabel jLabel2 = null;
	private JComboBox anonymousCommunication = null;
	private JLabel jLabel3 = null;
	private JComboBox clientAuthorization = null;
	private JComboBox secureCommunication = null;


	/**
	 * This is the default constructor
	 */
	public SecurityConfigurationPanel() {
		super();
		initialize();
	}
	public SecurityConfigurationPanel(SecureCommunicationConfiguration ms) {
		super();
		initialize();
		if (ms != null) {
			if (ms.getSecureCommunication() != null) {
				secureCommunication.setSelectedItem(ms.getSecureCommunication());
			}
			if (ms.getAuthenticationMethod() != null) {
				communicationMethod.setSelectedItem(ms.getAuthenticationMethod());
			}
			if (ms.getAnonymousClients() != null) {
				anonymousCommunication.setSelectedItem(ms.getAnonymousClients());
			}
			if (ms.getClientAuthorization() != null) {
				clientAuthorization.setSelectedItem(ms.getClientAuthorization());
			}
		}
	}
	
	public SecureCommunicationConfiguration getSecureCommunicationConfiguration(){
		SecureCommunicationConfiguration ms= new SecureCommunicationConfiguration();
		SecureCommunicationMethodType comm = (SecureCommunicationMethodType) getSecureCommunication().getSelectedItem();
		ms.setSecureCommunication(comm);
		if (comm.equals(SecureCommunicationMethodType.GSI_Transport_Level_Security)) {
			ms.setAnonymousClients((AnonymousClientsType) getAnonymousCommunication().getSelectedItem());
			ms.setAuthenticationMethod((AuthenticationMethodType) getCommunicationMethod()
				.getSelectedItem());
			ms.setClientAuthorization((ClientAuthorizationType) getClientAuthorization().getSelectedItem());
		}

		if (comm.equals(SecureCommunicationMethodType.GSI_Secure_Conversation)) {
			ms.setAnonymousClients((AnonymousClientsType) getAnonymousCommunication().getSelectedItem());
			ms.setAuthenticationMethod((AuthenticationMethodType) getCommunicationMethod()
				.getSelectedItem());
			ms.setClientAuthorization((ClientAuthorizationType) getClientAuthorization().getSelectedItem());
		}

		if (comm.equals(SecureCommunicationMethodType.GSI_Secure_Message)) {
			ms.setAnonymousClients(AnonymousClientsType.No);
			ms.setAuthenticationMethod((AuthenticationMethodType) getCommunicationMethod()
				.getSelectedItem());
			ms.setClientAuthorization((ClientAuthorizationType) getClientAuthorization().getSelectedItem());
		}

		if (comm.equals(SecureCommunicationMethodType.None)) {
			ms.setAnonymousClients(AnonymousClientsType.No);
			ms.setAuthenticationMethod((AuthenticationMethodType) getCommunicationMethod()
				.getSelectedItem());
			ms.setClientAuthorization(ClientAuthorizationType.None);
		}
		return ms;
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(300, 200);
		this.add(getJPanel(), java.awt.BorderLayout.NORTH);
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 3;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 3;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			jLabel3 = new JLabel();
			jLabel3.setText("Client Authorization");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 2;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			jLabel2 = new JLabel();
			jLabel2.setText("Anonymous Communication");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			jLabel1 = new JLabel();
			jLabel1.setText("Communication Method");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			jLabel = new JLabel();
			jLabel.setText("Secure Communication");
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(jLabel, gridBagConstraints);
			jPanel.add(jLabel1, gridBagConstraints1);
			jPanel.add(getCommunicationMethod(), gridBagConstraints2);
			jPanel.add(jLabel2, gridBagConstraints3);
			jPanel.add(getAnonymousCommunication(), gridBagConstraints4);
			jPanel.add(jLabel3, gridBagConstraints5);
			jPanel.add(getClientAuthorization(), gridBagConstraints6);
			jPanel.add(getSecureCommunication(), gridBagConstraints7);
		}
		return jPanel;
	}


	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getCommunicationMethod() {
		if (communicationMethod == null) {
			communicationMethod = new JComboBox();
			communicationMethod.addItem(AuthenticationMethodType.Integrity);
			communicationMethod.addItem(AuthenticationMethodType.Privacy);
			communicationMethod.addItem(AuthenticationMethodType.Integrity_Or_Privacy);
		}
		return communicationMethod;
	}


	/**
	 * This method initializes jComboBox1	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getAnonymousCommunication() {
		if (anonymousCommunication == null) {
			anonymousCommunication = new JComboBox();
			anonymousCommunication.addItem(AnonymousClientsType.No);
			anonymousCommunication.addItem(AnonymousClientsType.Yes);
		}
		return anonymousCommunication;
	}


	/**
	 * This method initializes jComboBox2	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getClientAuthorization() {
		if (clientAuthorization == null) {
			clientAuthorization = new JComboBox();
			clientAuthorization.addItem(ClientAuthorizationType.None);
			clientAuthorization.addItem(ClientAuthorizationType.Host);
			clientAuthorization.addItem(ClientAuthorizationType.Self);
		}
		return clientAuthorization;
	}


	private void updateCombos(){
		if (secureCommunication.getSelectedItem().equals(SecureCommunicationMethodType.Default)) {
			communicationMethod.setEnabled(false);
			anonymousCommunication.setSelectedItem(AnonymousClientsType.No);
			anonymousCommunication.setEnabled(false);
			clientAuthorization.setEnabled(false);
		}else if (secureCommunication.getSelectedItem().equals(SecureCommunicationMethodType.None)) {
			communicationMethod.setEnabled(false);
			anonymousCommunication.setSelectedItem(AnonymousClientsType.No);
			anonymousCommunication.setEnabled(false);
			clientAuthorization.setEnabled(false);
		} else if (secureCommunication.getSelectedItem().equals(
			SecureCommunicationMethodType.GSI_Secure_Conversation)) {
			communicationMethod.setEnabled(true);
			anonymousCommunication.setEnabled(true);
			clientAuthorization.setEnabled(true);
		} else if (secureCommunication.getSelectedItem().equals(
			SecureCommunicationMethodType.GSI_Secure_Message)) {
			communicationMethod.setEnabled(true);
			anonymousCommunication.setSelectedItem(AnonymousClientsType.No);
			anonymousCommunication.setEnabled(false);
			clientAuthorization.setEnabled(true);
		} else if (secureCommunication.getSelectedItem().equals(
			SecureCommunicationMethodType.GSI_Transport_Level_Security)) {
			communicationMethod.setEnabled(true);
			anonymousCommunication.setEnabled(true);
			clientAuthorization.setEnabled(true);
		}
	}
	
	private JComboBox getSecureCommunication() {
		if (secureCommunication == null) {
			secureCommunication = new JComboBox();
			secureCommunication.addItem(SecureCommunicationMethodType.Default);
			secureCommunication.addItem(SecureCommunicationMethodType.None);
			secureCommunication.addItem(SecureCommunicationMethodType.GSI_Transport_Level_Security);
			secureCommunication.addItem(SecureCommunicationMethodType.GSI_Secure_Conversation);
			secureCommunication.addItem(SecureCommunicationMethodType.GSI_Secure_Message);
			secureCommunication.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					updateCombos();
				}
			});
			updateCombos();
		}
		return secureCommunication;
	}

}
