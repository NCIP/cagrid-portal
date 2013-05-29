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
package gov.nih.nci.cagrid.portal.domain;

import gov.nih.nci.cagrid.portal.util.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "addresses")
@GenericGenerator(name="id-generator", strategy = "native",
    parameters = {
        @Parameter(name="sequence", value="seq_addresses")
    }
)
public class Address extends AbstractDomainObject {
	
	private String street1;
	private String street2;
	private String postalCode;
	private String locality;
	private String stateProvince;
	private String country;
	private Float latitude;
	private Float longitude;

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Float getLatitude() {
		return latitude;
	}
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public Float getLongitude() {
		return longitude;
	}
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	
	public String getPostalCode() {
		return StringUtils.parsePostalCode(postalCode);
	}

    public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	

	public String getStateProvince() {
		return stateProvince;
	}
	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}
	

	public String getStreet1() {
		return street1;
	}
	public void setStreet1(String street1) {
		this.street1 = street1;
	}
	

	public String getStreet2() {
		return street2;
	}
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	
	@Transient
	public Geocode getGeocode(){
		Geocode geocode = null;
		if(getLatitude() != null && getLongitude() != null){
			geocode = new Geocode(getLatitude(), getLongitude());
		}
		return geocode;
	}
	
	public void setGeocode(Geocode geocode){
		setLatitude(geocode.getLatitude());
		setLongitude(geocode.getLongitude());
	}


}
