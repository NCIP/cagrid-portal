package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.BaseSpringAbstractTest;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 25, 2006
 * Time: 6:05:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class EPRPingServiceTestCase extends BaseSpringAbstractTest {


    private final String fooEPR = "https://59.163.69.32:8443/wsrf/services/cagrid/CaDSRService";

    public void testEPR() {
        try {
            EndpointReferenceType epr = new EndpointReferenceType(new URI(fooEPR));
            assertTrue(EPRPingService.ping(epr) == EPRPingService.SERVICE_INACTIVE);

            assertTrue(EPRPingService.ping(validServiceEPR) == EPRPingService.SERVICE_ACTIVE);
        } catch (Exception e) {
            fail(e.getMessage());

        }
    }

}
