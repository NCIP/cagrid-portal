package gov.nih.nci.cagrid.gridgrouper.plugin.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;

public class GridGrouperExpressionBuilder extends JPanel {

	private static final long serialVersionUID = 1L;

	private JSplitPane jSplitPane = null;

	private JPanel treePanel = null;

	private JPanel expressionPanel = null;

	private JScrollPane jScrollPane = null;

	private JTree grouperTree = null;

	private JScrollPane jScrollPane1 = null;

	private JTree expressionTree = null;

	/**
	 * This is the default constructor
	 */
	public GridGrouperExpressionBuilder() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.ipadx = 0;
		gridBagConstraints2.ipady = 0;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.weighty = 1.0;
		gridBagConstraints2.gridx = 1;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getJSplitPane(), gridBagConstraints2);
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setLeftComponent(getTreePanel());
			jSplitPane.setRightComponent(getExpressionPanel());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes treePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTreePanel() {
		if (treePanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			treePanel = new JPanel();
			treePanel.setLayout(new GridBagLayout());
			treePanel.add(getJScrollPane(), gridBagConstraints1);
		}
		return treePanel;
	}

	/**
	 * This method initializes expressionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getExpressionPanel() {
		if (expressionPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			expressionPanel = new JPanel();
			expressionPanel.setLayout(new GridBagLayout());
			expressionPanel.add(getJScrollPane1(), gridBagConstraints);
		}
		return expressionPanel;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getGrouperTree());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes grouperTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getGrouperTree() {
		if (grouperTree == null) {
			grouperTree = new JTree();
		}
		return grouperTree;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getExpressionTree());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes expressionTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getExpressionTree() {
		if (expressionTree == null) {
			expressionTree = new JTree();
		}
		return expressionTree;
	}

}
