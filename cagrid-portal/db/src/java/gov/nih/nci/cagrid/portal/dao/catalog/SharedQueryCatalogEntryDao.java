/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.domain.catalog.SharedQueryCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.dataservice.Query;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SharedQueryCatalogEntryDao extends AboutCatalogEntryDao<SharedQueryCatalogEntry, Query> {

	@Override
	public SharedQueryCatalogEntry createCatalogAbout(Query domainObject) {
		SharedQueryCatalogEntry entry = isAbout(domainObject);
		if(entry == null){
			entry = new SharedQueryCatalogEntry();
			entry.setAbout(domainObject);
		}
		getHibernateTemplate().save(entry);
		return entry;
	}

	@Override
	public Class domainClass() {
		return SharedQueryCatalogEntry.class;
	}

}
