/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.domain.metadata.common.UMLAttribute;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CriterionBean {
	
	private UMLAttribute umlAttribute;
	private String predicate;
	private String value;

	/**
	 * 
	 */
	public CriterionBean() {

	}

	public UMLAttribute getUmlAttribute() {
		return umlAttribute;
	}

	public void setUmlAttribute(UMLAttribute umlAttribute) {
		this.umlAttribute = umlAttribute;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
