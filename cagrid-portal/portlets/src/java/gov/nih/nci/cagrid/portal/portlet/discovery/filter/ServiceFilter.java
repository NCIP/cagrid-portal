package gov.nih.nci.cagrid.portal.portlet.discovery.filter;

import gov.nih.nci.cagrid.portal.domain.GridService;

import java.util.List;

/**
 * Interface for filtering services.
 * Implementation classes will filter services
 * based on some criteria
 * <p/>
 * Used to filter services in the View
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface ServiceFilter {

    /**
     * Filter a List of services based on
     * some pre-defined criteria.
     * Implementations can define their filter criterias
     *
     * @param services
     * @return
     */
    public List<GridService> filter(List<GridService> services);

    /**
     * Will return boolean flag if a service will be
     * filtered by implementing class
     *
     * @param service
     * @return
     */
    public boolean willBeFiltered(GridService service);
}
