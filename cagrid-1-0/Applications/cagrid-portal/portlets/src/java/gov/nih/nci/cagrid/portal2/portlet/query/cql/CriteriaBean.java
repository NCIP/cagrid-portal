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
public class CriteriaBean {
	
	private UMLClass umlClass;
	private List<CriterionBean> criteria = new ArrayList<CriterionBean>();
	private List<AssociationBean> associations = new ArrayList<AssociationBean>();

	/**
	 * 
	 */
	public CriteriaBean() {

	}

	public UMLClass getUmlClass() {
		return umlClass;
	}

	public void setUmlClass(UMLClass umlClass) {
		this.umlClass = umlClass;
	}

	public List<CriterionBean> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<CriterionBean> criteria) {
		this.criteria = criteria;
	}

	public List<AssociationBean> getAssociations() {
		return associations;
	}

	public void setAssociations(List<AssociationBean> associations) {
		this.associations = associations;
	}

}
