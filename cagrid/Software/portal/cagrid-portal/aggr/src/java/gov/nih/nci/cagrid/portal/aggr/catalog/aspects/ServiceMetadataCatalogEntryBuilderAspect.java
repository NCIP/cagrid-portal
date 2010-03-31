/**
 *
 */
package gov.nih.nci.cagrid.portal.aggr.catalog.aspects;

import gov.nih.nci.cagrid.portal.aggr.catalog.ServiceMetadataCatalogEntryBuilder;
import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.util.filter.ServiceFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
@Aspect
public class ServiceMetadataCatalogEntryBuilderAspect {

    private static final Log logger = LogFactory
            .getLog(ServiceMetadataCatalogEntryBuilderAspect.class);
    private ServiceFilter baseServiceFilter;
    private ServiceMetadataCatalogEntryBuilder serviceMetadataCatalogEntryBuilder;
    private GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao;


    @AfterReturning("execution(* gov.nih.nci.cagrid.portal.dao.GridServiceDao.*(gov.nih.nci.cagrid.portal.domain.GridService)) && !within(gov.nih.nci.cagrid.portal.aggr.catalog.ServiceMetadataCatalogEntryBuilder)  && args(service)")
    public void onSave(GridService service) throws Throwable {
        try {

            if (baseServiceFilter.willBeFiltered(service)) {
                logger.info("Service should be filtered. Will hide the associate CE");
                GridServiceEndPointCatalogEntry entry = service.getCatalog();
                if (entry != null) {
                    gridServiceEndPointCatalogEntryDao.hide(entry);
                }
            } else {
                logger.debug("Grid Service being saved. Will try and create Grid Service CE");
                serviceMetadataCatalogEntryBuilder.build(service);
            }
        } catch (Exception ex) {
            logger
                    .error("Error creating catalog entry: " + ex.getMessage(),
                            ex);
        }
    }

    public ServiceFilter getBaseServiceFilter() {
        return baseServiceFilter;
    }

    public void setBaseServiceFilter(ServiceFilter baseServiceFilter) {
        this.baseServiceFilter = baseServiceFilter;
    }

    public ServiceMetadataCatalogEntryBuilder getServiceMetadataCatalogEntryBuilder() {
        return serviceMetadataCatalogEntryBuilder;
    }

    public void setServiceMetadataCatalogEntryBuilder(ServiceMetadataCatalogEntryBuilder serviceMetadataCatalogEntryBuilder) {
        this.serviceMetadataCatalogEntryBuilder = serviceMetadataCatalogEntryBuilder;
    }

    public GridServiceEndPointCatalogEntryDao getGridServiceEndPointCatalogEntryDao() {
        return gridServiceEndPointCatalogEntryDao;
    }

    public void setGridServiceEndPointCatalogEntryDao(GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao) {
        this.gridServiceEndPointCatalogEntryDao = gridServiceEndPointCatalogEntryDao;
    }
}
