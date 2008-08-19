package gov.nih.nci.cagrid.portal.portlet.status;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Service that will manipulate service status(s)
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EditServiceStatusManager {

    private static final Log logger = LogFactory.getLog(EditServiceStatusManager.class);

    private GridServiceDao gridServiceDao;
    private DiscoveryModel discoveryModel;

    /**
     * Will ban/unban services
     *
     * @param serviceId
     * @return
     */
    public ServiceStatus banUnbanService(int serviceId) {
        GridService service = gridServiceDao.getById(serviceId);

        //Should be an aspect        
//        if(discoveryModel.getLiferayUser()!=null){
        logger.debug("Found admin user. Will change service status");
        if (service.getCurrentStatus().equals(ServiceStatus.BANNED)) {
            logger.debug("Service is in Banned state. Will unban " + service.getUrl());
            gridServiceDao.unbanService(service);
        } else {
            logger.debug("Will Ban Service " + service.getUrl());
            gridServiceDao.banService(service);
        }
//        }
        return service.getCurrentStatus();
    }

    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    public DiscoveryModel getDiscoveryModel() {
        return discoveryModel;
    }

    public void setDiscoveryModel(DiscoveryModel discoveryModel) {
        this.discoveryModel = discoveryModel;
    }
}


