package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.common.GeoCodeValues;
import gov.nih.nci.cagrid.portal.domain.GeocodedDomainObject;
import gov.nih.nci.cagrid.portal.exception.GeoCoderRetreivalException;
import org.apache.log4j.Category;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.HttpURLConnection;

/**
 * Utility class that will parse and implement
 * the geocder interface
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 8, 2006
 * Time: 12:24:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeoCoderUtility implements DomainObjectGeocoder {

    private String baseURL;
    private String geocoderAppID;
    private Category _logger = Category.getInstance(this.getClass());

    public GeoCodeValues geocodeDomainObject(GeocodedDomainObject obj) throws GeoCoderRetreivalException {

        try {
            URLConstructor url = new URLConstructor(baseURL + "?" + "appid=" + geocoderAppID);
            url.add("zip", obj.getPostalCode());

            HttpURLConnection conn = (HttpURLConnection) (url.toURL().openConnection());
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);

            XMLResult handler = new XMLResult();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            saxParser.parse(conn.getInputStream(), handler);

            GeoCodeValues geoCode = new GeoCodeValues();
            geoCode.setLatitude(new Float(handler.latValue));
            geoCode.setLongitude(new Float(handler.longValue));

            return geoCode;
        } catch (Exception e) {
            _logger.error("GeoCoderUtility: Error doinga remote geocode lookup" + e.getMessage());
            throw new GeoCoderRetreivalException(e);
        }
    }


    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }


    public void setGeocoderAppID(String geocoderAppID) {
        this.geocoderAppID = geocoderAppID;
    }


    /**
     * inner class to parse xml results *
     */

    private class XMLResult extends DefaultHandler {
        private boolean startLatitudeTag;
        private boolean startLongTag;

        private String latValue;
        private String longValue;

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("Latitude")) {
                startLatitudeTag = true;
            } else if (qName.equals("Longitude")) {
                startLongTag = true;
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("Latitude")) {
                startLatitudeTag = false;
            } else if (qName.equals("Longitude")) {
                startLongTag = false;
            }
        }

        public void characters(char ch[], int start, int length) throws SAXException {
            if (startLatitudeTag)
                latValue = new String(ch, start, length);
            else if (startLongTag)
                longValue = new String(ch, start, length);
        }
    }
}
