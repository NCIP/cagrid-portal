/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class MapNode {
	
	private String latitude;
	private String longitude;

	/**
	 * 
	 */
	public MapNode() {

	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
