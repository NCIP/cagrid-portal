package org.cagrid.grape;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;



public class ConfigurationWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	
	private ConfigurationManager conf;

	private JPanel mainPanel = null;

	private JPanel treePanel = null;

	private JPanel displayPanel = null;

	private JScrollPane jScrollPane = null;

	private JTree configurationTree = null;

	/**
	 * @param owner
	 */
	public ConfigurationWindow(Frame owner, ConfigurationManager conf) throws Exception{
		super(owner);
		setModal(false);
		this.conf = conf;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() throws Exception{
		this.setSize(300, 200);
		this.setTitle("Preferences");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() throws Exception{
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
	private JPanel getMainPanel() throws Exception{
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getTreePanel(), gridBagConstraints);
			mainPanel.add(getDisplayPanel(), gridBagConstraints1);
		}
		return mainPanel;
	}

	/**
	 * This method initializes treePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTreePanel() throws Exception{
		if (treePanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.weightx = 1.0;
			treePanel = new JPanel();
			treePanel.setLayout(new GridBagLayout());
			treePanel.add(getJScrollPane(), gridBagConstraints2);
		}
		return treePanel;
	}

	/**
	 * This method initializes displayPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDisplayPanel() {
		if (displayPanel == null) {
			displayPanel = new JPanel();
			displayPanel.setLayout(new GridBagLayout());
		}
		return displayPanel;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() throws Exception{
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getConfigurationTree());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes configurationTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getConfigurationTree() throws Exception{
		if (configurationTree == null) {
			configurationTree = new ConfigurationTree(this,this.conf);
		}
		return configurationTree;
	}

}
