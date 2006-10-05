package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.data.ui.tree.CheckBoxTreeNode;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;


/** 
 *  TypeTreeNode
 *  Node in the TargetTypesTree to represent and allow selection of 
 *  schema element types
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 20, 2006 
 * @version $Id$ 
 */
public class TypeTreeNode extends CheckBoxTreeNode {
	
	private SchemaElementType type;
	
	public TypeTreeNode(TargetTypesTree tree, SchemaElementType type) {
		super(tree, type.getType());
		setAllowsChildren(false);
		this.type = type;
	}
	
	
	public SchemaElementType getType() {
		return type;
	}
}
