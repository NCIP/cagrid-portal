package gov.nih.nci.cagrid.gridgrouper.plugin.ui;

import gov.nih.nci.cagrid.common.portal.MultiEventProgressBar;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.LogicalOperator;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipExpression;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipQuery;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipStatus;
import gov.nih.nci.cagrid.gridgrouper.client.Group;
import gov.nih.nci.cagrid.gridgrouper.ui.GridGrouperLookAndFeel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

public class GridGrouperExpressionBuilder extends JPanel {

	private static final String EXPRESSION_EDITOR = "ExpressionEditor"; // @jve:decl-index=0:

	private static final String QUERY_EDITOR = "QueryEditor"; // @jve:decl-index=0:

	private static final long serialVersionUID = 1L;

	private JSplitPane jSplitPane = null;

	private JPanel treePanel = null;

	private JPanel expressionPanel = null;

	private JScrollPane jScrollPane = null;

	private GridGrouperTree grouperTree = null;

	private JScrollPane jScrollPane1 = null;

	private ExpressionTree expressionTree = null;

	private MultiEventProgressBar progress = null;

	private MembershipExpression expression;

	private JPanel editorPanel = null;

	private CardLayout editorLayout = null;

	private JPanel expressionEditor = null;

	private JPanel queryEditor = null;

	private JPanel expressionProperties = null;

	private JLabel jLabel = null;

	private JComboBox logicalRelation = null;

	private JPanel expressionButtons = null;

	private JButton addExpression = null;

	private JButton addGroup = null;

	private JButton removeExpression = null;

	private JPanel queryProperties = null;

	private JPanel queryButtons = null;

	private JButton removeGroup = null;

	private JLabel jLabel1 = null;

	private JComboBox membership = null;

	/**
	 * This is the default constructor
	 */
	public GridGrouperExpressionBuilder() {
		super();
		GroupIdentifier group1 = getGroupIdentifier("Group A");
		GroupIdentifier group2 = getGroupIdentifier("Group B");
		MembershipExpression in1Not2 = getExpression(true, group1, true,
				group2, false);
		MembershipExpression in2Not1 = getExpression(true, group1, false,
				group2, true);
		this.expression = getExpression(false, in1Not2, in2Not1);
		// this.expression = new MembershipExpression();
		// this.expression.setLogicRelation(LogicalOperator.AND);
		initialize();
	}

	public GridGrouperExpressionBuilder(MembershipExpression expression) {
		super();
		this.expression = expression;
		initialize();
	}

	private static GroupIdentifier getGroupIdentifier(String groupName) {
		GroupIdentifier id = new GroupIdentifier();
		id.setGroupName(groupName);
		return id;
	}

	private MembershipExpression getExpression(boolean and,
			MembershipExpression exp1, MembershipExpression exp2) {
		MembershipExpression[] expression = new MembershipExpression[2];
		expression[0] = exp1;
		expression[1] = exp2;
		MembershipExpression exp = new MembershipExpression();
		exp.setMembershipExpression(expression);
		if (and) {
			exp.setLogicRelation(LogicalOperator.AND);
		} else {
			exp.setLogicRelation(LogicalOperator.OR);
		}
		return exp;
	}

