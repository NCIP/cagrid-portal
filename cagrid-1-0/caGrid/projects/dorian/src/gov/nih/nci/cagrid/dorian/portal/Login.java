package gov.nih.nci.cagrid.gums.portal;

import gov.nih.nci.cagrid.gums.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.gums.idp.portal.IdPLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.projectmobius.portal.PortalResourceManager;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: Login.java,v 1.1 2005-10-26 17:31:21 langella Exp $
 */
public class Login extends JDialog {

	private javax.swing.JPanel jContentPane = null;
	private JPanel mainPanel = null;
	private JPanel infoPanel = null;
	private JLabel usernameLabel = null;
	private JTextField user = null;
	private JLabel passwordLabel = null;
	private JPasswordField jPasswordField = null;
	private JPanel buttonPanel = null;
	private JButton login = null;
	private JButton cancel = null;
	private BasicAuthCredential cred;
	private JCheckBox saveSession = null;
	private JLabel saveSessionLabel = null;
	/**
	 * This is the default constructor
	 */
	public Login() {
		super(PortalResourceManager.getInstance().getGridPortal(),true);
		initialize();
		
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("Session");	
	}
	
	public void edit(){	
			this.pack();
			this.setSize(250, 175);
			this.show();	
	}
	
	public BasicAuthCredential login(){
		if((getCredentials()==null) || (!saveSession.isSelected())){
			this.pack();
			this.setSize(250, 175);
			this.show();
			if(!saveSession.isSelected()){
				this.getUser().setText("");
				this.getJPasswordField().setText("");
			}
		}
		return getCredentials();
	}
	
	private BasicAuthCredential getCredentials(){
		return cred;
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getMainPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
	  
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.gridy = 0;
			gridBagConstraints16.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints16.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints16.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints16.weightx = 1.0D;
			gridBagConstraints16.weighty = 1.0D;
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridy = 1;
			gridBagConstraints21.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.SOUTH;
			mainPanel.add(getInfoPanel(), gridBagConstraints16);
			mainPanel.add(getButtonPanel(), gridBagConstraints21);
		}
		return mainPanel;
	}
    
	private JPanel getInfoPanel() {
		if (infoPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 2;
			saveSessionLabel = new JLabel();
			saveSessionLabel.setText("Save as Session");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints.gridy = 2;
			passwordLabel = new JLabel();
			usernameLabel = new JLabel();
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			infoPanel = new JPanel();
			infoPanel.setLayout(new GridBagLayout());
			usernameLabel.setText("Username");
			infoPanel.setName("login");
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.gridy = 0;
			gridBagConstraints19.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints19.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints17.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints17.gridx = 1;
			gridBagConstraints17.gridy = 0;
			gridBagConstraints17.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.gridy = 1;
			gridBagConstraints20.insets = new java.awt.Insets(5,5,5,5);
			infoPanel.add(usernameLabel, gridBagConstraints19);
			infoPanel.add(passwordLabel, gridBagConstraints20);
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.weighty = 1.0D;
			passwordLabel.setText("Password");
			gridBagConstraints18.gridx = 1;
			gridBagConstraints18.gridy = 1;
			gridBagConstraints18.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints18.weightx = 1.0;
			gridBagConstraints18.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints18.weighty = 1.0D;
			infoPanel.add(getUser(), gridBagConstraints17);
			infoPanel.add(getJPasswordField(), gridBagConstraints18);
			infoPanel.add(getSaveSession(), gridBagConstraints);
			infoPanel.add(saveSessionLabel, gridBagConstraints1);
		}
		return infoPanel;
	}
   
	private JTextField getUser() {
		if (user == null) {
			user = new JTextField();
			user.setPreferredSize(new java.awt.Dimension(100,20));
		}
		return user;
	}

	private JPasswordField getJPasswordField() {
		if (jPasswordField == null) {
			jPasswordField = new JPasswordField();
			jPasswordField.setPreferredSize(new java.awt.Dimension(100,20));
		}
		return jPasswordField;
	}
	 
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getLogin(), null);
			buttonPanel.add(getCancel(), null);
		}
		return buttonPanel;
	}
   
	private JButton getLogin() {
		if (login == null) {
			login = new JButton();
			login.setText("Login");
			login.setIcon(IdPLookAndFeel.getLoginIcon());
			login.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					cred = new BasicAuthCredential();
					cred.setUserId(user.getText());
					cred.setPassword(new String(jPasswordField.getPassword()));
					dispose();
				}
			});
		}
		return login;
	}
    
	private JButton getCancel() {
		if (cancel == null) {
			cancel = new JButton();
			cancel.setText("Cancel");
			cancel.setIcon(GumsLookAndFeel.getCloseIcon());
			cancel.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					dispose();
				}
			});
		}
		return cancel;
	}
	/**
	 * This method initializes saveSession	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getSaveSession() {
		if (saveSession == null) {
			saveSession = new JCheckBox();
		}
		return saveSession;
	}
	
	public void resetSession(){
		cred = null;
	}
       }
