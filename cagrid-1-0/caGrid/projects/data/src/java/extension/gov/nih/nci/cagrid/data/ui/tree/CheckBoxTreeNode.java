package gov.nih.nci.cagrid.data.ui.tree;

import gov.nih.nci.cagrid.data.ui.types.TypeTreeNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/** 
 *  CheckBoxTreeNode
 *  Tree node that has a check box
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 20, 2006 
 * @version $Id$ 
 */
public abstract class CheckBoxTreeNode extends DefaultMutableTreeNode {

	private CheckBoxTree parentTree;
	private JCheckBox check;
	
	public CheckBoxTreeNode(CheckBoxTree parentTree, String nodeText) {
		super();
		this.parentTree = parentTree;;
		setUserObject(nodeText);
		
		
		
		
		
		
		parentTree.getModel().addTreeModelListener(new TreeModelListener() {
			public void treeNodesChanged(TreeModelEvent e) {
				// ignore this
			}
			
			
			public void treeNodesInserted(TreeModelEvent e) {
				// is the node in question a child?
				if (e.getTreePath().getParentPath() != null 
					&& e.getTreePath().getParentPath().getLastPathComponent() == CheckBoxTreeNode.this) {
					TreeNode[] inserted = (TreeNode[]) e.getChildren();
					for (int i = 0; i < inserted.length; i++) {
						
					}
				}
			}
			
			
			public void treeNodesRemoved(TreeModelEvent e) {
				// is the node in question a child?
				if (e.getTreePath().getParentPath() != null 
					&& e.getTreePath().getLastPathComponent() == CheckBoxTreeNode.this) {
					
				}
			}
			
			
			public void treeStructureChanged(TreeModelEvent e) {
				
			}
		});
	}
	
	
	public JCheckBox getCheckBox() {
		if (check == null) {
			check = new JCheckBox((String) getUserObject());
			// add listener to turn all children's check boxes on / off
			check.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int childCount = getChildCount();
					for (int i = 0; i < childCount; i++) {
						TypeTreeNode node = (TypeTreeNode) getChildAt(i);
						node.getCheckBox().setSelected(isChecked());
						parentTree.getTreeModel().nodeChanged(node);
					}
				}
			});
			check.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					// repaint the node when it changes
					parentTree.getTreeModel().nodeChanged(CheckBoxTreeNode.this);
					// inform everyone that a node has been checked / unchecked
					if (check.isSelected()) {
						parentTree.fireNodeChecked(CheckBoxTreeNode.this);
					} else {
						parentTree.fireNodeUnchecked(CheckBoxTreeNode.this);
					}
				}
			});
		}
		return check;
	}
	
	
	public boolean isChecked() {
		return check.isSelected();
	}
	
	
	public CheckBoxTreeNode[] getCheckedChildren() {
		List checked = new ArrayList();
		Enumeration childEnum = children();
		while (childEnum.hasMoreElements()) {
			Object node = childEnum.nextElement();
			if (node instanceof CheckBoxTreeNode) {
				if (((CheckBoxTreeNode) node).isChecked()) {
					checked.add(node);
				}
			}
		}
		CheckBoxTreeNode[] checkedNodes = new CheckBoxTreeNode[checked.size()];
		checked.toArray(checkedNodes);
		return checkedNodes;
	}
	
	
	public boolean allChildrenChecked() {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			TypeTreeNode node = (TypeTreeNode) getChildAt(i);
			if (!node.isChecked()) {
				return false;
			}
		}
		return true;
	}
	
	
	public boolean noChildrenChecked() {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			TypeTreeNode node = (TypeTreeNode) getChildAt(i);
			if (node.isChecked()) {
				return false;
			}
		}
		return true;
	}
}
