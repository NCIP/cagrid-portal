package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.manager.IndexServiceManager;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;

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
    public IndexAggregator(IndexService indexService,
        IndexServiceManager idxMgr, boolean metadataCompliance) {
        this.indexService = indexService;
        // Inherit the manager
        this.idxMgr = idxMgr;
        this.metadataCompliance = metadataCompliance;

        // create discovery client for the index
        this.discClient = new DiscoveryClient(indexService.getHandle());
    }

    public void run() {
        try {
            _logger.debug("Index Aggregator started for" + this.indexService.getEpr());
            EndpointReferenceType[] serviceEPR = discClient.getAllServices(metadataCompliance);
           _logger.debug("Found " + serviceEPR.length + " services in the index");

            for (int i = 0; i < serviceEPR.length; i++) {
                _logger.debug("Adding " + serviceEPR[i] + " to index.");

                RegisteredService rService = new RegisteredService(serviceEPR[i],true);
                idxMgr.addRegisteredService(indexService, rService);
            }
        } catch (Exception e) {
            _logger.error(e);
            _logger.debug("ERROR creating REGISTERED SERVICE");
        }
        _logger.debug("Index Aggregator for " + this.indexService.getName() + " exiting");
        ctx.publishEvent(new AggregatorFinishedEvent(this,indexService));
    }
}
