package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.util.TimestampProvider;
import gov.nih.nci.cagrid.portal.annotation.UpdatesCatalogs;
import org.springframework.beans.factory.annotation.Required;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class AbstractCatalogEntryDao<T extends AbstractDomainObject> extends AbstractDao<T> {

    TimestampProvider timestampProvider;


    @Override
    /**
     * Saves the catalog entry with the current timestamp
     */
    @UpdatesCatalogs
    public void save(T entry) {
        if (entry instanceof CatalogEntry)
            ((CatalogEntry) entry).setUpdatedAt(timestampProvider.getTimestamp());
        super.save(entry);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    @UpdatesCatalogs
    public void delete(T domainObject) {
        super.delete(domainObject);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public TimestampProvider getTimestampProvider() {
        return timestampProvider;
    }

    @Required
    public void setTimestampProvider(TimestampProvider timestampProvider) {
        this.timestampProvider = timestampProvider;
    }
}
