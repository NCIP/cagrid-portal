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
package gov.nih.nci.cagrid.portal.aggr.catalog.aspects;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal.service.PointOfContactCatalogService;
import gov.nih.nci.cagrid.portal.util.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.util.List;

/**
 * Will create CE's for POC's when services
 * are created at runtime
 * <p/>
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Aspect
public class PointOfContactCatalogAspect {

    private static final Log logger = LogFactory
            .getLog(PointOfContactCatalogAspect.class);
    private PointOfContactCatalogService pointOfContactCatalogService;


    @AfterReturning("execution(* gov.nih.nci.cagrid.portal.dao.GridServiceDao.save*(gov.nih.nci.cagrid.portal.domain.GridService)) && args(service)")
    public void onSave(GridService service) throws Throwable {
        try {
            logger.debug("Grid Service was saved. Will create CE's for POC's");

            List<PointOfContact> pocCollection = BeanUtils.traverse(service,
                    "serviceMetadata.serviceDescription.pointOfContactCollection", List.class);
            createCatalogs(pocCollection);


            pocCollection = BeanUtils.traverse(service,
                    "serviceMetadata.hostingResearchCenter.pointOfContactCollection", List.class);
            createCatalogs(pocCollection);

        } catch (Exception ex) {
            logger
                    .error("Error creating catalog entry: ",ex);
        }
    }

    private void createCatalogs(List<PointOfContact> pocCollection) {
        if (pocCollection != null) {
            for (PointOfContact poc : pocCollection) {
                getPointOfContactCatalogService().create(poc);
            }
        }
    }

    public PointOfContactCatalogService getPointOfContactCatalogService() {
        return pointOfContactCatalogService;
    }

    public void setPointOfContactCatalogService(PointOfContactCatalogService pointOfContactCatalogService) {
        this.pointOfContactCatalogService = pointOfContactCatalogService;
    }
}
