package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.discovery.MetadataUtils;
import gov.nih.nci.cagrid.discovery.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.service.ServiceContext;
import gov.nih.nci.cagrid.portal.domain.*;
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

                loadDescription(domainRC, rc);
                loadAddress(domainRC, rc);
                loadPOC(domainRC, rc);

            } catch (MetadataRetreivalException e) {
                _logger.error("Error loading metadata for " + rService.getEPR());
                _logger.error(e);
            }

            _logger.debug("Adding RC with " + domainRC.getPocCollection().size() + " POC's");

            rService.setResearchCenter(domainRC);

            //Load Domain Model
            try {
                loadDomainModel(rService);
            } catch (MetadataRetreivalException e) {
                //means is not a data service
                try {
                    loadOperations(rService);
                } catch (ResourcePropertyRetrievalException e1) {
                    _logger.info(e1);
                }
            }

            _logger.debug("Saving RegisteredService with ResearchCenter Info");

            // Save the RC object first
            //` gridServiceMgr.save(domainRC);
            gridServiceMgr.save(rService);
        }
    }

    private void loadDescription(ResearchCenter domainRC, gov.nih.nci.cagrid.metadata.common.ResearchCenter rc) {
        gov.nih.nci.cagrid.metadata.common.ResearchCenterDescription rcDesc = rc.getResearchCenterDescription();

        if (rcDesc != null) {
            domainRC.setDescription(rcDesc.getDescription());
            domainRC.setHomepageURL(rcDesc.getHomepageURL());
            domainRC.setImageURL(rcDesc.getImageURL());
            domainRC.setRssNewsURL(rcDesc.getRssNewsURL());
        }

    }

    private void loadAddress(ResearchCenter domainRC, gov.nih.nci.cagrid.metadata.common.ResearchCenter rc) {

        gov.nih.nci.cagrid.metadata.common.Address rcAddress = rc.getAddress();

        if (rcAddress != null) {
            domainRC.setStreet1(rcAddress.getStreet1());
            domainRC.setStreet2(rcAddress.getStreet2());
            domainRC.setState(rcAddress.getStateProvince());
            domainRC.setPostalCode(rcAddress.getPostalCode());
            domainRC.setLocality(rcAddress.getLocality());
            domainRC.setCountry(rcAddress.getCountry());
        }
    }

    private void loadPOC(ResearchCenter domainRC, gov.nih.nci.cagrid.metadata.common.ResearchCenter rc) {

        //Domain Object
        PointOfContact pocDomain = new PointOfContact();
        gov.nih.nci.cagrid.metadata.common.PointOfContact[] pocCollection = rc.getPointOfContactCollection().getPointOfContact();

        for (int i = 0; i < pocCollection.length; i++) {
            gov.nih.nci.cagrid.metadata.common.PointOfContact poc = pocCollection[i];
            pocDomain.setAffiliation(poc.getAffiliation());
            pocDomain.setRole(poc.getRole());
            pocDomain.setFirstName(poc.getFirstName());
            pocDomain.setLastName(poc.getLastName());
            pocDomain.setPhoneNumber(poc.getPhoneNumber());
            pocDomain.setEmail(poc.getEmail());
        }
    }

    private void loadDomainModel(RegisteredService rService) throws MetadataRetreivalException {
        gov.nih.nci.cagrid.metadata.dataservice.DomainModel dModel = GridUtils.getDomainModel(rService.getHandle());

        DomainModel modelDomain = new DomainModel();
        modelDomain.setLongName(dModel.getProjectLongName());
        modelDomain.setProjectShortName(dModel.getProjectShortName());
        modelDomain.setProjectDescription(dModel.getProjectDescription());
        modelDomain.setProjectVersion(dModel.getProjectVersion());
        gov.nih.nci.cagrid.metadata.common.UMLClass classes[] = dModel.getExposedUMLClassCollection().getUMLClass();

        for (int i = 0; i < classes.length; i++) {
            UMLClass dClass = new UMLClass();
            dClass.setClassName(classes[i].getClassName());
            modelDomain.getUmlClassCollection().add(dClass);
        }

        rService.setDomainModel(modelDomain);
    }

    public void loadOperations(RegisteredService rervice) throws ResourcePropertyRetrievalException {
        ServiceContext[] contexts = MetadataUtils.getServiceMetadata(rService.getHandle()).getServiceDescription().getService().getServiceContextCollection().getServiceContext();
        for (int i = 0; i < contexts.length; i++) {
            gov.nih.nci.cagrid.metadata.service.Operation opers[] = contexts[i].getOperationCollection().getOperation();
            for (int j = 0; j < opers.length; j++) {
                gov.nih.nci.cagrid.metadata.service.Operation operation = opers[j];
                Operation operDomain = new Operation();
                operDomain.setName(operation.getName());
                operDomain.setDescription(operation.getDescription());
                rService.getOperationCollection().add(operDomain);
            }

        }


    }

}
