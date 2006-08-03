package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;

import org.apache.axis.message.addressing.EndpointReferenceType;

import java.util.List;
import java.util.ListIterator;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 31, 2006
 * Time: 10:30:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class IndexServiceManagerLocalTestCase
    extends BaseSpringDataAccessAbstractTestCase {
    IndexServiceManager indexManager;

    /**
     * Mock insert services into index
     */
    public void testIndexServiceDAO() {
        List indexes = indexManager.loadAll(IndexService.class);

        for (ListIterator iter = indexes.listIterator(); iter.hasNext();) {
            IndexService idx = (IndexService) iter.next();

            try {
                DiscoveryClient disc = new DiscoveryClient(idx.getEpr());
                EndpointReferenceType[] services = disc.getAllServices(true);

                logger.debug("Index service has " +
                    idx.getRegisteredServicesCollection().size() +
                    " registered services");

                for (int i = 0; i < services.length; i++) {
                    RegisteredService rService = new RegisteredService(services[i]);
                    indexManager.addRegisteredService(idx, rService);
                }

                logger.debug("New Index Service has " +
                    idx.getRegisteredServicesCollection().size() +
                    "regisgetered Services");
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }
    }

    public void setIndexManager(IndexServiceManager indexManager) {
        this.indexManager = indexManager;
    }
}
