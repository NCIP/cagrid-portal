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
import java.awt.CardLayout;
import javax.swing.JSplitPane;
import java.awt.Dimension;

public class ConfigurationWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private ConfigurationManager conf;

	private JPanel treePanel = null;

	private JPanel displayPanel = null;

	private JScrollPane jScrollPane = null;

	private JTree configurationTree = null;

	private CardLayout displayLayout;

	private JSplitPane jSplitPane = null;

	/**
	 * @param owner
	 */
	public ConfigurationWindow(Frame owner, ConfigurationManager conf)
			throws Exception {
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
	private void initialize() throws Exception {
		this.setSize(300, 200);
		this.setTitle("Preferences");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() throws Exception {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes treePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTreePanel() throws Exception {
		if (treePanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
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
			displayLayout = new CardLayout();
			displayPanel = new JPanel(displayLayout);
		}
		return displayPanel;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() throws Exception {
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
	private JTree getConfigurationTree() throws Exception {
		if (configurationTree == null) {
			configurationTree = new ConfigurationTree(this, this.conf);
		}
		return configurationTree;
	}

	protected void addDisplayPanel(String name, JPanel panel) {
		System.out.println("Adding " + name);
		displayPanel.add(name, panel);
	}

	protected void showDisplayPanel(String name) {
		System.out.println("Showing " + name);
		displayLayout.show(displayPanel, name);
		validate();
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() throws Exception{
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerLocation(200);
			getDisplayPanel();
			jSplitPane.setLeftComponent(getTreePanel());
			jSplitPane.setRightComponent(getDisplayPanel());
			
		}
		return jSplitPane;
	}
}
