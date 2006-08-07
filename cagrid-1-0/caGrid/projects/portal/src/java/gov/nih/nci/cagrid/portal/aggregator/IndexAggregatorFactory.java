package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;

import java.util.List;
import java.util.ListIterator;
import java.util.Timer;


/**
 * Aggregates information from a Index.
 * <p/>
 * Can be message (event) driven
 *
 * @version 1.0
 * @created 22-Jun-2006 6:56:32 PM
 */
public class IndexAggregatorFactory extends AbstractAggregator {
    private GridServiceManager idxManager;
    private boolean metadataCompliance;

    /**
     * IOC through Constructor injection
     */
    public IndexAggregatorFactory(java.lang.Boolean metadataCompliance,
        GridServiceManager idxManager) {
        this.metadataCompliance = metadataCompliance.booleanValue();
        this.idxManager = idxManager;
    }

    /**
     * Will start a aggregator thread for each index in the DB
     */
    public void run() {
        _logger.debug("Index Aggregator Factory starting");

        // Retreive all indexe services from DB
        List indexes = idxManager.loadAll(IndexService.class);

        for (ListIterator iter = indexes.listIterator(); iter.hasNext();) {
            IndexService idx = (IndexService) iter.next();
            IndexAggregator idxAggrTask = new IndexAggregator(idx, idxManager,
                    this.metadataCompliance);

            //Aggregators don't have the spring context. So this needs to be set
            //so that they can publish events
            idxAggrTask.setApplicationContext(ctx);

            Timer timer = new Timer(true);
            timer.schedule(idxAggrTask, 1);
        }

        _logger.debug("Index Aggregator Factory exiting");
    }
}
