/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CQLQueryBean extends CriteriaBean {
	
	private String modifierType;
	private List<String> selectedAttributes = new ArrayList<String>();

	/**
	 * 
	 */
	public CQLQueryBean() {
		
	}
	
	public CQLQueryBean(UMLClass umlClass){
		setUmlClass(umlClass);
	}

	public String getModifierType() {
		return modifierType;
	}

	public void setModifierType(String modifierType) {
		this.modifierType = modifierType;
	}

	public List<String> getSelectedAttributes() {
		return selectedAttributes;
	}

	public void setSelectedAttributes(List<String> selectedAttributes) {
		this.selectedAttributes = selectedAttributes;
	}

}
