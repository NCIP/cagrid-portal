/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.domain.GridPortalUser;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class GridPortalUserDao extends AbstractDao<GridPortalUser> {

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return GridPortalUser.class;
	}

}
