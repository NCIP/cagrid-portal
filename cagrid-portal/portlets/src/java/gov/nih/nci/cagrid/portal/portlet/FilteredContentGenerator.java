package gov.nih.nci.cagrid.portal.portlet;

import gov.nih.nci.cagrid.portal.portlet.discovery.filter.ServiceFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class FilteredContentGenerator extends org.springframework.web.portlet.handler.PortletContentGenerator {

    protected Log logger = LogFactory.getLog(getClass());

    private ServiceFilter filter;

    public ServiceFilter getFilter() {
        return filter;
    }

    public void setFilter(ServiceFilter filter) {
        this.filter = filter;
    }


}
