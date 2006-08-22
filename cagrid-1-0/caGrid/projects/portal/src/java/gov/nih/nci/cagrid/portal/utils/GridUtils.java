package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.discovery.MetadataUtils;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 6:38:12 PM
 * To change this template use File | Settings | File Templates.
 */
public final class GridUtils {
    public static EndpointReferenceType getEPR(String epr)
        throws URI.MalformedURIException {
        return new EndpointReferenceType(new URI(epr));
    }

    /**
     * @param serviceEPR
     * @return Service Description
     */
    public static String getServiceDescription(EndpointReferenceType serviceEPR)
        throws MetadataRetreivalException {
        return GridUtils.getService(serviceEPR).getDescription();
    }

    /**
     * @param serviceEPR
     * @return
     * @throws MetadataRetreivalException
     */
    public static String getServiceName(EndpointReferenceType serviceEPR)
        throws MetadataRetreivalException {
        return GridUtils.getService(serviceEPR).getName();
    }

    public static String getServiceVersion(EndpointReferenceType serviceEPR)
        throws MetadataRetreivalException {
        return GridUtils.getService(serviceEPR).getVersion();
    }

    /**
     * Will return the Service metadata object for a given
     * grid service EPR.
     *
     * @param serviceEPR
     * @return
     * @throws MetadataRetreivalException
     */
    public static gov.nih.nci.cagrid.metadata.service.Service getService(
        EndpointReferenceType serviceEPR) throws MetadataRetreivalException {
        try {
            return GridUtils.getServiceMetadata(serviceEPR)
                            .getServiceDescription().getService();
        } catch (Exception e) {
            // wrap the generic exception into something more specific
            throw new MetadataRetreivalException(
                "Error retreiving service metadata for " +
                serviceEPR.toString());
        }
    }

    /**
     * @param service
     * @return boolean if service is alive and responding
     */
    public static boolean isServiceActive(EndpointReferenceType service) {
        //ToDo implement
        return true;
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
    public static gov.nih.nci.cagrid.metadata.ServiceMetadata getServiceMetadata(
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
            getDomainModel(EndpointReferenceType serviceEPR) throws MetadataRetreivalException{
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
