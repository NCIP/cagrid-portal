package gov.nih.nci.cagrid.portal.portlet.discovery.filter;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class UserAwareServiceFilter implements ServiceFilter {


    private static final Log logger = LogFactory.getLog(UserAwareServiceFilter.class);

    private DiscoveryModel discoveryModel;


    public List<GridService> filter(List<GridService> services) {
        List<GridService> out = BaseServiceFilter.filterServicesByInvalidMetadata((services));

        if (discoveryModel.getLiferayUser() != null && discoveryModel.getLiferayUser().isAdmin()) {
            logger.debug("Admin user. Will not filter services");
            return out;
        }
        return BaseServiceFilter.filterServicesByStatus(out, ServiceStatus.BANNED, ServiceStatus.DORMANT);
    }

    public boolean willBeFiltered(final GridService service) {
        List _prefilter = new ArrayList<GridService>() {
            {
                add(service);
            }
        };

        return filter(_prefilter).size() == 0;
    }

    public DiscoveryModel getDiscoveryModel() {
        return discoveryModel;
    }

    public void setDiscoveryModel(DiscoveryModel discoveryModel) {
        this.discoveryModel = discoveryModel;
    }
}
