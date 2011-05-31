/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao;

import java.util.List;

import javax.persistence.NonUniqueResultException;

import gov.nih.nci.cagrid.portal.domain.IdentityProvider;
import gov.nih.nci.cagrid.portal.domain.PortalUser;



/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class IdentityProviderDao extends AbstractDao<IdentityProvider> {

	@Override
	public Class domainClass() {
		return IdentityProvider.class;
	}

	public IdentityProvider getByUrl(String idpUrl) {
		IdentityProvider idp = null;
		List l = getHibernateTemplate().find(
				"from IdentityProvider where url = ?", idpUrl);
		if (l.size() > 1) {
			throw new NonUniqueResultException(
					"More than one IdentityProvider found for url = " + idpUrl);
		}
		if (l.size() == 1) {
			idp = (IdentityProvider) l.iterator().next();
		}
		return idp;
	}

}
