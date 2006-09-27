package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.portal.domain.DomainModel;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;
import gov.nih.nci.cagrid.portal.utils.GridUtils;
import gov.nih.nci.cagrid.portal.utils.MetadataAggregatorUtils;


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
            try {
                ServiceMetadata mData = GridUtils.getServiceMetadata(service.getHandle());
                ResearchCenter domainRC = aggrUtil.loadRC(mData);

                service.setResearchCenter(domainRC);

                //Load Domain Model
                try {
                    DomainModel domainModel = aggrUtil.loadDomainModel(GridUtils.getDomainModel(service.getHandle()));
                    service.setObjectModel(domainModel);
                } catch (MetadataRetreivalException e) {
                    //means is not a data service
                    try {
                        aggrUtil.loadOperations(service, mData);
                    } catch (ResourcePropertyRetrievalException e1) {
                        _logger.warn("Service " + service.getEPR() + " has no domain model or operations.");
                    }
                }

            } catch (MetadataRetreivalException e) {
                _logger.warn("Error loading research center for " + service.getEPR());
            }

            gridServiceMgr.save(service);
        } catch (PortalRuntimeException e) {
            _logger.error(e);
        }
    }
}