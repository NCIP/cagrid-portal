package gov.nih.nci.cagrid.dorian.ui.ifs;

import gov.nih.nci.cagrid.dorian.client.IdPAuthenticationClient;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.ui.IdPConf;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class DorianIdPAuthenticationPanel extends IdPAuthenticationPanel {
	private JPanel loginPanel = null;
	private JLabel userIdLabel = null;
	private JTextField userId = null;
	private JLabel passwordLabel = null;
	private JPasswordField password = null;

	public DorianIdPAuthenticationPanel(IdPConf conf) {
		super(conf);
		initialize();
	}

	public SAMLAssertion authenticate() throws Exception{
		BasicAuthCredential cred = new BasicAuthCredential();
		cred.setUserId(userId.getText());
		cred.setPassword(new String(password.getPassword()));
		IdPAuthenticationClient client = new IdPAuthenticationClient(getIdPInfo().getParameter("serviceId"),cred);
		return client.authenticate();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getLoginPanel(), java.awt.BorderLayout.CENTER);
	}

	/**
	 * This method initializes loginPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getLoginPanel() {
		if (loginPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.weighty = 0.0D;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridx = 0;
			passwordLabel = new JLabel();
			passwordLabel.setText("Password");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.weighty = 0.0D;
			gridBagConstraints.weightx = 1.0;
			userIdLabel = new JLabel();
			userIdLabel.setText("User Id");
			loginPanel = new JPanel();
			loginPanel.setLayout(new GridBagLayout());
			loginPanel.add(userIdLabel, gridBagConstraints1);
			loginPanel.add(getUserId(), gridBagConstraints);
			loginPanel.add(passwordLabel, gridBagConstraints2);
			loginPanel.add(getPassword(), gridBagConstraints3);
		}
		return loginPanel;
	}

	/**
	 * This method initializes userId	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getUserId() {
		if (userId == null) {
			userId = new JTextField();
		}
		return userId;
	}

	/**
	 * This method initializes password	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */    
	private JPasswordField getPassword() {
		if (password == null) {
			password = new JPasswordField();
		}
		return password;
	}

}
