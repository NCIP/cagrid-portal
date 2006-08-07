package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Timer;
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
    private static int reloaded = -1;
    private GridServiceManager gridServiceMgr;

    public MetadataAggregatorFactory(GridServiceManager gridServiceMgr) {
        this.gridServiceMgr = gridServiceMgr;
    }

    public void run() {
    }

    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        _logger.debug("Event received .....................");

        if (applicationEvent instanceof RegisteredServiceFoundEvent) {
            RegisteredServiceFoundEvent eventIndexRegistered = (RegisteredServiceFoundEvent) applicationEvent;
            RegisteredService rService = eventIndexRegistered.getrService();

            //schedule a Metadata Aggregator
            _logger.debug("Scheduling metadata aggregator for service" +
                rService.getName());

            TimerTask serviceMDAggr = new MetadataAggregator(rService,
                    gridServiceMgr);
            Timer timer = new Timer(true);
            timer.schedule(serviceMDAggr, 1);
        } else if (applicationEvent instanceof ContextRefreshedEvent) {
            reloaded++;
            _logger.info("Context was reloaded or loaded for the first time. " +
                reloaded);
        } else if (applicationEvent instanceof ContextClosedEvent) {
            _logger.info("Context was closed.");
        } else {
            _logger.info("EVENT: " + applicationEvent.getClass().getName());
        }
    }
}
