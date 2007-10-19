/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.cagrid.portal2.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLClass;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class UMLClassBean {
	
	private UMLClass umlClass;
	private List<UMLAttribute> attributes = new ArrayList<UMLAttribute>();

	/**
	 * 
	 */
	public UMLClassBean() {

	}
	
	public UMLClassBean(UMLClass umlClass){
		this.umlClass = umlClass;
		UMLClass superClass = umlClass;
		while(superClass != null){
			for(UMLAttribute att : superClass.getUmlAttributeCollection()){
				getAttributes().add(att);
			}
			superClass = superClass.getSuperClass();
		}
	}

	public UMLClass getUmlClass() {
		return umlClass;
	}

	public void setUmlClass(UMLClass umlClass) {
		this.umlClass = umlClass;
	}

	public List<UMLAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<UMLAttribute> attributes) {
		this.attributes = attributes;
	}

}
