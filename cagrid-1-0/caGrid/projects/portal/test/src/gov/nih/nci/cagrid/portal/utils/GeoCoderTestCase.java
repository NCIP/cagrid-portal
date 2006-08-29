package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.GeoCoderRetreivalException;
import junit.framework.TestCase;
import us.geocoder.rpc.Geo.Coder.US.GeoCode_PortType;
import us.geocoder.rpc.Geo.Coder.US.GeoCode_Service;
import us.geocoder.rpc.Geo.Coder.US.GeoCode_ServiceLocator;
import us.geocoder.rpc.Geo.Coder.US.GeocoderResult;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 8, 2006
 * Time: 12:41:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeoCoderTestCase extends TestCase {

    public void testGetGeoCode4RC() {
        ResearchCenter rc = new ResearchCenter();
        rc.setPostalCode("20852");

        GeoCoderUtility coder = new GeoCoderUtility();
        try {
            GeoCoderUtility.GeocodeResult geoString = coder.getGeoCode4RC(rc);
            assertNotNull(geoString);
        } catch (GeoCoderRetreivalException e) {
            fail(e.getMessage());
        }


    }

    public void testGeoCoderWS() {
        GeoCode_Service gService = new GeoCode_ServiceLocator();
        try {
            GeoCode_PortType gPort = gService.getGeoCode_Port();
            GeocoderResult[] result = gPort.geocode("20852");
            assertNotNull(result);
            assertNotNull(result[0]);
        } catch (Exception e) {
            fail(e.getMessage());

        }
    }


}
