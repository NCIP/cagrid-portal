package gov.nih.nci.cagrid.portal.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents cancer sites that belong
 * to caBIG workspaces
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 16, 2006
 * Time: 4:47:37 PM
 * To change this template use File | Settings | File Templates.
 *
 * @hibernate.class table="CABIG_PARTICIPANTS"
 */
public class CaBIGParticipant implements GeocodedDomainObject {

    private Integer pk;
    private String name;
    private String institute;
    private String homepageURL;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String email;
    private String phoneNumber;
    private String status;
    private Float latitude;
    private Float longitude;
    private Set workspaceCollection = new HashSet();

    /**
     * @hibernate.id generator-class="increment"
     * column="ID_KEY"
     */
    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    /**
     * @hibernate.property column="PARTICIPANT_NAME"
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @hibernate.property column="INSTITUTION_NAME"
     */
    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    /**
     * @hibernate.property column="HOMPAGE_URL"
     */

    public String getHomepageURL() {
        return homepageURL;
    }

    public void setHomepageURL(String homepageURL) {
        this.homepageURL = homepageURL;
    }

    /**
     * @hibernate.property column="STREET1"
     */
    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    /**
     * @hibernate.property column="STREET2"
     */
    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    /**
     * @hibernate.property column="CITY"
     */
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @hibernate.property column="STATE"
     */
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    /**
     * @hibernate.property column="COUNTRY"
     */
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @hibernate.property column="POSTAL_CODE"
     */

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @hibernate.property column="EMAIL"
     */

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @hibernate.property column="STATUS"
     */

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @hibernate.property column="LATITUDE"
     */
    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    /**
     * @hibernate.property column="LONGITUDE"
     */
    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    /**
     * @hibernate.property column="PHONE_NUMBER"
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @hibernate.set name="workspaceCollection"
     * table="CABIG_WORKSPACES_PARTICIPANTS_JOIN"
     * cascade="none"
     * @hibernate.collection-key column="CABIG_PARTICIPANTS_ID_KEY"
     * @hibernate.collection-many-to-many column="CABIG_WORKSPACES_ID_KEY"
     * class="gov.nih.nci.cagrid.portal.domain.CaBIGWorkspace"
     */
    public Set getWorkspaceCollection() {
        return workspaceCollection;
    }


    public void setWorkspaceCollection(Set workspaceCollection) {
        this.workspaceCollection = workspaceCollection;
    }
}
