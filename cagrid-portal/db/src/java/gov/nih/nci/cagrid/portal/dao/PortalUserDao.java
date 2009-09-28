/**
 *
 */
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.PortalUser;

import javax.persistence.NonUniqueResultException;
import java.util.List;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class PortalUserDao extends AbstractDao<PortalUser> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return PortalUser.class;
	}

	@Override
	public void save(PortalUser domainObject) {
		super.save(domainObject); // To change body of overridden methods use
									// File | Settings | File Templates.
	}

	public PortalUser getByPortalId(String userId) {
		try {
			String[] parts = userId.split(":");
			Long.valueOf(parts[0]);
			Long.valueOf(parts[1]);
		} catch (Exception ex) {
			throw new RuntimeException("Invalid portal ID: " + userId);
		}
		
//		String portalUserId = userId.replace(":", "%");
		System.out.println("#####################################");
		System.out.println("userId: '" + userId + "'");
		System.out.println("#####################################");
		
		PortalUser user = null;
		
		List l = getHibernateTemplate().find(
				"from PortalUser where portalId = ?", userId);
		
		if (l.size() > 1) {
			throw new NonUniqueResultException(
					"More than one PortalUser found for portalId = " + userId);
		}
		if (l.size() == 1) {
			user = (PortalUser) l.iterator().next();
		}
		return user;
	}

	public List<PortalUser> searchByNameAndEmail(String firstName,
			String lastName, String emailAddress) {
		List<PortalUser> l = getHibernateTemplate()
				.find(
						"from PortalUser pu where pu.person.firstName = ? and pu.person.lastName = ? and pu.person.emailAddress = ?",
						new Object[] { firstName, lastName, emailAddress });
		return l;
	}

	public PortalUser getByPersonId(Integer personId) {
		PortalUser user = null;
		List l = getHibernateTemplate().find(
				"from PortalUser pu where pu.person.id = ?", personId);
		if (l.size() > 1) {
			throw new NonUniqueResultException(
					"More than one PortalUser found for personId = " + personId);
		}
		if (l.size() == 1) {
			user = (PortalUser) l.iterator().next();
		}
		return user;		
	}

}
