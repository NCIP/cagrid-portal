package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.manager.IndexServiceManager;
import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * This class is Runnable thread that will retreive the
 * list of services from a given index service.
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 4:52:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexAggregator extends AbstractAggregator {

    private DiscoveryClient discClient;
    private boolean metadataCompliance;
    private IndexService indexService;
    private IndexServiceManager idxMgr;

    /**
     * Initialize the runnable class with the
     * index service to aggregate from
     */
    public IndexAggregator(IndexService indexService, IndexServiceManager idxMgr, boolean metadataCompliance) {
        this.indexService = indexService;
        // create discovery client for the index
        this.discClient = new DiscoveryClient(indexService.getHandle());

        // Inherit the manager
        this.idxMgr = idxMgr;
        this.metadataCompliance = metadataCompliance;
    }

    public void run() {
        try {
            EndpointReferenceType[] services = discClient.getAllServices(metadataCompliance);

            for (int i = 0; i < services.length; i++) {
                mLogger.debug("Adding " + services[i] + " to index.");
                indexService.addRegisteredService(new RegisteredService(services[i]));
                mLogger.debug("Saving index with " + indexService.getRegisteredServicesCollection().size() + " Registered Services.");
                idxMgr.save(indexService);
            }

        } catch (Exception e) {
            mLogger.error(e);
        }
    }
}
