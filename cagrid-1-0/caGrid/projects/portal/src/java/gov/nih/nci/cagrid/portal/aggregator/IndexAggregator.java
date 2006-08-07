package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;
import gov.nih.nci.cagrid.portal.utils.GridUtils;

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
    private boolean metadataCompliance = true;
    private IndexService indexService;
    private GridServiceManager idxMgr;

    /**
     * Initialize the runnable class with the
     * index service to aggregate from
     */
    public IndexAggregator(IndexService indexService,
        GridServiceManager idxMgr, boolean metadataCompliance) {
        this.indexService = indexService;

        // Inherit the manager
        this.idxMgr = idxMgr;
        this.metadataCompliance = metadataCompliance;

        // create discovery client for the index
        this.discClient = new DiscoveryClient(indexService.getHandle());
    }

    public void run() {
        _logger.debug("Index Aggregator started for" +
            this.indexService.getEPR());

        EndpointReferenceType[] serviceEPR = new EndpointReferenceType[0];

        try {
            serviceEPR = discClient.getAllServices(metadataCompliance);
        } catch (Exception e) {
            _logger.error(e);
        }

        _logger.debug("Found " + serviceEPR.length + " services in the index");

        for (int i = 0; i < serviceEPR.length; i++) {
            _logger.debug("Adding " + serviceEPR[i] + " to index.");

            RegisteredService rService = null;

            rService = new RegisteredService(serviceEPR[i]);
            rService.setIndex(indexService);

            try {
                rService.setDescription(GridUtils.getServiceDescription(
                        serviceEPR[i]));
                rService.setName(GridUtils.getServiceDescription(serviceEPR[i]));
            } catch (MetadataRetreivalException e) {
                _logger.info(e);
            } finally {
                idxMgr.save(rService);
            }

            try {
                _logger.debug("Found Service. Publishing Event");
                ctx.publishEvent(new RegisteredServiceFoundEvent(this, rService));
            } catch (NullPointerException e) {
                _logger.error(
                    "IndexAggregator does not have a proper context to publish events");
            }

            _logger.debug("Event Published");
        }

        _logger.debug("Index Aggregator for " + this.indexService.getName() +
            " exiting");
    }

    public void setIndexService(IndexService indexService) {
        this.indexService = indexService;
    }

    public void setIdxMgr(GridServiceManager idxMgr) {
        this.idxMgr = idxMgr;
    }
}
