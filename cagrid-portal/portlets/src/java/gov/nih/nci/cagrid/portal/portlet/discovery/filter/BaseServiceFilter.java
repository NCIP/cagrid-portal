package gov.nih.nci.cagrid.portal.portlet.discovery.filter;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Default filter of services that
 * is not dependent on the web users
 * privileges. Will filter invalid, banned and dormant services
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class BaseServiceFilter implements ServiceFilter {

    public static List<GridService> filterBannedServices(List<GridService> in) {
        return filterServicesByStatus(in, ServiceStatus.BANNED);
    }

    public static List<GridService> filterDormantServices(List<GridService> in) {
        return filterServicesByStatus(in, ServiceStatus.DORMANT);

    }

    public static List<GridService> filterServicesByInvalidMetadata(
            List<GridService> in) {
        List<GridService> out = new ArrayList<GridService>();
        for (GridService svc : in) {
            boolean filter = false;
            try {
                if (svc.getServiceMetadata() == null || svc.getServiceMetadata().getServiceDescription() == null) {
                    filter = true;
                }
            } catch (Exception e) {
                filter = true;
            }
            if (!filter) {
                out.add(svc);
            }
        }
        return out;
    }

    public static List<GridService> filterServicesByStatus(
            List<GridService> in, ServiceStatus... statuses) {
        List<GridService> out = new ArrayList<GridService>();
        for (GridService svc : in) {
            boolean filter = false;
            for (ServiceStatus status : statuses) {
                if (status.equals(svc.getCurrentStatus())) {
                    filter = true;
                    break;
                }
            }
            if (!filter) {
                out.add(svc);
            }
        }
        return out;
    }

    public List<GridService> filter(List<GridService> services) {
        return filterServicesByStatus(filterServicesByInvalidMetadata((services)), ServiceStatus.BANNED, ServiceStatus.DORMANT);
    }

    public boolean willBeFiltered(GridService service) {
        return false;
    }
}
