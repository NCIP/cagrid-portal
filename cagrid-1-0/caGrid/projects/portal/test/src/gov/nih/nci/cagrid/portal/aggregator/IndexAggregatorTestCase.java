package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;

import org.springframework.scheduling.timer.ScheduledTimerTask;
import org.springframework.scheduling.timer.TimerFactoryBean;

import java.util.Iterator;
import java.util.TimerTask;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 4, 2006
 * Time: 9:03:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexAggregatorTestCase
    extends BaseSpringDataAccessAbstractTestCase {
    GridServiceManager idxManager;

    /**
     * Will test the index aggregator factory
     */
    public void testAggregator() {
        ScheduledTimerTask[] schedule = new ScheduledTimerTask[rootIndexSet.size()];
        int counter = 0;

        for (Iterator iter = rootIndexSet.iterator(); iter.hasNext();) {
            try {
                IndexService idx = new IndexService((String) iter.next());
                TimerTask idxAggrTask = new IndexAggregator(idx, idxManager,
                        true);

                //Start Aggregtor without delay
                schedule[counter++] = new ScheduledTimerTask(idxAggrTask, 1);
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }

        //Start the index aggregator task
        TimerFactoryBean factory = new TimerFactoryBean();
        schedule[0].getTimerTask().run();
        factory.setScheduledTimerTasks(schedule);
        factory.afterPropertiesSet();
    }

    public void setIdxManager(GridServiceManager idxManager) {
        this.idxManager = idxManager;
    }
}
