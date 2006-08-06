package gov.nih.nci.cagrid.portal.aggregator;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import gov.nih.nci.cagrid.portal.domain.GridService;


/**
 * @version 1.0
 * @created 22-Jun-2006 6:56:33 PM
 */
public class MetadataAggregator extends AbstractAggregator {

    GridService service;

    public MetadataAggregator(GridService service) {
        this.service = service;
    }




    public void run() {
        _logger.debug("Aggregating metadata for service" + service.getName());
    }
}
