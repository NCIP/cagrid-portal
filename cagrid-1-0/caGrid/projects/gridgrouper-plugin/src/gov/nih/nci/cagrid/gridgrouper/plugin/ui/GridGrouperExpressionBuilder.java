package gov.nih.nci.cagrid.gridgrouper.plugin.ui;

import gov.nih.nci.cagrid.common.portal.MultiEventProgressBar;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.LogicalOperator;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipExpression;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipQuery;
import gov.nih.nci.cagrid.gridgrouper.bean.Predicate;
import gov.nih.nci.cagrid.gridgrouper.ui.GridGrouperLookAndFeel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

public class GridGrouperExpressionBuilder extends JPanel {
	
	private static final String EXPRESSION_EDITOR = "ExpressionEditor";  //  @jve:decl-index=0:
	private static final String QUERY_EDITOR = "ExpressionEditor";  //  @jve:decl-index=0:

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
		this.expression = getExpression(false, in1Not2,in2Not1);
		//this.expression = new MembershipExpression();
		//this.expression.setLogicRelation(LogicalOperator.AND);
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
	
	private MembershipExpression getExpression(boolean and,MembershipExpression exp1, MembershipExpression exp2) {
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
			query[0].setPredicate(Predicate.IN);
		} else {
			query[0].setPredicate(Predicate.NOT_IN);
		}

		query[1] = new MembershipQuery();
		query[1].setGroupIdentifier(grp2);
		if (in2) {
			query[1].setPredicate(Predicate.IN);
		} else {
			query[1].setPredicate(Predicate.NOT_IN);
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
			expressionTree = new ExpressionTree(this,expression);
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
			editorPanel.add(getExpressionEditor(), getExpressionEditor().getName());
			editorPanel.add(getQueryEditor(), getQueryEditor().getName());
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
			expressionEditor = new JPanel();
			expressionEditor.setLayout(new GridBagLayout());
			expressionEditor.setName(EXPRESSION_EDITOR);
			expressionEditor.setBorder(BorderFactory.createTitledBorder(null,
					"Edit Membership Expression",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), new Color(62, 109, 181)));
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
			queryEditor = new JPanel();
			queryEditor.setLayout(new GridBagLayout());
			queryEditor.setName(QUERY_EDITOR);
			queryEditor.setBorder(BorderFactory.createTitledBorder(null,
					"Edit Membership Query",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), new Color(62, 109, 181)));
		}
		return queryEditor;
	}

}
