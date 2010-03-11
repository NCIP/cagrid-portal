/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class AssociationBean {
	
	private String roleName;
	private CriteriaBean criteriaBean;

	/**
	 * 
	 */
	public AssociationBean() {

	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public CriteriaBean getCriteriaBean() {
		return criteriaBean;
	}

	public void setCriteriaBean(CriteriaBean criteria) {
		this.criteriaBean = criteria;
	}

}
