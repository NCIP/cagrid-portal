package gov.nih.nci.cagrid.portal.utils;

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

    public static EndpointReferenceType getEPR(String epr) throws URI.MalformedURIException {
        return new EndpointReferenceType(new URI(epr));
    }
}
