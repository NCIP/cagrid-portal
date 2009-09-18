/**
 *
 */
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.domain.catalog.InformationModelCatalogEntry;

import javax.persistence.NonUniqueResultException;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class InformationModelCatalogEntryDao extends AbstractCatalogEntryDao<InformationModelCatalogEntry> {


    @Override
    public Class domainClass() {
        return InformationModelCatalogEntry.class;
    }

    public InformationModelCatalogEntry getDynamicModelByProjectLongName(String projectLongName) {
        InformationModelCatalogEntry infoCe = null;
        List l = getHibernateTemplate().find(
                "from InformationModelCatalogEntry where author is null and projectLongName = ?", projectLongName);
        if (l.size() > 1) {
            throw new NonUniqueResultException(
                    "More than one InformationModel found for projectLongName = "
                            + projectLongName);
        }
        if (l.size() == 1) {
            infoCe = (InformationModelCatalogEntry) l.iterator().next();
        }
        return infoCe;
    }

}