	private MembershipExpression getExpression(boolean and,
			GroupIdentifier grp1, boolean in1, GroupIdentifier grp2, boolean in2) {
		MembershipQuery[] query = new MembershipQuery[2];
		query[0] = new MembershipQuery();
		query[0].setGroupIdentifier(grp1);
		if (in1) {
			query[0].setMembershipStatus(MembershipStatus.MEMBER_OF);
		} else {
			query[0].setMembershipStatus(MembershipStatus.NOT_MEMBER_OF);
		}

		query[1] = new MembershipQuery();
		query[1].setGroupIdentifier(grp2);
		if (in2) {
			query[1].setMembershipStatus(MembershipStatus.MEMBER_OF);
		} else {
			query[1].setMembershipStatus(MembershipStatus.NOT_MEMBER_OF);
		}

		MembershipExpression exp = new MembershipExpression();
		exp.setMembershipQuery(query);
		if (and) {
			exp.setLogicRelation(LogicalOperator.AND);
		} else {
			exp.setLogicRelation(LogicalOperator.OR);
		}
		return exp;
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
		this.setSize(500, 300);
		this.setLayout(new GridBagLayout());
		this.add(getJSplitPane(), gridBagConstraints2);
		setExpressionEditor(expression);
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
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.insets = new Insets(2, 10, 2, 10);
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			treePanel = new JPanel();
			treePanel.setLayout(new GridBagLayout());
			treePanel.add(getProgress(), gridBagConstraints3);
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
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.ipadx = 0;
			gridBagConstraints4.ipady = 0;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.ipady = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			expressionPanel = new JPanel();
			expressionPanel.setLayout(new GridBagLayout());
			expressionPanel.add(getJScrollPane1(), gridBagConstraints);
			expressionPanel.add(getEditorPanel(), gridBagConstraints4);
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
	protected GridGrouperTree getGrouperTree() {
		if (grouperTree == null) {
			grouperTree = new GridGrouperTree(this);
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
			jScrollPane1.setName("jScrollPane1");
			jScrollPane1.setViewportView(getExpressionTree());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes expressionTree
	 * 
	 * @return javax.swing.JTree
	 */
	protected ExpressionTree getExpressionTree() {
		if (expressionTree == null) {
			expressionTree = new ExpressionTree(this, expression);
		}
		return expressionTree;
	}

	/**
	 * This method initializes progress
	 * 
	 * @return javax.swing.JProgressBar
	 */
	protected MultiEventProgressBar getProgress() {
		if (progress == null) {
			progress = new MultiEventProgressBar(false);
			progress.setForeground(GridGrouperLookAndFeel.getPanelLabelColor());
			progress.setString("");
			progress.setStringPainted(true);
		}
		return progress;
	}

	/**
	 * This method initializes editorPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getEditorPanel() {
		if (editorPanel == null) {
			editorPanel = new JPanel();
			editorLayout = new CardLayout();
			editorPanel.setLayout(editorLayout);
			editorPanel.setName("editorPanel");
			editorPanel.add(getExpressionEditor(), EXPRESSION_EDITOR);
			editorPanel.add(getQueryEditor(), QUERY_EDITOR);
		}
		return editorPanel;
	}

	/**
	 * This method initializes expressionEditor
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getExpressionEditor() {
		if (expressionEditor == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.weightx = 1.0D;
			gridBagConstraints8.gridy = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.weightx = 1.0D;
			gridBagConstraints5.gridy = 0;
			expressionEditor = new JPanel();
			expressionEditor.setLayout(new GridBagLayout());
			expressionEditor.setName(EXPRESSION_EDITOR);
			expressionEditor.setBorder(BorderFactory.createTitledBorder(null,
					"Edit Membership Expression",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), new Color(62, 109, 181)));
			expressionEditor
					.add(getExpressionProperties(), gridBagConstraints5);
			expressionEditor.add(getExpressionButtons(), gridBagConstraints8);
		}
		return expressionEditor;
	}

	/**
	 * This method initializes queryEditor
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getQueryEditor() {
		if (queryEditor == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.gridx = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.weightx = 1.0D;
			gridBagConstraints9.gridy = 0;
			queryEditor = new JPanel();
			queryEditor.setLayout(new GridBagLayout());
			queryEditor.setName(QUERY_EDITOR);
			queryEditor.setBorder(BorderFactory.createTitledBorder(null,
					"Edit Membership Query",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), new Color(62, 109, 181)));
			queryEditor.add(getQueryProperties(), gridBagConstraints9);
			queryEditor.add(getQueryButtons(), gridBagConstraints10);
		}
		return queryEditor;
	}

	public void setExpressionEditor(MembershipExpression exp) {
		this.getLogicalRelation().setSelectedItem(exp.getLogicRelation());
		this.editorLayout.show(getEditorPanel(), EXPRESSION_EDITOR);
		repaint();
	}

	public void setExpressionQuery(MembershipQuery query) {
		this.getMembership().setSelectedItem(query.getMembershipStatus());
		this.editorLayout.show(getEditorPanel(), QUERY_EDITOR);
	}

	/**
	 * This method initializes expressionProperties
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getExpressionProperties() {
		if (expressionProperties == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints6.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Logical Operator");
			expressionProperties = new JPanel();
			expressionProperties.setLayout(new GridBagLayout());
			expressionProperties.add(jLabel, gridBagConstraints6);
			expressionProperties.add(getLogicalRelation(), gridBagConstraints7);
		}
		return expressionProperties;
	}

	/**
	 * This method initializes logicalRelation
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getLogicalRelation() {
		if (logicalRelation == null) {
			logicalRelation = new JComboBox();
			logicalRelation
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							BaseTreeNode node = getExpressionTree()
									.getCurrentNode();
							if (node instanceof ExpressionNode) {
								ExpressionNode en = (ExpressionNode) node;
								en.getExpression().setLogicRelation(
										(LogicalOperator) getLogicalRelation()
												.getSelectedItem());
								en.refresh();
							}
						}
					});
			logicalRelation.addItem(LogicalOperator.AND);
			logicalRelation.addItem(LogicalOperator.OR);
		}
		return logicalRelation;
	}

	/**
	 * This method initializes expressionButtons
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getExpressionButtons() {
		if (expressionButtons == null) {
			expressionButtons = new JPanel();
			expressionButtons.setLayout(new FlowLayout());
			expressionButtons.add(getAddExpression(), null);
			expressionButtons.add(getAddGroup(), null);
			expressionButtons.add(getRemoveExpression(), null);
		}
		return expressionButtons;
	}

	/**
	 * This method initializes addExpression
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddExpression() {
		if (addExpression == null) {
			addExpression = new JButton();
			addExpression.setText("Add Expression");
			addExpression
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

						}
					});
		}
		return addExpression;
	}

	/**
	 * This method initializes addGroup
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddGroup() {
		if (addGroup == null) {
			addGroup = new JButton();
			addGroup.setText("Add Group");
			addGroup.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					DefaultMutableTreeNode currentNode = getGrouperTree()
							.getCurrentNode();
					if (currentNode instanceof GroupTreeNode) {
						GroupTreeNode grp = (GroupTreeNode) currentNode;
						addGroupToCurrentExpression(grp.getGroup());
					} else {
						PortalUtils
								.showErrorMessage("Please select a group to add!!!");
					}
				}
			});
		}
		return addGroup;
	}

	/**
	 * This method initializes removeExpression
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveExpression() {
		if (removeExpression == null) {
			removeExpression = new JButton();
			removeExpression.setText("Remove");
		}
		return removeExpression;
	}

	/**
	 * This method initializes queryProperties
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getQueryProperties() {
		if (queryProperties == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.weightx = 1.0;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.gridy = 0;
			jLabel1 = new JLabel();
			jLabel1.setText("Membership");
			queryProperties = new JPanel();
			queryProperties.setLayout(new GridBagLayout());
			queryProperties.setName("jPanel");
			queryProperties.add(jLabel1, gridBagConstraints12);
			queryProperties.add(getMembership(), gridBagConstraints11);
		}
		return queryProperties;
	}

	/**
	 * This method initializes queryButtons
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getQueryButtons() {
		if (queryButtons == null) {
			queryButtons = new JPanel();
			queryButtons.setLayout(new FlowLayout());
			queryButtons.add(getRemoveGroup(), null);
		}
		return queryButtons;
	}

	/**
	 * This method initializes removeGroup
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveGroup() {
		if (removeGroup == null) {
			removeGroup = new JButton();
			removeGroup.setText("Remove");
		}
		return removeGroup;
	}

	/**
	 * This method initializes membership
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getMembership() {
		if (membership == null) {
			membership = new JComboBox();
			membership.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					BaseTreeNode node = getExpressionTree().getCurrentNode();
					if (node instanceof QueryNode) {
						QueryNode n = (QueryNode) node;
						n.getQuery().setMembershipStatus(
								(MembershipStatus) getMembership()
										.getSelectedItem());
						n.refresh();
					}
				}
			});
			membership.addItem(MembershipStatus.MEMBER_OF);
			membership.addItem(MembershipStatus.NOT_MEMBER_OF);
		}
		return membership;
	}

	protected void addGroupToCurrentExpression(Group grp) {
		DefaultMutableTreeNode currentNode = this.expressionTree
				.getCurrentNode();
		if (currentNode instanceof ExpressionNode) {
			ExpressionNode exp = (ExpressionNode) currentNode;
			exp.addGroup(grp);
		} else {
			PortalUtils
					.showErrorMessage("Please select an expression in which to add the group!!!");
		}
	}
}
