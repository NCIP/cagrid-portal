/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;

import javax.persistence.NonUniqueResultException;
import java.util.List;

/**
 * Dao for Catalog entries that are "about" another Portal domain object
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class AboutCatalogEntryDao<T extends CatalogEntry, D extends DomainObject> extends AbstractCatalogEntryDao<T> {

    public AboutCatalogEntryDao() {
    }


    public abstract Class domainClass();

    /**
     * Subclasses need to provide implementation for creating
     * catalog items from domain objects
     *
     * @param domainObject
     */
    public abstract T createCatalogAbout(D domainObject);


    /**
     * @param d
     * @return CatalogEntry from the domain object that the catalog is about
     */
    public T isAbout(DomainObject d) {

        T catalog = null;
        List l = getHibernateTemplate().find("from " + domainClass().getSimpleName() + " where about.id = ?",
                new Object[]{d.getId()});
        if (l.size() > 1) {
            throw new NonUniqueResultException("More than one CatalogEntry found for " + d.getClass() + "  with ID = " + d.getId());
        }
        if (l.size() == 1) {
            catalog = (T) l.iterator().next();
        }
        return catalog;
    }

}