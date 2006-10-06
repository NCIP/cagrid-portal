package gov.nih.nci.cagrid.data.ui.types.umltree;

import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cagrid.data.ui.tree.CheckBoxTree;
import gov.nih.nci.cagrid.data.ui.tree.CheckBoxTreeNode;

/** 
 *  UMLCLassTreeNode
 *  Tree node to represent a UML Class from the caDSR
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 5, 2006 
 * @version $Id$ 
 */
public class UMLClassTreeNode extends CheckBoxTreeNode {
	private UMLClassMetadata classMd;

	public UMLClassTreeNode(CheckBoxTree parentTree, UMLClassMetadata classMd) {
		super(parentTree, classMd.getName());
		getCheckBox().setToolTipText(classMd.getFullyQualifiedName());
		this.classMd = classMd;
		this.setAllowsChildren(false);
	}
	
	
	public UMLClassMetadata getClassMetadata() {
		return classMd;
	}
}
