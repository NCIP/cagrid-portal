package gov.nih.nci.cagrid.portal.aggr.catalog;

import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.util.filter.ServiceFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author kherm manav.kher@semanticbits.com
 */
public class CatalogServiceStatusSync {

    protected final Log logger = LogFactory
                 .getLog(this.getClass());


    private ServiceFilter baseServiceFilter;
    GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao;

    public void sync() {
        for (GridServiceEndPointCatalogEntry entry : gridServiceEndPointCatalogEntryDao.getAll()) {
            if (entry.isHidden()) {
                if (!baseServiceFilter.willBeFiltered(entry.getAbout())) {
                    logger.info("Catalog with ID " + entry.getId()
                            + " is hidden for a service that is in good standing. Will unhide");
                    entry.setHidden(false);
                    gridServiceEndPointCatalogEntryDao.save(entry);
                }
            } else {
                if (baseServiceFilter.willBeFiltered(entry.getAbout())) {
                    logger.info("Catalog with ID " + entry.getId()
                            + " is not hidden for a service that is in not in good standing. Will hide");
                    entry.setHidden(true);
                    gridServiceEndPointCatalogEntryDao.save(entry);
                }
            }
        }
    }

    public ServiceFilter getBaseServiceFilter() {
        return baseServiceFilter;
    }

    public void setBaseServiceFilter(ServiceFilter baseServiceFilter) {
        this.baseServiceFilter = baseServiceFilter;
    }

    public GridServiceEndPointCatalogEntryDao getGridServiceEndPointCatalogEntryDao() {
        return gridServiceEndPointCatalogEntryDao;
    }

    public void setGridServiceEndPointCatalogEntryDao(GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao) {
        this.gridServiceEndPointCatalogEntryDao = gridServiceEndPointCatalogEntryDao;
    }
}
