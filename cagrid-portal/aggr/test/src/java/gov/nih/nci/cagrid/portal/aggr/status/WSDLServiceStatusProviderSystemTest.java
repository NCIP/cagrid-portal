package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.util.PortalAggrIntegrationTestBase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class WSDLServiceStatusProviderSystemTest extends PortalAggrIntegrationTestBase {


    private String _nonCagridService = "http://www.google.com";
    private String cadsrUrl;


    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        cadsrUrl = (String) getApplicationContext().getBean("cadsrUrl");
    }

    public void testStatusProvider() {


        WSDLServiceStatusProvider _provider = new WSDLServiceStatusProvider();
        _provider.setTimeout(100000);
        assertEquals("Should not get wsdl from this url", ServiceStatus.INACTIVE, _provider.getStatus(_nonCagridService));
        assertEquals("Should get wsdl from URL", ServiceStatus.ACTIVE, _provider.getStatus(cadsrUrl));

        assertEquals(ServiceStatus.INACTIVE, _provider.getStatus("http://wsdl"));
        assertEquals(ServiceStatus.INACTIVE, _provider.getStatus("http://"));
    }


}
