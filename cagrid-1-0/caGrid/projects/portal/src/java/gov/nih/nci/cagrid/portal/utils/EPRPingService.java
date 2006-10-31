package gov.nih.nci.cagrid.portal.utils;

import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 25, 2006
 * Time: 5:45:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EPRPingService {

    /**
     * Will return true if service(epr)
     * is rechable currently.
     *
     * @param epr
     * @return
     */
    public boolean ping(EndpointReferenceType epr);
}
