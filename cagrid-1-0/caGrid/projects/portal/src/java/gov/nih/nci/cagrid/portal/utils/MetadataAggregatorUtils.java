package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.service.ServiceContext;
import gov.nih.nci.cagrid.portal.domain.*;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 6, 2006
 * Time: 4:38:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetadataAggregatorUtils {

    public MetadataAggregatorUtils() {
    }

    /**
     * @param mData
     * @return Reserch Center from the service metadata. Service metadata has the Point of
     *         Contacts also
     * @throws MetadataRetreivalException
     */
    public final ResearchCenter loadRC(ServiceMetadata mData) throws MetadataRetreivalException {
        ResearchCenter domainRC = new ResearchCenter();

        gov.nih.nci.cagrid.metadata.common.ResearchCenter rc = mData.getHostingResearchCenter().getResearchCenter();
        domainRC.setDisplayName(rc.getDisplayName());
        domainRC.setShortName(rc.getShortName());

        loadDescription(domainRC, rc);
        loadAddress(domainRC, rc);
        loadPOC(domainRC, rc);

        return domainRC;
    }

    /**
     * @param dModel DomainModel from the service metadata
     * @return
     * @throws MetadataRetreivalException
     */
    public final DomainModel loadDomainModel(gov.nih.nci.cagrid.metadata.dataservice.DomainModel dModel) throws MetadataRetreivalException {


        DomainModel modelDomain = new DomainModel();
        modelDomain.setLongName(dModel.getProjectLongName());
        modelDomain.setProjectShortName(dModel.getProjectShortName());
        modelDomain.setProjectDescription(dModel.getProjectDescription());
        modelDomain.setProjectVersion(dModel.getProjectVersion());
        gov.nih.nci.cagrid.metadata.common.UMLClass classes[] = dModel.getExposedUMLClassCollection().getUMLClass();

        for (int i = 0; i < classes.length; i++) {
            UMLClass dClass = translateUMLClass(classes[i]);
            modelDomain.getUmlClassCollection().add(dClass);
        }
        return modelDomain;
    }

    public final void loadOperations(RegisteredService rService, gov.nih.nci.cagrid.metadata.ServiceMetadata sMetadata) throws ResourcePropertyRetrievalException {
        ServiceContext[] contexts = sMetadata.getServiceDescription().getService().getServiceContextCollection().getServiceContext();
        for (int i = 0; i < contexts.length; i++) {
            gov.nih.nci.cagrid.metadata.service.Operation opers[] = contexts[i].getOperationCollection().getOperation();
            if (opers != null) {
                for (int j = 0; j < opers.length; j++) {
                    Operation operDomain = new Operation();
                    operDomain.setName(opers[j].getName());
                    operDomain.setDescription(opers[j].getDescription());

                    if (opers[j].getOutput().getUMLClass() != null) {
                        UMLClass outputClass = translateUMLClass(opers[j].getOutput().getUMLClass());
                        operDomain.setOutput(outputClass);
                    }

                    rService.addOperation(operDomain);
                }
            }
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
            //save and add to Registered Service

            domainRC.addPOC(pocDomain);
        }
    }


    /**
     * Transforms metadata UMLClass object into
     * a UMLClass domain class
     *
     * @param umlClass
     * @return
     */
    private UMLClass translateUMLClass(gov.nih.nci.cagrid.metadata.common.UMLClass umlClass) {
        UMLClass domainClass = new UMLClass();
        domainClass.setClassName(umlClass.getClassName());
        domainClass.setDescription(umlClass.getDescription());
        domainClass.setPackageName(umlClass.getPackageName());
        domainClass.setProjectName(umlClass.getProjectName());
        domainClass.setProjectVersion(umlClass.getProjectVersion());
        return domainClass;

    }
}
