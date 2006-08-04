package gov.nih.nci.cagrid.gridgrouper.ui;

import gov.nih.nci.cagrid.gridca.portal.ProxyComboBox;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.projectmobius.portal.GridPortalComponent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;

public class GroupManagementBrowser extends GridPortalComponent {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel treePanel = null;

	private JLabel jLabel = null;

	private JComboBox services = null;

	private JPanel contentPanel = null;

	private JLabel jLabel1 = null;

	private JComboBox credentials = null;

	private JScrollPane treePane = null;

	private JTree groupTree = null;

	/**
	 * This is the default constructor
	 */
	public GroupManagementBrowser() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(800, 500);
		this.setContentPane(getJContentPane());
		this.setTitle("Group Management Browser");
		this.setFrameIcon(GridGrouperLookAndFeel.getGroupIcon());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getMainPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.weighty = 1.0D;
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 0.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getTreePanel(), gridBagConstraints);
			mainPanel.add(getContentPanel(), gridBagConstraints3);
		}
		return mainPanel;
	}

	/**
	 * This method initializes treePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTreePanel() {
		if (treePanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 4;
			gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints6.weightx = 1.0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 3;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints5.weightx = 1.0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 2;
			jLabel1 = new JLabel();
			jLabel1.setText("Credentials");
			jLabel1.setFont(new Font("Dialog", Font.BOLD, 14));
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.weightx = 1.0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints1.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Grid Grouper");
			jLabel.setFont(new Font("Dialog", Font.BOLD, 14));
			treePanel = new JPanel();
			treePanel.setLayout(new GridBagLayout());
			treePanel.add(jLabel, gridBagConstraints1);
			treePanel.add(getServices(), gridBagConstraints2);
			treePanel.add(jLabel1, gridBagConstraints4);
			treePanel.add(getCredentials(), gridBagConstraints5);
			treePanel.add(getTreePane(), gridBagConstraints6);
		}
		return treePanel;
	}

	/**
	 * This method initializes services
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getServices() {
		if (services == null) {
			services = new GridGrouperServiceList();
		}
		return services;
	}

	/**
	 * This method initializes contentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());
		}
		return contentPanel;
	}

	/**
	 * This method initializes credentials
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCredentials() {
		if (credentials == null) {
			credentials = new ProxyComboBox(true);
		}
		return credentials;
	}

	/**
	 * This method initializes treePane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTreePane() {
		if (treePane == null) {
			treePane = new JScrollPane();
			treePane.setViewportView(getGroupTree());
		}
		return treePane;
	}

	/**
	 * This method initializes groupTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getGroupTree() {
		if (groupTree == null) {
			groupTree = new JTree();
		}
		return groupTree;
	}

}
