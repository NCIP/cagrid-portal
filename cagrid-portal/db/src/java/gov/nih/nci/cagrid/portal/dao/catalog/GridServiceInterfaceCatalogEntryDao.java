/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 *
 */
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceInterfaceCatalogEntry;

import javax.persistence.NonUniqueResultException;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class GridServiceInterfaceCatalogEntryDao extends
        AbstractCatalogEntryDao<GridServiceInterfaceCatalogEntry> {

    @Override
    public Class domainClass() {
        return GridServiceInterfaceCatalogEntry.class;
    }

    public GridServiceInterfaceCatalogEntry getDynamicInterfaceForNameAndVersion(
            String name, String version) {

        GridServiceInterfaceCatalogEntry gsiCe = null;

        List l = getHibernateTemplate()
                .find(
                        "from GridServiceInterfaceCatalogEntry g where g.author is null and g.name = ? and g.version = ?",
                        new Object[]{name, version});
        if (l.size() > 1) {
            throw new NonUniqueResultException(
                    "More than one dynamic GridServiceInterfaceCatalogEntry found for name:version = "
                            + name + ":" + version);
        }
        if (l.size() == 1) {
            gsiCe = (GridServiceInterfaceCatalogEntry) l.iterator().next();
        }

        return gsiCe;
    }

    public GridServiceInterfaceCatalogEntry getInterfaceForNameAndVersion(
            String name, String version) {

        GridServiceInterfaceCatalogEntry gsiCe = null;

        List l = getHibernateTemplate()
                .find(
                        "from GridServiceInterfaceCatalogEntry g where g.name = ? and g.version = ?",
                        new Object[]{name, version});
        if (l.size() > 1) {
            throw new NonUniqueResultException(
                    "More than one GridServiceInterfaceCatalogEntry found for name:version = "
                            + name + ":" + version);
        }
        if (l.size() == 1) {
            gsiCe = (GridServiceInterfaceCatalogEntry) l.iterator().next();
        }

        return gsiCe;
    }

}
