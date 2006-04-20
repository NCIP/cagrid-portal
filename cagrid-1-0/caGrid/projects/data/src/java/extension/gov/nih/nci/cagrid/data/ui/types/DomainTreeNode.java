package gov.nih.nci.cagrid.data.ui.types;

import java.awt.Color;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;


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

	public DomainTreeNode(NamespaceType namespace) {
		super();
		this.namespace = namespace;
		setUserObject(namespace.getNamespace());
		// add child nodes
		SchemaElementType[] types = namespace.getSchemaElement();
		if (types != null) {
			// change listener to grey out the check box
			ChangeListener childChangeListener = new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (!allChildrenChecked()) {
						getCheckBox().setBackground(Color.LIGHT_GRAY);
					} else {
						getCheckBox().setBackground(Color.WHITE);
					}
				}
			};
			for (int i = 0; i < types.length; i++) {
				TypeTreeNode node = new TypeTreeNode(types[i]);
				node.getCheckBox().addChangeListener(childChangeListener);
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
}
