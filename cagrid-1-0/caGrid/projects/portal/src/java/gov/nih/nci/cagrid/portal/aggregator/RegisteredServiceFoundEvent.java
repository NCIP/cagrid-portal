package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.portal.domain.RegisteredService;

import org.springframework.context.ApplicationEvent;


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
public class RegisteredServiceFoundEvent extends ApplicationEvent {
    private RegisteredService rService;

    public RegisteredServiceFoundEvent(Object source, RegisteredService rService) {
        super(source);
        this.rService = rService;
    }

    public RegisteredService getrService() {
        return rService;
    }
}
