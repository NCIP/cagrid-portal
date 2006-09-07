package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.portal.domain.DomainModel;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;
import gov.nih.nci.cagrid.portal.utils.GridUtils;
import gov.nih.nci.cagrid.portal.utils.MetadataAggregatorUtils;
import org.springframework.dao.DataAccessException;


/**
 * @version 1.0
 * @created 22-Jun-2006 6:56:33 PM
 */
public class MetadataAggregator extends AbstractAggregator {
    private RegisteredService service;
    private GridServiceManager gridServiceMgr;

    public MetadataAggregator(RegisteredService service,
                              GridServiceManager gridServiceMgr) {
        this.service = service;
        this.gridServiceMgr = gridServiceMgr;
    }

    public void run() {

        MetadataAggregatorUtils aggrUtil = new MetadataAggregatorUtils();

        try {
            ServiceMetadata mData = GridUtils.getServiceMetadata(service.getHandle());
            ResearchCenter domainRC = aggrUtil.loadRC(mData);
            try {
                gridServiceMgr.save(domainRC);
                service.setResearchCenter(domainRC);
            } catch (DataAccessException e) {
                _logger.error(e);
            }
        } catch (MetadataRetreivalException e) {
            _logger.warn("Error loading research center for " + service.getEPR());
        }

        //Load Domain Model
        try {
            gov.nih.nci.cagrid.metadata.dataservice.DomainModel dModel = GridUtils.getDomainModel(service.getHandle());
            DomainModel domainModel = aggrUtil.loadDomainModel(dModel);
            service.setDomainModel(domainModel);
        } catch (MetadataRetreivalException e) {
            //means is not a data service
            try {
                aggrUtil.loadOperations(service);
            } catch (ResourcePropertyRetrievalException e1) {
                _logger.warn("Service " + service.getEPR() + " has no domain model or operations.");
            }
        }

        try {
            _logger.debug("Saving RegisteredService");
            gridServiceMgr.save(service);
        } catch (DataAccessException e) {
            _logger.error(e);
        }
    }
}