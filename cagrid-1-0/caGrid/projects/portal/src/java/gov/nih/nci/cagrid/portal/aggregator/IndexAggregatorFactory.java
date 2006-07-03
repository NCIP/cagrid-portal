package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.manager.IndexServiceManager;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

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
public class IndexAggregatorFactory implements ApplicationListener, Runnable {

    private IndexServiceManager idxManager;
    private boolean metadataCompliance;

    /**
     * IOC through Constructor injection
     */
    public IndexAggregatorFactory(boolean metadataCompliance, IndexServiceManager idxManager) {
        this.metadataCompliance = metadataCompliance;
        this.idxManager = idxManager;
    }

    /**
     * @param o0
     */
    public void onApplicationEvent(ApplicationEvent o0) {

    }


    /**
     * Will start a aggregator thread for each index in the DB
     */
    public void run() {
        // Retreive all indexe services from DB
        List indexes = idxManager.loadAll(IndexService.class);

        for (ListIterator iter = indexes.listIterator(); iter.hasNext();) {
            IndexService idx = (IndexService) iter.next();
            IndexAggregator idxAggr = new IndexAggregator(idx.getHandle(), this.metadataCompliance);
            idxAggr.run();
        }


    }


}