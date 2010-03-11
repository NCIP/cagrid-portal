/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class Geocode {

	private Float latitude;
	private Float longitude;
	
	/**
	 * 
	 */
	public Geocode() {

	}
	
	public Geocode(Float latitude, Float longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}

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
