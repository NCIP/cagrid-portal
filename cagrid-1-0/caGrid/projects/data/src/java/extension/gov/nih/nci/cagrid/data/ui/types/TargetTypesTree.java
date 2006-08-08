package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Enumeration;
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
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
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
	private DefaultMutableTreeNode rootNode;
	private List typeSelectionListeners;
	
	public TargetTypesTree() {
		super();
		typeSelectionListeners = new LinkedList();
		setCellRenderer(new CellRenderer());
		setCellEditor(new CellEditor());
		setEditable(true);
		setShowsRootHandles(true);
		this.rootNode = new DefaultMutableTreeNode();
		this.model = new DefaultTreeModel(rootNode);
		setModel(model);
		setRootVisible(false); // namespaces attach to invisible root node
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
	
	
	public void addNamespaceType(NamespaceType ns) {
		DomainTreeNode domainNode = new DomainTreeNode(this, ns);
		rootNode.add(domainNode);
		model.reload(rootNode);
		if (domainNode.getChildCount() != 0) {
			TreeNode child = domainNode.getChildAt(0);
			TreePath path = new TreePath(model.getPathToRoot(child));
			makeVisible(path);
		}
	}
	
	
	public void clearTree() {
		rootNode.removeAllChildren();
	}
	
	
	public void checkTypeNodes(NamespaceType ns, SchemaElementType[] types) {
		for (int i = 0; i < rootNode.getChildCount(); i++) {
			DomainTreeNode domainNode = (DomainTreeNode) rootNode.getChildAt(i);
			if (domainNode.getNamespace().equals(ns)) {
				domainNode.checkTypeNodes(types);
				break;
			}
		}
	}
	
	
	public NamespaceType[] getNamespaceTypes() {
		NamespaceType[] namespaces = new NamespaceType[rootNode.getChildCount()];
		for (int i = 0; i < rootNode.getChildCount(); i++) {
			namespaces[i] = ((DomainTreeNode) rootNode.getChildAt(i)).getNamespace();
		}
		return namespaces;
	}
	
	
	public SchemaElementType[] getCheckedTypes(NamespaceType namespace) {
		List selected = new ArrayList();
		// find the namespace node
		for (int i = 0; i < rootNode.getChildCount(); i++) {
			DomainTreeNode domainNode = (DomainTreeNode) rootNode.getChildAt(i);
			if (domainNode.getNamespace().getNamespace().equals(namespace.getNamespace())) {
				int childCount = domainNode.getChildCount();
				for (int j = 0; j < childCount; j++) {
					TypeTreeNode typeNode = (TypeTreeNode) domainNode.getChildAt(j);
					if (typeNode.isChecked()) {
						selected.add(typeNode.getType());
					}
				}
				break;
			}
		}
		SchemaElementType[] types = new SchemaElementType[selected.size()];
		selected.toArray(types);
		return types;
	}
	
	
	public SchemaElementType[] getAllCheckedTypes() {
		List selected = new ArrayList();
		Enumeration nodes = rootNode.depthFirstEnumeration();
		while (nodes.hasMoreElements()) {
			TreeNode treeNode = (TreeNode) nodes.nextElement();
			if (treeNode instanceof TypeTreeNode) {
				TypeTreeNode typeNode = (TypeTreeNode) treeNode;
				if (typeNode.isChecked()) {
					selected.add(typeNode.getType());
				}
			}
		}
		SchemaElementType[] types = new SchemaElementType[selected.size()];
		selected.toArray(types);
		return types;
	}
	
	
	public void addTypeSelectionListener(TypeSelectionListener listener) {
		typeSelectionListeners.add(listener);
	}
	
	
	public boolean removeTypeSelectionListener(TypeSelectionListener listener) {
		return typeSelectionListeners.remove(listener);
	}
	
	
	protected void fireTypeSelectionAdded(NamespaceType namespace, SchemaElementType addedType) {
		Iterator listenerIter = typeSelectionListeners.iterator();
		TypeSelectionEvent event = null;
		while (listenerIter.hasNext()) {
			if (event == null) {
				event = new TypeSelectionEvent(this, namespace, addedType);
			}
			((TypeSelectionListener) listenerIter.next()).typeSelectionAdded(event);
		}
	}
	
	
	protected void fireTypeSelectionRemoved(NamespaceType namespace, SchemaElementType removedType) {
		Iterator listenerIter = typeSelectionListeners.iterator();
		TypeSelectionEvent event = null;
		while (listenerIter.hasNext()) {
			if (event == null) {
				event = new TypeSelectionEvent(this, namespace, removedType);
			}
			((TypeSelectionListener) listenerIter.next()).typeSelectionRemoved(event);
		}
	}
	
	
	private static class CellRenderer extends DefaultTreeCellRenderer {
		
		public CellRenderer() {
			// super();
		}
		
		
		public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row, boolean focused) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, focused);
			if (value instanceof CheckBoxTreeNode) {
				CheckBoxTreeNode cbNode = (CheckBoxTreeNode) value;
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
			if (value instanceof CheckBoxTreeNode) {
				CheckBoxTreeNode checkNode = (CheckBoxTreeNode) value;
				check = checkNode.getCheckBox();
				return check;
			} else {
				return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
			}
		}
	}
}
