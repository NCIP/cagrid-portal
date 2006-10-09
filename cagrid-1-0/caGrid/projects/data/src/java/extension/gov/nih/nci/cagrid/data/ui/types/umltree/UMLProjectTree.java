package gov.nih.nci.cagrid.data.ui.types.umltree;

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
	
	
	public UMLPackageTreeNode addUmlPackage(String packageName) {
		UMLPackageTreeNode packNode = new UMLPackageTreeNode(this, packageName);
		packageNodes.put(packageName, packNode);
		getRootNode().add(packNode);
		reloadFromRoot();
		return packNode;
	}
	
	
	public UMLPackageTreeNode getUmlPackageNode(String packageName) {
		Enumeration packEnum = getRootNode().children();
		while (packEnum.hasMoreElements()) {
			UMLPackageTreeNode node = (UMLPackageTreeNode) packEnum.nextElement();
			if (node.getPackageName().equals(packageName)) {
				return node;
			}
		}
		return null;
	}
	
	
	public void removeUmlPackage(String packageName) {
		UMLPackageTreeNode packNode = (UMLPackageTreeNode) packageNodes.remove(packageName);
		if (packNode == null) {
			throw new IllegalArgumentException("Package " + packageName + " is not in this tree!");
		}
		getRootNode().remove(packNode);
		reloadFromRoot();
	}
	
	
	public UMLClassTreeNode addUmlClass(String packageName, String className) {
		// find the packag node
		UMLPackageTreeNode packNode = (UMLPackageTreeNode) packageNodes.get(packageName);
		if (packNode == null) {
			throw new IllegalArgumentException("Package " + packageName + " is not in this tree!");
		}
		UMLClassTreeNode classNode = new UMLClassTreeNode(this, className);
		packNode.add(classNode);
		reloadFromRoot();
		return classNode;
	}
	
	
	public UMLClassTreeNode getUmlClassNode(String packageName, String className) {
		UMLPackageTreeNode packNode = getUmlPackageNode(packageName);
		if (packNode != null) {
			Enumeration classNodes = packNode.children();
			while (classNodes.hasMoreElements()) {
				UMLClassTreeNode classNode = (UMLClassTreeNode) classNodes.nextElement();
				if (classNode.getClassName().equals(className)) {
					return classNode;
				}
			}
		}
		return null;
	}
	
	
	public void removeUmlClass(String packageName, String className) {
		// find the package node
		UMLPackageTreeNode packNode = (UMLPackageTreeNode) packageNodes.get(packageName);
		if (packNode == null) {
			throw new IllegalArgumentException("Package " + packageName + " is not in this tree!");
		}
		// find the class node
		UMLClassTreeNode classNode = null;
		Enumeration classNodeEnum = packNode.children();
		while (classNodeEnum.hasMoreElements()) {
			UMLClassTreeNode node = (UMLClassTreeNode) classNodeEnum.nextElement();
			if (node.getClassName().equals(className)) {
				classNode = node;
				break;
			}
		}
		if (classNode == null) {
			throw new IllegalArgumentException("Class " + className + " is not in this tree!");
		}
		packNode.remove(classNode);
		reloadFromRoot();
	}
}
