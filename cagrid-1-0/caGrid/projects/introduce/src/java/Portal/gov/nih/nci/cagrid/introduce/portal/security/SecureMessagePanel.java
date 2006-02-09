package gov.nih.nci.cagrid.introduce.portal.security;

import gov.nih.nci.cagrid.introduce.beans.security.CommunicationMethod;
import gov.nih.nci.cagrid.introduce.beans.security.SecureMessage;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SecureMessagePanel extends JPanel {

	private boolean enabled = false;
	private JLabel authMethodLabel = null;
	private JComboBox communicationMethod = null;
	private JLabel jLabel = null;
	private JTextField replayAttackInterval = null;


	public SecureMessagePanel() {
		super();
		initialize();
	}

	public void setSecureConversation(SecureMessage sm){
		communicationMethod.setSelectedItem(sm.getCommunicationMethod());
		Integer num = sm.getReplayAttackInterval();
		if(num!=null){
			this.replayAttackInterval.setText(num.toString());
		}
	}

	public SecureMessage getSecureMessage() throws Exception{
		if (enabled) {
			SecureMessage sm = new SecureMessage();
			sm.setCommunicationMethod((CommunicationMethod)communicationMethod.getSelectedItem());
			String s = replayAttackInterval.getText().trim();
			if(s.length()>0){
				try{
					sm.setReplayAttackInterval(Integer.valueOf(s));
				}catch(Exception e){
					throw new Exception("Replay Attack Interval must be an integer!!!");
				}
			}
			return sm;
		} else {
			return null;
		}
	}


	public void disablePanel() {
		enabled = false;
		communicationMethod.setEnabled(false);
		replayAttackInterval.setEnabled(false);
	}


	public void enablePanel() {
		enabled = true;
		communicationMethod.setEnabled(true);
		replayAttackInterval.setEnabled(true);
	}


	private void initialize() {
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints5.gridy = 1;
		gridBagConstraints5.weightx = 1.0;
		gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
		gridBagConstraints5.gridx = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
		gridBagConstraints4.gridy = 1;
		jLabel = new JLabel();
		jLabel.setText("Replay Attack Interval (milliseconds)");
		setBorder(javax.swing.BorderFactory.createTitledBorder(
			null, "Secure Message",
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
			javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
			IntroduceLookAndFeel.getPanelLabelColor()));
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
		gridBagConstraints1.weightx = 1.0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.gridx = 0;
		authMethodLabel = new JLabel();
		authMethodLabel.setText("Communication Method");
		this.setLayout(new GridBagLayout());
		this.setSize(300, 200);
		this.add(authMethodLabel, gridBagConstraints);
		this.add(getCommunicationMethod(), gridBagConstraints1);
		this.add(jLabel, gridBagConstraints4);
		this.add(getContextLifetime(), gridBagConstraints5);
	}


	/**
	 * This method initializes communicationMethod	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getCommunicationMethod() {
		if (communicationMethod == null) {
			communicationMethod = new JComboBox();
			communicationMethod.addItem(CommunicationMethod.Integrity);
			communicationMethod.addItem(CommunicationMethod.Privacy);
			communicationMethod.addItem(CommunicationMethod.Integrity_Or_Privacy);
		}
		return communicationMethod;
	}

	/**
	 * This method initializes replayAttackInterval	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getContextLifetime() {
		if (replayAttackInterval == null) {
			replayAttackInterval = new JTextField();
		}
		return replayAttackInterval;
	}

}
