/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.domain.PortalUser;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class PortalUserDao extends AbstractDao<PortalUser> {

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return PortalUser.class;
	}

}
