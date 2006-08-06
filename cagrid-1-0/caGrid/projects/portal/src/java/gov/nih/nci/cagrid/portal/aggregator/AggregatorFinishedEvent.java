package gov.nih.nci.cagrid.portal.aggregator;

import org.springframework.context.ApplicationEvent;
import gov.nih.nci.cagrid.portal.domain.GridService;

/**
 * Whenever an aggregator finsihes
 * processing, it can fire off this
 * event (with itself as source) to
 * alert other aggregators that it has finished processing
 * 
 *
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 5, 2006
 * Time: 11:14:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class AggregatorFinishedEvent extends ApplicationEvent {

    // The service (index or registry) that the source aggregator was processing
    GridService service;

    public AggregatorFinishedEvent(Object source, GridService service) {
        super(source);
        this.service = service;
    }

    public GridService getService() {
        return service;
    }
}
