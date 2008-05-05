/**
 *
 */
package gov.nih.nci.cagrid.portal.bugfixes;

import gov.nih.nci.cagrid.portal.dao.AddressDao;
import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.Geocode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@Transactional
public class AddressMonitor implements InitializingBean {

    private AddressDao addressDao;

    private Geocoder geocoder;

    private static final Log logger = LogFactory.getLog(AddressMonitor.class);

    /**
     *
     */
    public AddressMonitor() {

    }


    public void afterPropertiesSet() throws Exception {
        checkAddresses();
    }

    public void checkAddresses() {
        for (Address address : getAddressDao().getAll()) {
            Geocode geocode = address.getGeocode();

            try {
                geocode = getGeocoder().getGeocode(address);
            } catch (Exception ex) {
                String msg = "Error geocoding: " + ex.getMessage();
                logger.warn(msg);
            }
            if (geocode == null) {
                logger.warn("Couldn't geocode address '" + address + "'");
            } else {
                address.setGeocode(geocode);
                getAddressDao().save(address);
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

}