package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.domain.PointOfContact;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import org.apache.axis.types.URI;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 16, 2006
 * Time: 10:43:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class DomainModelPersistenceLocalTestCase extends BaseSpringDataAccessAbstractTestCase {

    GridServiceManager gridServiceManager;


    public void testRegisteredServicePeristence(){
        try {
            RegisteredService service = new RegisteredService("http://cagrid04.bmi.ohio-state.edu:7080/wsrf/services/cagrid/CaDSRService");
            service.setIndex(new IndexService("test-index"));
            //Create rc
            ResearchCenter rc = new ResearchCenter();
            rc.setDisplayName("Test Center");

            //create POC
             PointOfContact poc = new PointOfContact();
             poc.setFirstName("Test User");

            //add POC to RC
            rc.getPocCollection().add(poc);
            //add RC to Service
            service.setResearchCenter(rc);

            //save object graph
            gridServiceManager.save(service);

            assertTrue(gridServiceManager.loadAll(RegisteredService.class).size() > 0);
            
        } catch (URI.MalformedURIException e) {
            fail(e.getMessage());
        }
    }

    /**
     * setter for spring
     * @param gridServiceManager
     */
    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }
}

