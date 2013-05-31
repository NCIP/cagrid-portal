/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.geocode;

import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.Geocode;
import gov.nih.nci.cagrid.portal.util.URLConstructor;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class YahooGeocoder implements Geocoder {

	private static final Log logger = LogFactory.getLog(YahooGeocoder.class);

	private String baseUrl;

	private String yahooAppId;

	private long timeout;

	/**
	 * 
	 */
	public YahooGeocoder() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.aggr.geocode.Geocoder#getGeocode(gov.nih.nci.cagrid.portal.domain.Address)
	 */
	public Geocode getGeocode(Address address) throws GeocodingException {
		Geocode geoCode = null;

		QueryThread t = new QueryThread(address, getBaseUrl(), getYahooAppId());
		t.start();
		try {
			t.join(getTimeout());
		} catch (InterruptedException ex) {
			throw new GeocodingException("query thread interrupted: "
					+ ex.getMessage(), ex);
		}
		if (t.getEx() != null) {
			throw new GeocodingException("remote call encountered error: "
					+ t.getEx().getMessage(), t.getEx());
		}
		if (!t.isFinished()) {
			throw new GeocodingException("remote call timed out");
		}

		geoCode = t.getGeocode();

		return geoCode;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getYahooAppId() {
		return yahooAppId;
	}

	public void setYahooAppId(String yahooAppId) {
		this.yahooAppId = yahooAppId;
	}

	private class QueryThread extends Thread {

		private Geocode geoCode;

		private Address address;

		private String url;

		private String appId;

		private Exception ex;

		private boolean finished;

		QueryThread(Address address, String url, String appId) {
			this.address = address;
			this.url = url;
			this.appId = appId;
		}

		public void run() {
			try {
				URLConstructor con = new URLConstructor(url.trim() + "?" + "appid="
						+ appId);
				con.add("street", address.getStreet1());
				con.add("city", address.getLocality());
				con.add("state", address.getStateProvince());
				con.add("zip", address.getPostalCode());

				URL urlObj = con.toURL();
				logger.debug("Querying: " + urlObj.toString());
				HttpURLConnection conn = (HttpURLConnection) (urlObj
						.openConnection());
				conn.setRequestMethod("GET");
				conn.setDoOutput(true);

				XMLResult handler = new XMLResult();
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();

				saxParser.parse(conn.getInputStream(), handler);

				geoCode = new Geocode();
				geoCode.setLatitude(new Float(handler.latValue));
				geoCode.setLongitude(new Float(handler.longValue));

				finished = true;

			} catch (Exception ex) {
				this.ex = ex;
			}
		}

		boolean isFinished() {
			return finished;
		}

		Geocode getGeocode() {
			return geoCode;
		}

		Exception getEx() {
			return ex;
		}
	}

	private class XMLResult extends DefaultHandler {
		private boolean startLatitudeTag;

		private boolean startLongTag;

		private String latValue;

		private String longValue;

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if (qName.equals("Latitude")) {
				startLatitudeTag = true;
			} else if (qName.equals("Longitude")) {
				startLongTag = true;
			}
		}

		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if (qName.equals("Latitude")) {
				startLatitudeTag = false;
			} else if (qName.equals("Longitude")) {
				startLongTag = false;
			}
		}

		public void characters(char ch[], int start, int length)
				throws SAXException {
			if (startLatitudeTag)
				latValue = new String(ch, start, length);
			else if (startLongTag)
				longValue = new String(ch, start, length);
		}
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
