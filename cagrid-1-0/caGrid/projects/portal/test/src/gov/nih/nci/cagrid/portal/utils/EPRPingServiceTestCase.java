package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.BaseSpringAbstractTestCase;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 25, 2006
 * Time: 6:05:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class EPRPingServiceTestCase extends BaseSpringAbstractTestCase {

    private EPRPingService eprPingService;
    private final String fooEPR = "http://foo.bar";

    public void testEPR() {
        try {
            EndpointReferenceType epr = new EndpointReferenceType(new URI(fooEPR));
            assertFalse(eprPingService.ping(epr));

            assertTrue(eprPingService.ping(validServiceEPR));
        } catch (URI.MalformedURIException e) {
            fail(e.getMessage());
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    public void setEprPingService(EPRPingService eprPingService) {
        this.eprPingService = eprPingService;
    }
}
