/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.PortalUser;

import java.util.List;

import javax.persistence.NonUniqueResultException;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class PortalUserDao extends AbstractDao<PortalUser> {

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return PortalUser.class;
	}

	public PortalUser getByPortalId(Long userId) {
		PortalUser user = null;
		List l = getHibernateTemplate().find("from PortalUser where portalId = '" + userId + "'");
		if(l.size() > 1){
			throw new NonUniqueResultException("More than one PortalUser found for portalId = " + userId);
		}
		if(l.size() == 1){
			user = (PortalUser)l.iterator().next();
		}
		return user;
	}

}
