/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.domain.Role;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class RoleDao extends AbstractDao<Role> {

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return Role.class;
	}

}
