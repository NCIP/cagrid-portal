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
/**
 *
 */
package gov.nih.nci.cagrid.portal.aggr.geocode;

import gov.nih.nci.cagrid.portal.dao.AddressDao;
import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.Geocode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
@Transactional
public class AddressMonitor {

    private AddressDao addressDao;

    private Geocoder geocoder;

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
                    logger.debug(msg);
                }
                if (geocode == null) {
                    logger.warn("Couldn't geocode address '" + address + "'");
                } else {
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

}
