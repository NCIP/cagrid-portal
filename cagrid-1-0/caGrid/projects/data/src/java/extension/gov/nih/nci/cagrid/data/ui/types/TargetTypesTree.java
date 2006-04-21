package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import java.awt.Component;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
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
	private DefaultTreeModel model;
	
	public TargetTypesTree() {
		super();
		setEditable(true);
		setCellRenderer(new CellRenderer());
		setCellEditor(new CellEditor());
		model = new DefaultTreeModel(new DefaultMutableTreeNode());
		setModel(model);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
	
	
	private CheckBoxTreeNode getCurrentNode() {
		if (getSelectionPath() != null) {
			return (CheckBoxTreeNode) getSelectionPath().getLastPathComponent();
		}
		return null;
	}
	
	
	public void setNamespace(NamespaceType ns) {
		DomainTreeNode domainNode = new DomainTreeNode(this, ns);
		setModel(new DefaultTreeModel(domainNode));
	}
	
	
	public SerializationMapping getSelectedMapping() {
		CheckBoxTreeNode currentNode = getCurrentNode();
		if (currentNode != null && currentNode instanceof TypeTreeNode) {
			TypeTreeNode typeNode = (TypeTreeNode) currentNode;
			DomainTreeNode domainNode = (DomainTreeNode) typeNode.getParent();
			SerializationMapping mapping = new SerializationMapping(domainNode.getNamespace(), typeNode.getType());
			return mapping;
		}
		return null;
	}
	
	
	public SchemaElementType[] getCheckedTypes() {
		List selected = new ArrayList();
		DomainTreeNode domainNode = (DomainTreeNode) model.getRoot();
		int childCount = domainNode.getChildCount();
		for (int i = 0; i < childCount; i++) {
			TypeTreeNode typeNode = (TypeTreeNode) domainNode.getChildAt(i);
			if (typeNode.isChecked()) {
				selected.add(typeNode.getType());
			}
		}		
		SchemaElementType[] types = new SchemaElementType[selected.size()];
		selected.toArray(types);
		return types;
	}
	
	
	private class CellRenderer extends DefaultTreeCellRenderer {
		
		public CellRenderer() {
			
		}
		
		
		public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row, boolean focused) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, focused);
			if (value instanceof CheckBoxTreeNode) {
				CheckBoxTreeNode cbNode = (CheckBoxTreeNode) value;
				cbNode.getCheckBox().setText(cbNode.toString());
				cbNode.getCheckBox().setForeground(getForeground());
				cbNode.getCheckBox().setBackground(getBackground());
				return cbNode.getCheckBox();
			}
			return this;
		}
	}
	
	
	private class CellEditor extends DefaultCellEditor {
		private JCheckBox check;
		
		public CellEditor() {
			super(new JCheckBox());
		}
		
		
		public Object getCellEditorValue() {
			return check;
		}
		
		
		public boolean isCellEditable(EventObject e) {
			return true;
		}
		
		
		public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
			CheckBoxTreeNode checkNode = (CheckBoxTreeNode) value;
			check = checkNode.getCheckBox();
			return check;
		}
	}
}
