package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/** 
 *  TargetTypesTree
 *  Tree for showing and selecting types to expose via a data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 20, 2006 
 * @version $Id$ 
 */
public class TargetTypesTree extends JTree {
	
	public TargetTypesTree() {
		super();
		setCellRenderer(new CellRenderer());
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
	
	
	public void setNamespace(NamespaceType ns) {
		DomainTreeNode domainNode = new DomainTreeNode(ns);
		setModel(new DefaultTreeModel(domainNode));
	}
	
	
	public SerializationMapping getSelectedMapping() {
		if (getSelectionPath() != null && getSelectionPath().getLastPathComponent() instanceof TypeTreeNode) {
			TypeTreeNode typeNode = (TypeTreeNode) getSelectionPath().getLastPathComponent();
			DomainTreeNode domainNode = (DomainTreeNode) typeNode.getParent();
			SerializationMapping mapping = new SerializationMapping(domainNode.getNamespace(), typeNode.getType());
			return mapping;
		}
		return null;
	}
	
	
	private class CellRenderer extends DefaultTreeCellRenderer {
		
		public CellRenderer() {
			
		}
		
		
		public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row, boolean focused) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, focused);
			if (value instanceof CheckBoxTreeNode) {
				JPanel panel = new JPanel();
				CheckBoxTreeNode cbNode = (CheckBoxTreeNode) value;
				panel.add(cbNode.getCheckBox());
				panel.add(this);
				return panel;
			}
			return this;
		}
	}
}
