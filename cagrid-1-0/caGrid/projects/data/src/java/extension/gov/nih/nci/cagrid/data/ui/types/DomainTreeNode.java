package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultTreeModel;


/** 
 *  DomainTreeNode
 *  Node in the TargetTypesTree to represent a domain model.  
 *  Children are all TypeTreeNodes
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 20, 2006 
 * @version $Id$ 
 */
public class DomainTreeNode extends CheckBoxTreeNode {
	
	private NamespaceType namespace;
	private TargetTypesTree parentTree;

	public DomainTreeNode(TargetTypesTree tree, NamespaceType namespace) {
		super();
		this.parentTree = tree;
		this.namespace = namespace;
		setUserObject(namespace.getNamespace());
		// add child nodes
		SchemaElementType[] types = namespace.getSchemaElement();
		if (types != null) {
			ChangeListener childListener = new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (allChildrenChecked()) {
						getCheckBox().setSelected(true);
					}
					if (noChildrenChecked()) {
						getCheckBox().setSelected(false);
					}
					// tell everybody that the type selection has been changed
					parentTree.fireTypeSelectionChanged();
				}
			};
			// add the nodes
			for (int i = 0; i < types.length; i++) {
				TypeTreeNode node = new TypeTreeNode(types[i]);
				node.getCheckBox().addChangeListener(childListener);
				add(node);
			}
		}
		// add listener to turn all children's check boxes on / off
		getCheckBox().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int childCount = getChildCount();
				for (int i = 0; i < childCount; i++) {
					TypeTreeNode node = (TypeTreeNode) getChildAt(i);
					node.getCheckBox().setSelected(isChecked());
					((DefaultTreeModel) parentTree.getModel()).nodeChanged(node);
				}
			}
		});
	}
	
	
	public NamespaceType getNamespace() {
		return this.namespace;
	}
	
	
	private boolean allChildrenChecked() {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			TypeTreeNode node = (TypeTreeNode) getChildAt(i);
			if (!node.isChecked()) {
				return false;
			}
		}
		return true;
	}
	
	
	private boolean noChildrenChecked() {
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
