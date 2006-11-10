package gov.nih.nci.cagrid.portal.utils;


import gov.nih.nci.cagrid.portal.BaseSpringAbstractTest;
import gov.nih.nci.cagrid.portal.common.GeoCodeValues;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.GeoCoderRetreivalException;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 8, 2006
 * Time: 12:41:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeoCoderTestCase extends BaseSpringAbstractTest {

    private DomainObjectGeocoder geocoder;


    public void setGeocoder(DomainObjectGeocoder geocoder) {
        this.geocoder = geocoder;
    }

    public void testGetGeoCode4RC() {
        ResearchCenter rc = new ResearchCenter();
        rc.setPostalCode("20852");

        try {
            GeoCodeValues geoString = geocoder.geocodeDomainObject(rc);
            assertNotNull(geoString);
        } catch (GeoCoderRetreivalException e) {
            fail(e.getMessage());
        }


    }


}
