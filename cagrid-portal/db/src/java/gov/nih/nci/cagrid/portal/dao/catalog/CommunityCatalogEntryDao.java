package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CommunityCatalogEntry;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CommunityCatalogEntryDao extends AbstractDao<CommunityCatalogEntry> {

    @Override
    public Class domainClass() {
        return CommunityCatalogEntry.class;
    }

    public boolean isUnique(String name) {
        CommunityCatalogEntry entry = new CommunityCatalogEntry();
        entry.setName(name);

        return getByExample(entry) == null;

    }
}
