/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleType;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CatalogEntryRoleTypeDao extends AbstractDao<CatalogEntryRoleType> {

	@Override
	public Class domainClass() {
		return CatalogEntryRoleType.class;
	}

}
