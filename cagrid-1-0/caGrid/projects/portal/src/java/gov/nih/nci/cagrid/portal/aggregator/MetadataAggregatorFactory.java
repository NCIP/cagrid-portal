package gov.nih.nci.cagrid.portal.aggregator;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.timer.TimerFactoryBean;
import org.springframework.scheduling.timer.ScheduledTimerTask;

import java.util.TimerTask;


/**
 * Aggregates metadata information from a service.
 * <p/>
 * Can be triggered by a message(event)
 *
 * @version 1.0
 * @created 22-Jun-2006 6:56:33 PM
 */
public class MetadataAggregatorFactory extends AbstractAggregator {
    public MetadataAggregatorFactory() {
    }





    public void run() {
    }


    public void onApplicationEvent(ApplicationEvent applicationEvent) {
      _logger.debug("Event received .....................");
        if(applicationEvent.getSource() instanceof IndexAggregator)
        {
            AggregatorFinishedEvent event = (AggregatorFinishedEvent)applicationEvent;
            //schedule a Metadata Aggregator
            _logger.debug("Scheduling metadata aggregator for service" + event.getService().getName());
            TimerTask serviceMDAggr = new MetadataAggregator(event.getService());
            ScheduledTimerTask[] schedule = new ScheduledTimerTask[]{
                                                       new ScheduledTimerTask(serviceMDAggr)
                                                        };

            TimerFactoryBean factory = new TimerFactoryBean();
            factory.setScheduledTimerTasks(schedule);
            factory.afterPropertiesSet();
        }
    }
}
