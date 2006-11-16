package gov.nih.nci.cagrid.browser.util;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Nov 15, 2006
 * Time: 12:56:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridUtils {

    public static EndpointReferenceType getEPR(String epr)
            throws URI.MalformedURIException {
        return new EndpointReferenceType(new URI(epr));
    }

}
