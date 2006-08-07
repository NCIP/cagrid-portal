package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.common.Address;
import gov.nih.nci.cagrid.metadata.common.ResearchCenterDescription;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;
import gov.nih.nci.cagrid.portal.utils.GridUtils;


/**
 * @version 1.0
 * @created 22-Jun-2006 6:56:33 PM
 */
public class MetadataAggregator extends AbstractAggregator {
    private RegisteredService rService;
    private GridServiceManager gridServiceMgr;

    public MetadataAggregator(RegisteredService rService,
        GridServiceManager gridServiceMgr) {
        this.rService = rService;
        this.gridServiceMgr = gridServiceMgr;
    }

    public void run() {
        // Only try this if metadata retreival looks possible
        if (rService.getName() != null) {
            _logger.debug("Starting metadata aggregator for" +
                rService.getName());

            //Map the metadata ResearchCenter into a
            //domain object
            ResearchCenter domainRC = new ResearchCenter();

            try {
                ServiceMetadata service = GridUtils.getServiceMetadata(rService.getHandle());
                gov.nih.nci.cagrid.metadata.common.ResearchCenter rc = service.getHostingResearchCenter()
                                                                              .getResearchCenter();
                domainRC.setDisplayName(rc.getDisplayName());
                domainRC.setShortName(rc.getShortName());

                ResearchCenterDescription rcDesc = rc.getResearchCenterDescription();

                if (rcDesc != null) {
                    domainRC.setDescription(rcDesc.getDescription());
                    domainRC.setHomepageURL(rcDesc.getHomepageURL());
                    domainRC.setImageURL(rcDesc.getImageURL());
                    domainRC.setRssNewsURL(rcDesc.getRssNewsURL());
                }

                Address rcAddress = rc.getAddress();

                if (rcAddress != null) {
                    domainRC.setStreet1(rcAddress.getStreet1());
                    domainRC.setStreet2(rcAddress.getStreet2());
                    domainRC.setState(rcAddress.getStateProvince());
                    domainRC.setPostalCode(rcAddress.getPostalCode());
                    domainRC.setLocality(rcAddress.getLocality());
                    domainRC.setCountry(rcAddress.getCountry());
                }
            } catch (MetadataRetreivalException e) {
                _logger.info("Error retreiving metadata for service" +
                    rService.getEPR());
            }

            rService.setResearchCenter(domainRC);
            _logger.debug("Saving RegisteredService with ResearchCenter Info");

            // Save the RC object first
            gridServiceMgr.save(domainRC);
            gridServiceMgr.save(rService);
        }
    }
}
