package gov.nih.nci.cagrid.data.ui.types.umltree;

import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.data.ui.tree.CheckBoxTree;

import java.util.Enumeration;
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
	
	
	public void removeUmlPackage(UMLPackageMetadata pack) {
		UMLPackageTreeNode packNode = (UMLPackageTreeNode) packageNodes.get(pack);
		if (packNode == null) {
			throw new IllegalArgumentException("Package " + pack.getName() + " is not in this tree!");
		}
		getRootNode().remove(packNode);
		reloadFromRoot();
	}
	
	
	public void addUmlClass(UMLPackageMetadata pack, UMLClassMetadata clazz) {
		// find the packag node
		UMLPackageTreeNode packNode = (UMLPackageTreeNode) packageNodes.get(pack.getName());
		if (packNode == null) {
			throw new IllegalArgumentException("Package " + pack.getName() + " is not in this tree!");
		}
		UMLClassTreeNode classNode = new UMLClassTreeNode(this, clazz);
		packNode.add(classNode);
		reloadFromRoot();
	}
	
	
	public void removeUmlClass(UMLPackageMetadata pack, UMLClassMetadata clazz) {
		// find the package node
		UMLPackageTreeNode packNode = (UMLPackageTreeNode) packageNodes.get(pack);
		if (packNode == null) {
			throw new IllegalArgumentException("Package " + pack.getName() + " is not in this tree!");
		}
		// find the class node
		UMLClassTreeNode classNode = null;
		Enumeration classNodeEnum = packNode.children();
		while (classNodeEnum.hasMoreElements()) {
			UMLClassTreeNode node = (UMLClassTreeNode) classNodeEnum.nextElement();
			if (node.getClassMetadata().equals(clazz)) {
				classNode = node;
				break;
			}
		}
		if (classNode == null) {
			throw new IllegalArgumentException("Class " + clazz.getFullyQualifiedName() + " is not in this tree!");
		}
		packNode.remove(classNode);
		reloadFromRoot();
	}
}
