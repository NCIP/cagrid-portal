package gov.nih.nci.cagrid.dorian.ui.idp;





import gov.nih.nci.cagrid.dorian.ui.DorianLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: IdPMenu.java,v 1.1 2006-09-08 19:22:18 langella Exp $
 */
public class IdPMenu extends GridPortalComponent {

	private javax.swing.JPanel jContentPane = null;
	
	private JPanel mainPanel = null;
	private JPanel menuPanel = null;
	private JRadioButton register = null;
	private JLabel registerLabel = null;
	private JRadioButton userManagement = null;
	private JLabel userManagementLabel = null;
	private ButtonGroup group;
	private JPanel buttonPanel = null;
	private JButton perform = null;
	private JButton close = null;

	public IdPMenu() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setFrameIcon(DorianLookAndFeel.getIdpIcon());
		this.setTitle("Identity Provider Menu");
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			group = new ButtonGroup();
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getMainPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.weightx = 1.0D;
			gridBagConstraints9.weighty = 1.0D;
			gridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.gridy = 1;
			gridBagConstraints15.insets = new java.awt.Insets(10,10,10,10);
			mainPanel.add(getMenuPanel(), gridBagConstraints9);
			mainPanel.add(getButtonPanel(), gridBagConstraints15);
		}
		return mainPanel;
	}

	private JPanel getMenuPanel() {
		if (menuPanel == null) {
			registerLabel = new JLabel();
			userManagementLabel = new JLabel();
			menuPanel = new JPanel();
			menuPanel.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		
			registerLabel.setText("Register");
			userManagementLabel.setText("Manage Users");
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			menuPanel.add(getRegister(), gridBagConstraints1);
			menuPanel.add(registerLabel, gridBagConstraints2);
			menuPanel.add(getUserManagement(), gridBagConstraints3);
			menuPanel.add(userManagementLabel, gridBagConstraints4);
			menuPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Identity Provider Options",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, DorianLookAndFeel.getPanelLabelColor()));	
			
		}
		return menuPanel;
	}

	
	private JRadioButton getRegister() {
		if (register == null) {
			register = new JRadioButton();
			group.add(register);
		}
		return register;
	}

	
	private JRadioButton getUserManagement() {
		if (userManagement == null) {
			userManagement = new JRadioButton();
			group.add(userManagement);
		}
		return userManagement;
	}

	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getPerform(), null);
			buttonPanel.add(getClose(), null);
		}
		return buttonPanel;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getPerform() {
		if (perform == null) {
			perform = new JButton();
			perform.setText("Select");
			perform.setIcon(DorianLookAndFeel.getSelectIcon());
			perform.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					perform();
				}
			});
		}
		return perform;
	}
	
	
	private void perform(){
		if(register.isSelected()){
			PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(new ApplicationWindow(),400,500);
		}else if(userManagement.isSelected()){
			PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(new UserManagerWindow(),600,500);
		}
		
	}
	
	
	
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getClose() {
		if (close == null) {
			close = new JButton();
			close.setText("Close");
			close.setIcon(DorianLookAndFeel.getCloseIcon());
			close.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					dispose();
				}
			});
		}
		return close;
	}
     }
