package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
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
		setCellRenderer(new CellRenderer());
		model = new DefaultTreeModel(new DefaultMutableTreeNode());
		setModel(model);
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				CheckBoxTreeNode checkNode = getCurrentNode();
				if (checkNode != null) {
					Rectangle checkBounds = checkNode.getCheckBox().getBounds();
					if (checkBounds.contains(e.getPoint())) {
						checkNode.getCheckBox().doClick();
					}
				}
			}
		});
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
	
	
	private CheckBoxTreeNode getCurrentNode() {
		if (getSelectionPath() != null) {
			return (CheckBoxTreeNode) getSelectionPath().getLastPathComponent();
		}
		return null;
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
				CheckBoxTreeNode cbNode = (CheckBoxTreeNode) value;
				cbNode.getCheckBox().setText(cbNode.toString());
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
			return checkNode.getCheckBox();
		}
	}
}
