package gov.nih.nci.cagrid.gts.ui;

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
 * @version $Id: GTSMenu.java,v 1.1 2006-09-08 19:22:50 langella Exp $
 */
public class GTSMenu extends GridPortalComponent {

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel menuPanel = null;

	private ButtonGroup group;

	private JPanel buttonPanel = null;

	private JButton perform = null;

	private JButton close = null;

	private JRadioButton manageTrustedAuthorities = null;

	private JLabel jLabel = null;

	private JRadioButton manageAccess = null;

	private JLabel jLabel1 = null;

	private JRadioButton manageTrustLevels = null;

	private JLabel jLabel2 = null;

	private JRadioButton manageAuthorities = null;

	private JLabel jLabel3 = null;

	public GTSMenu() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(400, 300);
		this.setContentPane(getJContentPane());
		this.setFrameIcon(GTSLookAndFeel.getGTSIcon());
		this.setTitle("Grid Trust Management");
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
			gridBagConstraints23.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints23.gridy = 4;
			jLabel3 = new JLabel();
			jLabel3.setText("Manage Authorities");
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints14.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints14.gridy = 4;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 1;
			gridBagConstraints22.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints22.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints22.gridy = 3;
			jLabel2 = new JLabel();
			jLabel2.setText("Manage Trust Levels");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints13.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints13.gridy = 3;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints21.gridy = 2;
			jLabel1 = new JLabel();
			jLabel1.setText("Manage Access Control");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.gridy = 2;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints11.gridy = 1;
			jLabel = new JLabel();
			jLabel.setText("Manage Trusted Authorities");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints.gridy = 1;
			menuPanel = new JPanel();
			menuPanel.setLayout(new GridBagLayout());
			menuPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Grid Trust Management Options",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					GTSLookAndFeel.getPanelLabelColor()));
			menuPanel.add(getManageTrustedAuthorities(), gridBagConstraints);
			menuPanel.add(jLabel, gridBagConstraints11);

			menuPanel.add(getManageAccess(), gridBagConstraints12);
			menuPanel.add(jLabel1, gridBagConstraints21);
			menuPanel.add(getManageTrustLevels(), gridBagConstraints13);
			menuPanel.add(jLabel2, gridBagConstraints22);
			menuPanel.add(getManageAuthorities(), gridBagConstraints14);
			menuPanel.add(jLabel3, gridBagConstraints23);
		}
		return menuPanel;
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
			perform.setIcon(GTSLookAndFeel.getSelectIcon());
			perform.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					perform();
				}
			});
		}
		return perform;
	}

	private void perform() {
		if (manageTrustedAuthorities.isSelected()) {
			PortalResourceManager.getInstance().getGridPortal()
					.addGridPortalComponent(new TrustedAuthoritiesWindow());
		} else if (manageAccess.isSelected()) {
			PortalResourceManager.getInstance().getGridPortal()
					.addGridPortalComponent(new PermissionManagerWindow());
		} else if (manageTrustLevels.isSelected()) {
			PortalResourceManager.getInstance().getGridPortal()
					.addGridPortalComponent(new TrustLevelManagerWindow());
		} else if (manageAuthorities.isSelected()) {
			PortalResourceManager.getInstance().getGridPortal()
					.addGridPortalComponent(new AuthorityManagerWindow());
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
			close.setIcon(GTSLookAndFeel.getCloseIcon());
			close.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return close;
	}

	/**
	 * This method initializes manageTrustedAuthorities
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getManageTrustedAuthorities() {
		if (manageTrustedAuthorities == null) {
			manageTrustedAuthorities = new JRadioButton();
			group.add(manageTrustedAuthorities);
		}
		return manageTrustedAuthorities;
	}

	/**
	 * This method initializes manageAccess
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getManageAccess() {
		if (manageAccess == null) {
			manageAccess = new JRadioButton();
			group.add(manageAccess);
		}
		return manageAccess;
	}

	/**
	 * This method initializes manageTrustLevels
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getManageTrustLevels() {
		if (manageTrustLevels == null) {
			manageTrustLevels = new JRadioButton();
			group.add(manageTrustLevels);
		}
		return manageTrustLevels;
	}

	/**
	 * This method initializes manageAuthorities
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getManageAuthorities() {
		if (manageAuthorities == null) {
			manageAuthorities = new JRadioButton();
			group.add(manageAuthorities);
		}
		return manageAuthorities;
	}
}
