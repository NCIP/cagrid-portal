package gov.nih.nci.cagrid.portal.aggr.geocode;

import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.Geocode;
import gov.nih.nci.cagrid.portal.util.PortalAggrIntegrationTestBase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class YahooGeocoderSystemTest extends PortalAggrIntegrationTestBase {

    YahooGeocoder defaultGeocoder;

    public void testGeocode() throws Exception {
        Address address = new Address();
        address.setCountry("US");
        address.setPostalCode("20855");

        Geocode geoCode = defaultGeocoder.getGeocode(address);
        assertNotNull(geoCode);
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-aggr-geocode.xml",
                "applicationContext-db.xml"
        };
    }

    public YahooGeocoder getDefaultGeocoder() {
        return defaultGeocoder;
    }

    public void setDefaultGeocoder(YahooGeocoder defaultGeocoder) {
        this.defaultGeocoder = defaultGeocoder;
    }
}
