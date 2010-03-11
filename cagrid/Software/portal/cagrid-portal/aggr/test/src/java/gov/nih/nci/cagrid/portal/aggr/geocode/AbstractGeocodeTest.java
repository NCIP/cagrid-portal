package gov.nih.nci.cagrid.portal.aggr.geocode;

import gov.nih.nci.cagrid.portal.util.PortalAggrIntegrationTestBase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AbstractGeocodeTest extends PortalAggrIntegrationTestBase {


    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-aggr-geocode.xml",
                "applicationContext-db.xml"
        };
    }

}
