package gov.nih.nci.cagrid.data.ui.types.umltree;

import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.data.ui.tree.CheckBoxTree;

import java.util.HashMap;
import java.util.Map;

/** 
 *  UMLProjectTree
 *  Tree for describing and selecting items from caDSR UML Projects
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 5, 2006 
 * @version $Id$ 
 */
public class UMLProjectTree extends CheckBoxTree {
	private Map packageNodes;

	public UMLProjectTree() {
		super();
		packageNodes = new HashMap();
	}
	
	
	public void addUmlPackage(UMLPackageMetadata pack) {
		UMLPackageTreeNode packNode = new UMLPackageTreeNode(this, pack);
		packageNodes.put(pack.getName(), packNode);
		getRootNode().add(packNode);
		reloadFromRoot();
	}
	
	
	public void addUmlClass(UMLPackageMetadata pack, UMLClassMetadata clazz) {
		// find the class node
		UMLPackageTreeNode packNode = (UMLPackageTreeNode) packageNodes.get(pack.getName());
		if (packNode == null) {
			throw new IllegalArgumentException("Package " + pack.getName() + " is not in this tree!");
		}
		UMLCLassTreeNode classNode = new UMLCLassTreeNode(this, clazz);
		packNode.add(classNode);
		reloadFromRoot();
	}
}
