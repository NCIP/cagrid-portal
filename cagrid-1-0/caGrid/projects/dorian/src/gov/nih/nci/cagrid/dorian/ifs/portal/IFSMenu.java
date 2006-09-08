package gov.nih.nci.cagrid.dorian.ifs.portal;

import gov.nih.nci.cagrid.dorian.portal.DorianLookAndFeel;
import gov.nih.nci.cagrid.gridca.ui.ProxyManagerComponent;

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
 * @version $Id: IFSMenu.java,v 1.17 2006-09-08 17:31:47 langella Exp $
 */
public class IFSMenu extends GridPortalComponent {

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel menuPanel = null;

	private JRadioButton createProxy = null;

	private JLabel createProxyLabel = null;

	private ButtonGroup group;

	private JPanel buttonPanel = null;

	private JButton perform = null;

	private JButton close = null;

	private JRadioButton manageProxies = null;

	private JLabel manageProxiesLabel = null;

	private JRadioButton manageUsers = null;

	private JLabel manageUsersLabel = null;

	private JRadioButton trustedIdP = null;

	private JLabel idpLabel = null;

	private JRadioButton findCACertificate = null;

	private JLabel jLabel = null;

	public IFSMenu() {
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
		this.setFrameIcon(DorianLookAndFeel.getIFSIcon());
		this.setTitle("Identity Federation Menu");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
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
			gridBagConstraints15.insets = new java.awt.Insets(10, 10, 10, 10);
			mainPanel.add(getMenuPanel(), gridBagConstraints9);
			mainPanel.add(getButtonPanel(), gridBagConstraints15);
		}
		return mainPanel;
	}

	private JPanel getMenuPanel() {
		if (menuPanel == null) {
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 1;
			gridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints23.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints23.gridy = 4;
			jLabel = new JLabel();
			jLabel.setText("Dorian CA Certifcate");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints13.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints13.gridy = 4;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 1;
			gridBagConstraints22.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints22.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints22.gridy = 3;
			idpLabel = new JLabel();
			idpLabel.setText("Manage Trusted IdPs");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints12.gridy = 3;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints3.gridy = 2;
			manageUsersLabel = new JLabel();
			manageUsersLabel.setText("User Management");
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints21.gridy = 2;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.gridy = 1;
			manageProxiesLabel = new JLabel();
			manageProxiesLabel.setText("Proxy Management");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints.gridy = 1;
			createProxyLabel = new JLabel();
			menuPanel = new JPanel();
			menuPanel.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();

			createProxyLabel.setText("Create Proxy");
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			menuPanel.add(getRegister(), gridBagConstraints1);
			menuPanel.add(createProxyLabel, gridBagConstraints2);
			menuPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Identity Federation Options",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					DorianLookAndFeel.getPanelLabelColor()));
			menuPanel.add(getManageProxies(), gridBagConstraints);

			menuPanel.add(manageProxiesLabel, gridBagConstraints11);
			menuPanel.add(getManageUsers(), gridBagConstraints21);
			menuPanel.add(manageUsersLabel, gridBagConstraints3);
			menuPanel.add(getTrustedIdP(), gridBagConstraints12);
			menuPanel.add(idpLabel, gridBagConstraints22);
			menuPanel.add(getFindCACertificate(), gridBagConstraints13);
			menuPanel.add(jLabel, gridBagConstraints23);
		}
		return menuPanel;
	}

	private JRadioButton getRegister() {
		if (createProxy == null) {
			createProxy = new JRadioButton();
			group.add(createProxy);
		}
		return createProxy;
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

	private void perform() {
		if (createProxy.isSelected()) {
			PortalResourceManager.getInstance().getGridPortal()
					.addGridPortalComponent(new CreateProxyComponent(), 450,
							325);
		} else if (manageProxies.isSelected()) {
			PortalResourceManager.getInstance().getGridPortal()
					.addGridPortalComponent(new ProxyManagerComponent(), 600,
							400);
		} else if (manageUsers.isSelected()) {
			PortalResourceManager.getInstance().getGridPortal()
					.addGridPortalComponent(new UserManagerWindow(),700,550);
		}else if (trustedIdP.isSelected()) {
			PortalResourceManager.getInstance().getGridPortal()
			.addGridPortalComponent(new TrustedIdPsWindow());
		}else if (findCACertificate.isSelected()) {
			PortalResourceManager.getInstance().getGridPortal()
			.addGridPortalComponent(new ViewCACertificateWindow(),500,150);
		}
		dispose();
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

	/**
	 * This method initializes manageProxies
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getManageProxies() {
		if (manageProxies == null) {
			manageProxies = new JRadioButton();
			group.add(manageProxies);
		}
		return manageProxies;
	}

	/**
	 * This method initializes manageUsers
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getManageUsers() {
		if (manageUsers == null) {
			manageUsers = new JRadioButton();
			group.add(manageUsers);
		}
		return manageUsers;
	}

	/**
	 * This method initializes trustedIdP	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */    
	private JRadioButton getTrustedIdP() {
		if (trustedIdP == null) {
			trustedIdP = new JRadioButton();
			group.add(trustedIdP);
		}
		return trustedIdP;
	}

	/**
	 * This method initializes findCACertificate	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */    
	private JRadioButton getFindCACertificate() {
		if (findCACertificate == null) {
			findCACertificate = new JRadioButton();
			group.add(findCACertificate);
		}
		return findCACertificate;
	}
}
