package gov.nih.nci.cagrid.portal.aggr.catalog;

import gov.nih.nci.cagrid.portal.dao.PointOfContactDao;
import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal.service.PointOfContactCatalogService;

/**
 * Will create CE's for existing POC's
 * Runs on container startup.
 * <p/>
 * Aspect will create CE's at runtime
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class POCCatalogCreator extends AbstractCatalogCreator {
    PointOfContactCatalogService pointOfContactCatalogService;

    PointOfContactDao pointOfContactDao;


    public void afterPropertiesSet() throws Exception {
        for (PointOfContact poc : pointOfContactDao.getAll()) {
            pointOfContactCatalogService.create(poc);
        }
    }

    public PointOfContactCatalogService getPointOfContactCatalogService() {
        return pointOfContactCatalogService;
    }

    public void setPointOfContactCatalogService(PointOfContactCatalogService pointOfContactCatalogService) {
        this.pointOfContactCatalogService = pointOfContactCatalogService;
    }

    public PointOfContactDao getPointOfContactDao() {
        return pointOfContactDao;
    }

    public void setPointOfContactDao(PointOfContactDao pointOfContactDao) {
        this.pointOfContactDao = pointOfContactDao;
    }
}