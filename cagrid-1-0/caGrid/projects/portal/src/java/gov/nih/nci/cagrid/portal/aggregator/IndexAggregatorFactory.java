package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.manager.IndexServiceManager;

import java.util.List;
import java.util.ListIterator;

/**
 * Aggregates information from a Index.
 * <p/>
 * Can be message (event) driven
 *
 * @version 1.0
 * @created 22-Jun-2006 6:56:32 PM
 */
public class IndexAggregatorFactory extends AbstractAggregator {

    private IndexServiceManager idxManager;
    private boolean metadataCompliance;

    /**
     * IOC through Constructor injection
     */
    public IndexAggregatorFactory(java.lang.Boolean metadataCompliance, IndexServiceManager idxManager) {
        this.metadataCompliance = metadataCompliance.booleanValue();
        this.idxManager = idxManager;
    }

    /**
     * Will start a aggregator thread for each index in the DB
     */
    public void run() {
        // Retreive all indexe services from DB
        List indexes = idxManager.loadAll(IndexService.class);

        for (ListIterator iter = indexes.listIterator(); iter.hasNext();) {
            IndexService idx = (IndexService) iter.next();
            IndexAggregator idxAggr = new IndexAggregator(idx, idxManager, this.metadataCompliance);
            _logger.debug("Index Aggregator started for index " + idx.getHandle().toString());
            idxAggr.run();
        }


    }


}