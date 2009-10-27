package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.domain.catalog.PointOfContactCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;

import javax.persistence.NonUniqueResultException;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PointOfContactCatalogEntryDao extends
        AboutCatalogEntryDao<PointOfContactCatalogEntry, PointOfContact> {

    public PointOfContactCatalogEntryDao() {
    }

    /*
      * (non-Javadoc)
      *
      * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
      */
    @Override
    public Class domainClass() {
        return PointOfContactCatalogEntry.class;
    }

    /**
     * Overriden. This does a search on email address. SO two CE's cannot
     * belong to POC's with the same email
     *
     * @param d
     * @return CatalogEntry from the domain object that the catalog is about
     */
    public PointOfContactCatalogEntry isAbout(PointOfContact d) {

        PointOfContactCatalogEntry catalog = null;
        List l = getHibernateTemplate().find("from " + domainClass().getSimpleName() + " where about.person.emailAddress = ?",
                new Object[]{d.getPerson().getEmailAddress()});
        if (l.size() > 1) {
            throw new NonUniqueResultException("More than one CatalogEntry found for Domain Object with ID = " + d.getId());
        }
        if (l.size() == 1) {
            catalog = (PointOfContactCatalogEntry) l.iterator().next();
        }
        return catalog;
    }


    public PointOfContactCatalogEntry createCatalogAbout(PointOfContact poc) {
        PointOfContactCatalogEntry catalog = isAbout(poc);
        if (catalog == null) {
            logger.debug("Will create a new Catalog for POC");
            catalog = new PointOfContactCatalogEntry();
            catalog.setAbout(poc);
            poc.setCatalog(catalog);
        } else
            logger
                    .debug("Catalog entry already exists for POC.");

        super.save(catalog);
        return catalog;
    }
}
