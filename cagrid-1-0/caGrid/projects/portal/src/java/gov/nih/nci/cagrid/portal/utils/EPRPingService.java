package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.metadata.MetadataConstants;
import gov.nih.nci.cagrid.metadata.ResourcePropertyHelper;
import gov.nih.nci.cagrid.metadata.exceptions.InvalidResourcePropertyException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * Utility that will ping an EPR to see if the endpoint is
 * "Active" "Inactive" or "INVALID"
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 25, 2006
 * Time: 5:50:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class EPRPingService {

    /**
     * service is active and reachable *
     */
    public final static int SERVICE_ACTIVE = 1;
    /**
     * service is not reachable or is inactive currently *
     */
    public final static int SERVICE_INACTIVE = 0;
    /**
     * service is reachable but is not a valid caGrid service *
     */
    public final static int SERVICE_INVALID = -1;

    /**
     * Utility that will ping an EPR to see if the endpoint is "Active" or "Inactive"
     *
     * @param epr
     * @return int 0 means service is inactive or not responding
     *         1 means service is active and alive at the given EPR
     *         -1 means service is active but not a valid caGrid service
     */
    public static int ping(EndpointReferenceType epr) {
    	 try {
         	ResourcePropertyHelper.getResourceProperty(epr,
 					MetadataConstants.CAGRID_MD_QNAME);
         }catch(InvalidResourcePropertyException ex){
         	return SERVICE_INVALID;
         }catch(RemoteResourcePropertyRetrievalException ex){
         	return SERVICE_INACTIVE;
         }catch(ResourcePropertyRetrievalException ex){
         	throw new RuntimeException("Error checking status of service: " + ex.getMessage(), ex);
         }

         //if it reaches this point
         return SERVICE_ACTIVE;
    }

    /**
     * Will explain the code you get from the ping(EPR epr) method
     *
     * @param code
     * @return
     */
    public static String explainCode(int code) {
        if (code == SERVICE_ACTIVE) {
            return "Service is active";
        } else if (code == SERVICE_INACTIVE) {
            return "Service is not currently active";
        } else if (code == SERVICE_INVALID) {
            return "Service is not a valid caGrid service. Not publishing expected metadata";
        }

        return null;
    }
}
