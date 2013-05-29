/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
