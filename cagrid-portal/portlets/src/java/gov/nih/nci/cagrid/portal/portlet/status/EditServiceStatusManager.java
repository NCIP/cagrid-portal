/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.status;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service that will manipulate service status(s)
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@RemoteProxy(name = "ServiceStatusManager",
        creator = SpringCreator.class,
        creatorParams = @Param(name = "beanName",
                value = "serviceStatusManager"))
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
    @RemoteMethod
    public ServiceStatus banUnbanService(int serviceId) {
        GridService service = gridServiceDao.getById(serviceId);

        //Should be an aspect        
        if (discoveryModel.getLiferayUser() != null) {
            logger.debug("Found admin user. Will change service status");
            if (service.getCurrentStatus().equals(ServiceStatus.BANNED)) {
                logger.debug("Service is in Banned state. Will unban " + service.getUrl());
                gridServiceDao.unbanService(service);
            } else {
                logger.debug("Will Ban Service " + service.getUrl());
                gridServiceDao.banService(service);
            }
        }
        return service.getCurrentStatus();
    }

    @RemoteMethod
    @Transactional
    public boolean reloadMetadata(int serviceId) {
        GridService service = gridServiceDao.getById(serviceId);
        //Should be an aspect
        if (discoveryModel.getLiferayUser() != null) {
            logger.debug("Found admin user. Will schedule a metadata reload");
            //set it to some non null value            
            service.setMetadataHash("123");
            gridServiceDao.save(service);
        }
        return true;
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


