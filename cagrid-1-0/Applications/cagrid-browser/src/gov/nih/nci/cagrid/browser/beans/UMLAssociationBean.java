/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.beans;

import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class UMLAssociationBean {
	
	private UMLAssociation umlAssociation;
	private UMLClass umlClass;
	public UMLAssociation getUmlAssociation() {
		return umlAssociation;
	}
	public void setUmlAssociation(UMLAssociation umlAssociation) {
		this.umlAssociation = umlAssociation;
	}
	public UMLClass getUmlClass() {
		return umlClass;
	}
	public void setUmlClass(UMLClass umlClass) {
		this.umlClass = umlClass;
	}

}
