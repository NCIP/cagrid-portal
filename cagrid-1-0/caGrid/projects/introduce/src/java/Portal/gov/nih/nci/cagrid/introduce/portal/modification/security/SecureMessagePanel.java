package gov.nih.nci.cagrid.introduce.portal.modification.security;

import gov.nih.nci.cagrid.introduce.beans.security.CommunicationMethod;
import gov.nih.nci.cagrid.introduce.beans.security.SecureMessage;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


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
	public SecureMessagePanel() {
		super();
		initialize();
	}

	public void setSecureMessage(SecureMessage sm){
		communicationMethod.setSelectedItem(sm.getCommunicationMethod());
	}

	public SecureMessage getSecureMessage() throws Exception{
		if (enabled) {
			SecureMessage sm = new SecureMessage();
			sm.setCommunicationMethod((CommunicationMethod)communicationMethod.getSelectedItem());
			return sm;
		} else {
			return null;
		}
	}


	public void disablePanel() {
		enabled = false;
		communicationMethod.setEnabled(false);
	}


	public void enablePanel() {
		enabled = true;
		communicationMethod.setEnabled(true);
	}


	private void initialize() {
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
		this.add(authMethodLabel, gridBagConstraints);
		this.add(getCommunicationMethod(), gridBagConstraints1);
	}


	/**
	 * This method initializes communicationMethod	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getCommunicationMethod() {
		if (communicationMethod == null) {
			communicationMethod = new JComboBox();
			communicationMethod.addItem(CommunicationMethod.Privacy);
			communicationMethod.addItem(CommunicationMethod.Integrity);
			communicationMethod.addItem(CommunicationMethod.Integrity_Or_Privacy);
		}
		return communicationMethod;
	}

}
