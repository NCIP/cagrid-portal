/**
 * 
 */
package gov.nih.nci.cagrid.portal2.aggr.geocode;

import gov.nih.nci.cagrid.portal2.domain.Address;
import gov.nih.nci.cagrid.portal2.domain.Geocode;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface Geocoder {
	Geocode getGeocode(Address address) throws GeocodingException; 
}
