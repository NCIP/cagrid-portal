package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.domain.GeoCodeValues;
import gov.nih.nci.cagrid.portal.domain.GeocodedDomainObject;
import gov.nih.nci.cagrid.portal.exception.GeoCoderRetreivalException;
import us.geocoder.rpc.Geo.Coder.US.GeoCode_PortType;
import us.geocoder.rpc.Geo.Coder.US.GeoCode_Service;
import us.geocoder.rpc.Geo.Coder.US.GeoCode_ServiceLocator;
import us.geocoder.rpc.Geo.Coder.US.GeocoderResult;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 8, 2006
 * Time: 12:24:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeoCoderUtility {

    public static GeoCodeValues geocodeDomainObject(GeocodedDomainObject obj) throws GeoCoderRetreivalException {
        GeoCode_Service gService = new GeoCode_ServiceLocator();

        GeocoderResult[] result = new GeocoderResult[0];
        try {
            GeoCode_PortType gPort = gService.getGeoCode_Port();
            result = gPort.geocode(obj.getPostalCode());
        } catch (ServiceException e) {
            throw new GeoCoderRetreivalException(e);
        } catch (RemoteException e) {
            throw new GeoCoderRetreivalException(e);
        }
        GeoCodeValues geoCode = new GeoCodeValues();
        geoCode.setLatitude(new Float(result[0].getLat()));
        geoCode.setLongitude(new Float(result[0].get_long()));

        return geoCode;
    }
}
