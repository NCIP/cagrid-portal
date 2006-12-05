/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.beans;

import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;

import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class UMLClassBean {
	
	private UMLClass umlSuperClass;
	private UMLClass umlClass;
	private List associationBeans;
	
	public List getAssociationBeans() {
		return associationBeans;
	}
	public void setAssociationBeans(List associationBeans) {
		this.associationBeans = associationBeans;
	}
	public UMLClass getUmlClass() {
		return umlClass;
	}
	public void setUmlClass(UMLClass umlClass) {
		this.umlClass = umlClass;
	}
	public UMLClass getUmlSuperClasses() {
		return umlSuperClass;
	}
	public void setUmlSuperClass(UMLClass umlSuperClass) {
		this.umlSuperClass = umlSuperClass;
	}

}
