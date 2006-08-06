package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.manager.IndexServiceManager;

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.TimerTask;

import org.springframework.scheduling.timer.ScheduledTimerTask;
import org.springframework.scheduling.timer.TimerFactoryBean;


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
    public IndexAggregatorFactory(java.lang.Boolean metadataCompliance,
                                  IndexServiceManager idxManager) {
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
        ScheduledTimerTask[] schedule = new ScheduledTimerTask[indexes.size()];
        int counter =0;

        for (ListIterator iter = indexes.listIterator(); iter.hasNext();) {
            IndexService idx = (IndexService) iter.next();
            TimerTask idxAggrTask = new IndexAggregator(idx, idxManager,
                    this.metadataCompliance);

            //Start Aggregtor for individual index
            // Without much delay and will run only once
            schedule[counter++] = new ScheduledTimerTask(idxAggrTask,100);
        }

        //Start the index aggregator tasks
        TimerFactoryBean factory = new TimerFactoryBean();
        factory.setScheduledTimerTasks(schedule);
        factory.afterPropertiesSet();

        _logger.debug("Index Aggregator Factory exiting");
    }
}
