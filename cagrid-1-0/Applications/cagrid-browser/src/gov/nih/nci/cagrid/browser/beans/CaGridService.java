package gov.nih.nci.cagrid.browser.beans;


import gov.nih.nci.cagrid.browser.util.AppUtils;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.service.CaDSRRegistration;
import gov.nih.nci.cagrid.metadata.service.ServiceContext;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a caGrid Service
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Nov 9, 2006
 * Time: 2:06:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class CaGridService {

    private static Logger logger = Logger.getLogger(CaGridService.class);
    private EndpointReferenceType epr;

    private String name;
    private String description;
    private String version;
    private ResearchCenter rcInfo;
    private PointOfContact[] pocList;
    private DomainModel domainModel;
    private CaDSRRegistration caDSRRegistration;
    private List opersCollection = new ArrayList();

    public CaGridService(EndpointReferenceType epr) {
        this.epr = epr;
        loadLightMetadata();
    }

    public void loadLightMetadata() {
        try {
            gov.nih.nci.cagrid.metadata.ServiceMetadata metadata = MetadataUtils.getServiceMetadata(this.epr);

            this.description = metadata.getServiceDescription().getService().getDescription();
            this.name = metadata.getServiceDescription().getService().getName();
            this.version = metadata.getServiceDescription().getService().getVersion();

        } catch (ResourcePropertyRetrievalException e) {
            logger.warn("Error retrieving research info: " + e.getMessage());
        }

    }

    public String navigateToServiceDetails() {
        DiscoveredServices disc = (DiscoveredServices) AppUtils.getBean("discoveryResult");
        disc.setNavigatedService(this);

        return "success";
    }

    /**
     * Will force the bean to self populate
     * with metadata
     */
    public void fillMetadata() {
        try {
            gov.nih.nci.cagrid.metadata.ServiceMetadata metadata = MetadataUtils.getServiceMetadata(this.epr);
            rcInfo = metadata.getHostingResearchCenter().getResearchCenter();
            pocList = metadata.getServiceDescription().getService().getPointOfContactCollection().getPointOfContact();
            domainModel = MetadataUtils.getDomainModel(this.epr);

            //try getting operations if no data model
            loadOperations(metadata);

            caDSRRegistration = metadata.getServiceDescription().getService().getCaDSRRegistration();
        } catch (ResourcePropertyRetrievalException e) {
            logger.warn("Error retrieving metadata for: " + e.getMessage());

        }

    }

    public final void loadOperations(gov.nih.nci.cagrid.metadata.ServiceMetadata sMetadata) throws ResourcePropertyRetrievalException {
        opersCollection.clear();
        try {
            ServiceContext[] contexts = sMetadata.getServiceDescription().getService().getServiceContextCollection().getServiceContext();
            for (int i = 0; i < contexts.length; i++) {
                //load operations from multiple contexts within the service
                gov.nih.nci.cagrid.metadata.service.Operation opers[] = contexts[i].getOperationCollection().getOperation();
                if (opers != null) {
                    for (int j = 0; j < opers.length; j++) {
                        opersCollection.add(opers[j]);
                    }
                }
            }
        } catch (NullPointerException e) {
            //expected. Ignore
        }
    }

    public List getOpersCollection() {
        return opersCollection;
    }

    public EndpointReferenceType getEpr() {
        return epr;
    }

    public void setEpr(EndpointReferenceType epr) {
        this.epr = epr;
    }


    public String getUrl() {
        return this.epr.toString();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public ResearchCenter getRcInfo() {
        return rcInfo;
    }

    public PointOfContact[] getPocList() {
        return pocList;
    }

    public DomainModel getDomainModel() {
        return domainModel;
    }

    public CaDSRRegistration getCaDSRRegistration() {
        return caDSRRegistration;
    }

}
