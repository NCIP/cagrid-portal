package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import java.awt.Component;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
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
	private NamespaceType namespaceType;
	private List typeSelectionListeners;
	
	public TargetTypesTree() {
		super();
		typeSelectionListeners = new LinkedList();
		setEditable(true);
		setCellRenderer(new CellRenderer());
		setCellEditor(new CellEditor());
		setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
		setRootVisible(false); // until namespaces are added
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
	
	
	public void setNamespace(NamespaceType ns) {
		DomainTreeNode domainNode = new DomainTreeNode(this, ns);
		setModel(new DefaultTreeModel(domainNode));
		setRootVisible(true);
		this.namespaceType = ns;
	}
	
	
	public void checkSchemaNodes() {
		DomainTreeNode domainNode = (DomainTreeNode) ((DefaultTreeModel) getModel()).getRoot();
		if (domainNode != null) {
			for (int i = 0; i < domainNode.getChildCount(); i++) {
				TypeTreeNode typeNode = (TypeTreeNode) domainNode.getChildAt(i);
				typeNode.getCheckBox().setSelected(true);
			}
		}
	}
	
	
	public NamespaceType getOriginalNamespace() {
		return namespaceType;
	}
	
	
	public SchemaElementType[] getCheckedTypes() {
		List selected = new ArrayList();
		DomainTreeNode domainNode = (DomainTreeNode) ((DefaultTreeModel) getModel()).getRoot();
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
	
	
	/**
	 * Creates a new namespace type from the one loaded into the tree
	 * and the user's selection of schema element types to expose
	 * @return
	 */
	public NamespaceType getUserDefinedNamespace() {
		NamespaceType ns = new NamespaceType();
		ns.setLocation(namespaceType.getLocation());
		ns.setNamespace(namespaceType.getNamespace());
		ns.setPackageName(namespaceType.getPackageName());
		ns.setSchemaElement(getCheckedTypes());
		return ns;
	}
	
	
	public void addTypeSelectionListener(TypeSelectionListener listener) {
		typeSelectionListeners.add(listener);
	}
	
	
	public boolean removeTypeSelectionListener(TypeSelectionListener listener) {
		return typeSelectionListeners.remove(listener);
	}
	
	
	protected void fireTypeSelectionAdded(SchemaElementType addedType) {
		Iterator listenerIter = typeSelectionListeners.iterator();
		TypeSelectionEvent event = null;
		while (listenerIter.hasNext()) {
			if (event == null) {
				event = new TypeSelectionEvent(this, addedType);
			}
			((TypeSelectionListener) listenerIter.next()).typeSelectionAdded(event);
		}
	}
	
	
	protected void fireTypeSelectionRemoved(SchemaElementType removedType) {
		Iterator listenerIter = typeSelectionListeners.iterator();
		TypeSelectionEvent event = null;
		while (listenerIter.hasNext()) {
			if (event == null) {
				event = new TypeSelectionEvent(this, removedType);
			}
			((TypeSelectionListener) listenerIter.next()).typeSelectionRemoved(event);
		}
	}
	
	
	private static class CellRenderer extends DefaultTreeCellRenderer {
		
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
	
	
	private static class CellEditor extends DefaultCellEditor {
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
