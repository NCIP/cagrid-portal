/**
 *
 */
package gov.nih.nci.cagrid.portal.aggr.catalog.aspects;

import gov.nih.nci.cagrid.portal.aggr.catalog.ServiceMetadataCatalogEntryBuilder;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.util.filter.ServiceFilter;
import gov.nih.nci.cagrid.portal.service.CatalogEntryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@Aspect
public class ServiceMetadataCatalogEntryBuilderAspect {

    private static final Log logger = LogFactory
            .getLog(ServiceMetadataCatalogEntryBuilderAspect.class);
    private ServiceFilter baseServiceFilter;
    private ServiceMetadataCatalogEntryBuilder serviceMetadataCatalogEntryBuilder;
    private CatalogEntryService catalogEntryService;


    @AfterReturning("execution(* gov.nih.nci.cagrid.portal.dao.GridServiceDao.*(gov.nih.nci.cagrid.portal.domain.GridService)) && !within(gov.nih.nci.cagrid.portal.aggr.catalog.ServiceMetadataCatalogEntryBuilder)  && args(service)")
    public void onSave(GridService service) throws Throwable {
        try {

            if (baseServiceFilter.willBeFiltered(service)) {
                logger.info("Service should be filtered. Will delete the associate CE");
                GridServiceEndPointCatalogEntry entry = service.getCatalog();
                if (entry != null){
                    service.setCatalog(null);
                    catalogEntryService.deleteCatalogEntry(entry);
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

    public CatalogEntryService getCatalogEntryService() {
        return catalogEntryService;
    }

    public void setCatalogEntryService(CatalogEntryService catalogEntryService) {
        this.catalogEntryService = catalogEntryService;
    }
}
