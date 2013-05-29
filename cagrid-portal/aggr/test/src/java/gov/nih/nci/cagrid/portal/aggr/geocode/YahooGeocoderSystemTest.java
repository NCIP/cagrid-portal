/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.aggr.geocode;

import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.Geocode;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class YahooGeocoderSystemTest extends AbstractGeocodeTest {

    YahooGeocoder defaultGeocoder;

    public void testGeocode() throws Exception {
        Address address = new Address();
        address.setCountry("US");
        address.setPostalCode("20855");

        Geocode geoCode = defaultGeocoder.getGeocode(address);
        assertNotNull(geoCode);
    }

    public YahooGeocoder getDefaultGeocoder() {
        return defaultGeocoder;
    }

    public void setDefaultGeocoder(YahooGeocoder defaultGeocoder) {
        this.defaultGeocoder = defaultGeocoder;
    }
}
