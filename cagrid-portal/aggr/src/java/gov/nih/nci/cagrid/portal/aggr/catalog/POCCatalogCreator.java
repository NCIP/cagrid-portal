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
            try {
                pointOfContactCatalogService.create(poc);
            } catch (Exception e) {
                logger.warn("Error cresting POC catalog for POC ID " + poc.getId() + ". Will skip");
            }
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