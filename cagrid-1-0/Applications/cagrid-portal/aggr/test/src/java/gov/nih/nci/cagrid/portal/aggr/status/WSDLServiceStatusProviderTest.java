package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import junit.framework.TestCase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class WSDLServiceStatusProviderTest extends TestCase {

    private String _cagridservice = "http://cagrid-service.nci.nih.gov:8080/wsrf/services/cagrid/GlobalModelExchange";
    private String _nonCagridService = "http://www.google.com";


    public void testStatusProvider() {


        WSDLServiceStatusProvider _provider = new WSDLServiceStatusProvider();
        assertEquals("Should not get wsdl from this url", ServiceStatus.INACTIVE, _provider.getStatus(_nonCagridService));
        assertEquals("Should get wsdl from URL", ServiceStatus.ACTIVE, _provider.getStatus(_cagridservice));

        assertEquals(ServiceStatus.INACTIVE, _provider.getStatus("http://wsdl"));
        assertEquals(ServiceStatus.INACTIVE, _provider.getStatus("http://"));
    }
}
