/**
 *
 */
package gov.nih.nci.cagrid.portal.bugfixes;

import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.Geocode;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public interface Geocoder {
    Geocode getGeocode(Address address) throws GeocodingException;
}