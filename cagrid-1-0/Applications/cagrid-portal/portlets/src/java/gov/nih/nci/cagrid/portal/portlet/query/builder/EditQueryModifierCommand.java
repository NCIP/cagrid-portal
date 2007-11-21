/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.portlet.query.cql.QueryModifierType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class EditQueryModifierCommand {

	private QueryModifierType modifierType;
	private Set<String> selectedAttributes = new HashSet<String>();
	private String editOperation;
	
	/**
	 * 
	 */
	public EditQueryModifierCommand() {

	}

	public QueryModifierType getModifierType() {
		return modifierType;
	}

	public void setModifierType(QueryModifierType modifierType) {
		this.modifierType = modifierType;
	}

	public Set<String> getSelectedAttributes() {
		return selectedAttributes;
	}

	public void setSelectedAttributes(Set<String> selectedAttributes) {
		this.selectedAttributes = selectedAttributes;
	}

	public String getEditOperation() {
		return editOperation;
	}

	public void setEditOperation(String editOperation) {
		this.editOperation = editOperation;
	}

}
