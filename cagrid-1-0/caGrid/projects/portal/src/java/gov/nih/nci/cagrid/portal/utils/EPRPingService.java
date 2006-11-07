package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.metadata.MetadataConstants;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.oasis.wsrf.properties.WSResourcePropertiesServiceAddressingLocator;

/**
 * Utility that will ping an EPR to see if the endpoint is "Active" or "Inactive"
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 25, 2006
 * Time: 5:50:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class EPRPingService {


    /**
     * Utility that will ping an EPR to see if the endpoint is "Active" or "Inactive"
     *
     * @param epr
     * @return boolean True means EPR is "Alive" or "Active"
     */
    public static boolean ping(EndpointReferenceType epr) {
        WSResourcePropertiesServiceAddressingLocator locator = new WSResourcePropertiesServiceAddressingLocator();

        try {
            locator.getGetResourcePropertyPort(epr).getResourceProperty(MetadataConstants.CAGRID_MD_QNAME);
        } catch (Exception e) {
            //service is not reachable
            return false;
        }

        //if it reaches this point
        return true;
    }
}
