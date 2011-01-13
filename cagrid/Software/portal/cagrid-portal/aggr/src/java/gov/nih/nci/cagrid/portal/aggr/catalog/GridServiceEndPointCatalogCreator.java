package gov.nih.nci.cagrid.portal.aggr.catalog;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.util.filter.ServiceFilter;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Transactional
public class GridServiceEndPointCatalogCreator extends AbstractCatalogCreator {

    private GridServiceDao gridServiceDao;
    private GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao;
    private ServiceMetadataCatalogEntryBuilder serviceMetadataCatalogEntryBuilder;
    private ServiceFilter baseServiceFilter;

    /**
     * Will make sure catalog items exist for all services Run on container
     * startup
     *
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {

        for (GridService g : gridServiceDao.getAll()) {
            if (gridServiceEndPointCatalogEntryDao.isAbout(g) == null) {
                if (!baseServiceFilter.willBeFiltered(g)) {

                    logger
                            .debug("GridService Endpoint catalog not found. Will create for id "
                                    + g.getId());
                    serviceMetadataCatalogEntryBuilder.build(g);
                }
            }

        }
    }

    // spring getters and setters

    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    public GridServiceEndPointCatalogEntryDao getGridServiceEndPointCatalogEntryDao() {
        return gridServiceEndPointCatalogEntryDao;
    }

    public void setGridServiceEndPointCatalogEntryDao(GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao) {
        this.gridServiceEndPointCatalogEntryDao = gridServiceEndPointCatalogEntryDao;
    }

    public ServiceMetadataCatalogEntryBuilder getServiceMetadataCatalogEntryBuilder() {
        return serviceMetadataCatalogEntryBuilder;
    }

    public void setServiceMetadataCatalogEntryBuilder(ServiceMetadataCatalogEntryBuilder serviceMetadataCatalogEntryBuilder) {
        this.serviceMetadataCatalogEntryBuilder = serviceMetadataCatalogEntryBuilder;
    }

    public ServiceFilter getBaseServiceFilter() {
        return baseServiceFilter;
    }

    public void setBaseServiceFilter(ServiceFilter baseServiceFilter) {
        this.baseServiceFilter = baseServiceFilter;
    }
}
