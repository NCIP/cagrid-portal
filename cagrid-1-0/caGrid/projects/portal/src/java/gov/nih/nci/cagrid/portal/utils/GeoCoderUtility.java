package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.GeoCoderRetreivalException;
import org.apache.log4j.Category;
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
    protected Category _logger = Category.getInstance(getClass().getName());


    public final GeocodeResult getGeoCode4RC(ResearchCenter rc) throws GeoCoderRetreivalException {
        GeoCode_Service gService = new GeoCode_ServiceLocator();

        GeocoderResult[] result = new GeocoderResult[0];
        try {
            GeoCode_PortType gPort = gService.getGeoCode_Port();
            result = gPort.geocode(rc.getPostalCode());
        } catch (ServiceException e) {
            _logger.error(e);
            throw new GeoCoderRetreivalException(e);
        } catch (RemoteException e) {
            _logger.error(e);
            throw new GeoCoderRetreivalException(e);
        }
        GeocodeResult geoCode = new GeocodeResult();
        geoCode.setLatitude(new Float(result[0].getLat()));
        geoCode.setLongitude(new Float(result[0].get_long()));

        return geoCode;
    }

    public class GeocodeResult {
        private Float latitude;
        private Float longitude;

        public Float getLatitude() {
            return latitude;
        }

        public void setLatitude(Float latitude) {
            this.latitude = latitude;
        }

        public Float getLongitude() {
            return longitude;
        }

        public void setLongitude(Float longitude) {
            this.longitude = longitude;
        }
    }

}
