package gov.nih.nci.cagrid.data.ui.types.umltree;

import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.data.ui.tree.CheckBoxTree;
import gov.nih.nci.cagrid.data.ui.tree.CheckBoxTreeNode;

/** 
 *  UMLPackageTreeNode
 *  Node to show and manage UML package metadata from the caDSR
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 5, 2006 
 * @version $Id$ 
 */
public class UMLPackageTreeNode extends CheckBoxTreeNode {
	private UMLPackageMetadata pack;

	public UMLPackageTreeNode(CheckBoxTree parentTree, UMLPackageMetadata pack) {
		super(parentTree, pack.getName());
		getCheckBox().setToolTipText(pack.getDescription());
		this.pack = pack;
	}
	
	
	public UMLPackageMetadata getPackage() {
		return pack;
	}
	
	
	public UMLClassMetadata[] getSelectedClasses() {
		CheckBoxTreeNode[] checked = getCheckedChildren();
		UMLClassMetadata[] md = new UMLClassMetadata[checked.length];
		for (int i = 0; i < checked.length; i++) {
			md[i] = ((UMLCLassTreeNode) checked[i]).getClassMetadata();
		}
		return md;
	}
}
