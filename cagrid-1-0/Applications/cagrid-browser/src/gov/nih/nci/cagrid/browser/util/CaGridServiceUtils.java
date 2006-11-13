package gov.nih.nci.cagrid.browser.util;

import gov.nih.nci.cagrid.browser.exception.MetadataRetreivalException;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 6:38:12 PM
 * To change this template use File | Settings | File Templates.
 */
public final class CaGridServiceUtils {

    private EndpointReferenceType epr;
    private gov.nih.nci.cagrid.metadata.ServiceMetadata metadata;

    public CaGridServiceUtils(EndpointReferenceType serviceEPR) throws MetadataRetreivalException {
        this.epr = serviceEPR;
        try {
            this.metadata = getServiceMetadata(serviceEPR);
        } catch (Exception e) {
            // wrap the generic exception into something more specific
            throw new MetadataRetreivalException("Error retreiving service metadata for " +
                    serviceEPR.toString());
        }
    }


    public static EndpointReferenceType getEPR(String epr)
            throws URI.MalformedURIException {
        return new EndpointReferenceType(new URI(epr));

    }


    public String getServiceDescription() {
        return this.metadata.getServiceDescription().getService().getDescription();
    }

    public String getServiceName() {
        return this.metadata.getServiceDescription().getService().getName();
    }

    public String getServiceVersion(EndpointReferenceType serviceEPR)
            throws MetadataRetreivalException {
        return this.metadata.getServiceDescription().getService().getVersion();
    }


    /**
     * Main method that all other
     * methods in  this class depend upon.
     * ANytime Discovery API changes, you only
     * have to change this one method.
     *
     * @param serviceEPR
     * @return
     * @throws MetadataRetreivalException
     */
    public gov.nih.nci.cagrid.metadata.ServiceMetadata getServiceMetadata(
            EndpointReferenceType serviceEPR) throws MetadataRetreivalException {
        try {
            return MetadataUtils.getServiceMetadata(serviceEPR);
        } catch (Exception e) {
            // wrap the generic exception into something more specific
            throw new MetadataRetreivalException(
                    "Error retreiving service metadata for " +
                            serviceEPR.toString());
        }
    }

    public static gov.nih.nci.cagrid.metadata.dataservice.DomainModel
            getDomainModel(EndpointReferenceType serviceEPR) throws MetadataRetreivalException {
        try {
            return MetadataUtils.getDomainModel(serviceEPR);
        } catch (Exception e) {
            // wrap the generic exception into something more specific
            throw new MetadataRetreivalException(
                    "Error retreiving service metadata for " +
                            serviceEPR.toString());
        }
    }
}
