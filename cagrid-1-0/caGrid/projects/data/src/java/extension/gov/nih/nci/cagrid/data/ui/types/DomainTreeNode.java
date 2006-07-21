package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
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
	private Map checkBoxTypes;
	private Map typeCheckBoxes;

	public DomainTreeNode(TargetTypesTree tree, NamespaceType namespace) {
		super();
		this.parentTree = tree;
		this.namespace = namespace;
		this.checkBoxTypes = new HashMap();
		this.typeCheckBoxes = new HashMap();
		
		setUserObject(namespace.getNamespace());
		// add child nodes
		SchemaElementType[] types = namespace.getSchemaElement();
		if (types != null) {
			ItemListener childListener = new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (allChildrenChecked()) {
						getCheckBox().setSelected(true);
					}
					if (noChildrenChecked()) {
						getCheckBox().setSelected(false);
					}
					// tell everybody that the type selection has been changed
					JCheckBox checkBox = (JCheckBox) e.getSource();
					if (checkBox.isSelected()) {
						// TODO: get the node the check box belongs to
						parentTree.fireTypeSelectionAdded(getNamespace(), (SchemaElementType) checkBoxTypes.get(checkBox));
					} else {
						parentTree.fireTypeSelectionRemoved(getNamespace(), (SchemaElementType) checkBoxTypes.get(checkBox));
					}
				}
			};
			// add the nodes
			for (int i = 0; i < types.length; i++) {
				TypeTreeNode node = new TypeTreeNode(types[i]);
				node.getCheckBox().addItemListener(childListener);
				checkBoxTypes.put(node.getCheckBox(), node.getType());
				typeCheckBoxes.put(node.getType(), node.getCheckBox());
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
	
	
	public void checkTypeNodes(SchemaElementType[] types) {
		for (int i = 0; i < types.length; i++) {
			JCheckBox check = (JCheckBox) typeCheckBoxes.get(types[i]);
			if (check != null) {
				check.setSelected(true);
			}
		}
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
