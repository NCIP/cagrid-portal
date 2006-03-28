package gov.nih.nci.cagrid.gts.portal;

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
 * @version $Id: GTSMenu.java,v 1.6 2006-03-28 20:49:34 langella Exp $
 */
public class GTSMenu extends GridPortalComponent {

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;
	private JPanel menuPanel = null;
	private JRadioButton trustedAuthority = null;
	private JLabel addTALabel = null;
	private ButtonGroup group;
	private JPanel buttonPanel = null;
	private JButton perform = null;
	private JButton close = null;

	private JRadioButton manageTrustedAuthorities = null;

	private JLabel jLabel = null;

	private JRadioButton manageAccess = null;

	private JLabel jLabel1 = null;


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
		this.setSize(400, 200);
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
			addTALabel = new JLabel();
			menuPanel = new JPanel();
			menuPanel.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();

			addTALabel.setText("Add Trusted Authority");
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			menuPanel.add(getTrustedAuthority(), gridBagConstraints1);
			menuPanel.add(addTALabel, gridBagConstraints2);
			menuPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Grid Trust Management Options",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, GTSLookAndFeel.getPanelLabelColor()));
			menuPanel.add(getManageTrustedAuthorities(), gridBagConstraints);
			menuPanel.add(jLabel, gridBagConstraints11);

			menuPanel.add(getManageAccess(), gridBagConstraints12);
			menuPanel.add(jLabel1, gridBagConstraints21);
		}
		return menuPanel;
	}


	private JRadioButton getTrustedAuthority() {
		if (trustedAuthority == null) {
			trustedAuthority = new JRadioButton();
			group.add(trustedAuthority);
		}
		return trustedAuthority;
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
		if (trustedAuthority.isSelected()) {
			PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(new TrustedAuthorityWindow());
		} else if (manageTrustedAuthorities.isSelected()) {
			PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(new TrustedAuthoritiesWindow());
		} else if (manageAccess.isSelected()) {
			PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(new PermissionManagerWindow());
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
}
