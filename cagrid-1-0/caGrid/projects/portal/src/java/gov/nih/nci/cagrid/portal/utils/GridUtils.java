package gov.nih.nci.cagrid.portal.utils;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.discovery.MetadataUtils;
import gov.nih.nci.cagrid.portal.domain.GridService;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 6:38:12 PM
 * To change this template use File | Settings | File Templates.
 */
public final class GridUtils {


   public static EndpointReferenceType getEPR(String epr) throws URI.MalformedURIException {
        return new EndpointReferenceType(new URI(epr));
    }

    /**
     *
     * @param serviceEPR
     * @return Service Description
     */
    public static String getServiceDescription(EndpointReferenceType serviceEPR){
        try {
            return GridUtils.getServiceMetadata(serviceEPR).getDescription();
        } catch (Exception e) {
            // Log exception and return null. Properties are sometimes not returned for services, if not set
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }

    /**
     *
     * @param serviceEPR
     * @return Service Name

     */
     public static String getServiceName(EndpointReferenceType serviceEPR){
        try {
            return GridUtils.getServiceMetadata(serviceEPR).getName();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            // Log exception and return null. Properties are sometimes not returned for services, if not set
            return null;
        }

    }

    /**
     * Will return the Service metadata object for a given
     * grid service EPR
     * @param serviceEPR
     * @return
     * @throws Exception
     */
     public static gov.nih.nci.cagrid.metadata.service.Service getServiceMetadata(EndpointReferenceType serviceEPR)throws Exception{

        return MetadataUtils.getServiceMetadata(serviceEPR).getServiceDescription().getService();



    }

    /**
     *
     * @param service
     * @return boolean if service is alive and responding
     */
     public static boolean isServiceActive(EndpointReferenceType service){
        //ToDo implement
        return true;


}
}
