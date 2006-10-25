package gov.nih.nci.cagrid.portal.utils;

import org.apache.axis.message.addressing.EndpointReferenceType;

import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 25, 2006
 * Time: 5:50:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class EPRPingServiceImpl implements EPRPingService {


    public boolean ping(EndpointReferenceType epr) throws RemoteException {
        return false;
    }
}
