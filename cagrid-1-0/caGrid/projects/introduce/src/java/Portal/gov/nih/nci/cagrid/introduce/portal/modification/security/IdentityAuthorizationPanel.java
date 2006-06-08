package gov.nih.nci.cagrid.introduce.portal.modification.security;

import gov.nih.nci.cagrid.introduce.beans.security.IdentityAuthorization;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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
public class IdentityAuthorizationPanel extends JPanel {

	private boolean enabled = false;
	private JLabel label = null;
	private JTextField identity = null;
	public IdentityAuthorizationPanel() {
		super();
		initialize();
	}

	public void setIdentityAuthorization(IdentityAuthorization auth){
		identity.setText(auth.getIdentity());
	}

	public IdentityAuthorization getIdentitytAuthorization() throws Exception{
		if (enabled) {
			if((identity.getText()==null)||(identity.getText().trim().length()==0)){
				throw new Exception("For client identity authorization you must specify a grid identity.");
			}
			IdentityAuthorization auth = new IdentityAuthorization();
			auth.setIdentity(identity.getText());
			return auth;
		} else {
			return null;
		}
	}


	public void disablePanel() {
		enabled = false;
		this.identity.setEnabled(false);
	}


	public void enablePanel() {
		enabled = true;
		this.identity.setEnabled(true);
	}


	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.weightx = 1.0;
		setBorder(javax.swing.BorderFactory.createTitledBorder(
			null, "Identity Authorization Configuration",
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
			javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
			IntroduceLookAndFeel.getPanelLabelColor()));
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.gridx = 0;
		label = new JLabel();
		label.setText("Grid Identity");
		this.setLayout(new GridBagLayout());
		this.add(label, gridBagConstraints);
		this.add(getHostname(), gridBagConstraints1);
	}

	/**
	 * This method initializes identity	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getHostname() {
		if (identity == null) {
			identity = new JTextField();
		}
		return identity;
	}

}
