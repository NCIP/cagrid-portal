/**
 * 
 */
package gov.nih.nci.cagrid.portal2.aggr.geocode;

import gov.nih.nci.cagrid.portal2.dao.AddressDao;
import gov.nih.nci.cagrid.portal2.domain.Address;
import gov.nih.nci.cagrid.portal2.domain.Geocode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class AddressMonitor {

	private AddressDao addressDao;

	private Geocoder geocoder;

	private long timeout;
	
	private static final Log logger = LogFactory.getLog(AddressMonitor.class);

	/**
	 * 
	 */
	public AddressMonitor() {

	}

	public void checkAddresses() {
		for (Address address : getAddressDao().getAll()) {
			Geocode geocode = address.getGeocode();
			if (geocode == null) {
				try {
					geocode = getGeocoder().getGeocode(address);
				} catch (Exception ex) {
					String msg = "Error geocoding: " + ex.getMessage();
					logger.warn(msg);
				}
				if(geocode == null){
					logger.warn("Couldn't geocode address '" + address + "'");
				}else{
					address.setGeocode(geocode);
					getAddressDao().save(address);
				}
			}
		}
	}

	public AddressDao getAddressDao() {
		return addressDao;
	}

	public void setAddressDao(AddressDao addressDao) {
		this.addressDao = addressDao;
	}

	public Geocoder getGeocoder() {
		return geocoder;
	}

	public void setGeocoder(Geocoder geocoder) {
		this.geocoder = geocoder;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
