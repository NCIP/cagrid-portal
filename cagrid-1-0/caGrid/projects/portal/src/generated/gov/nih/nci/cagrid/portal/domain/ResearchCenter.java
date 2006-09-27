package gov.nih.nci.cagrid.portal.domain;

import java.util.HashSet;
import java.util.Set;


/**
 * @version 1.0
 * @hibernate.class table="RESEARCH_CENTER"
 * @created 19-Jun-2006 4:08:50 PM
 */
public class ResearchCenter implements DomainObject {
    private java.lang.String country;
    private java.lang.String description;
    private java.lang.String displayName;
    private java.lang.Float latitude;
    private java.lang.Float longitude;
    private java.lang.String homepageURL;
    private java.lang.String imageURL;
    private java.lang.String locality;
    private java.lang.String postalCode;
    private Integer pk;
    private java.lang.String street1;
    private java.lang.String street2;
    private java.lang.String state;
    private java.lang.String shortName;
    private java.util.Set pocCollection = new HashSet();
    private java.lang.String rssNewsURL;
    private RegisteredService registeredService;

    public ResearchCenter() {
    }

    /**
     * @hibernate.id generator-class="foreign" column="ID_KEY"
     * @hibernate.generator-param name="property" value="registeredService"
     */
    public Integer getPk() {
        return pk;
    }

    /**
     * @return
     * @hibernate.property column="COUNTRY"
     */
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return
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
     * @return
     * @hibernate.property column="LOCALITY"
     */
    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    /**
     * @return
     * @hibernate.property column="POSTAL_CODE"
     */
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return
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
     * @hibernate.property column="STATE"
     */
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    /**
     * @return
     * @hibernate.property column="DESCRIPTION"
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * @return
     * @hibernate.property column="DISPLAY_NAME"
     */
    public java.lang.String getDisplayName() {
        return displayName;
    }

    /**
     * @return
     * @hibernate.property column="HOMEPAGE_URL"
     */
    public java.lang.String getHomepageURL() {
        return homepageURL;
    }

    /**
     * @return
     * @hibernate.property column="IMAGE_URL"
     */
    public java.lang.String getImageURL() {
        return imageURL;
    }

    /**
     * @return
     * @hibernate.property column="SHORT_NAME"
     */
    public java.lang.String getShortName() {
        return shortName;
    }

    /**
     * @return
     * @hibernate.property column="RSS_NEWS_URL"
     */
    public String getRssNewsURL() {
        return rssNewsURL;
    }


    /**
     * @param newVal
     */
    public void setDescription(java.lang.String newVal) {
        description = newVal;
    }

    /**
     * @param newVal
     */
    public void setDisplayName(java.lang.String newVal) {
        displayName = newVal;
    }

    /**
     * @param newVal
     */
    public void setHomepageURL(java.lang.String newVal) {
        homepageURL = newVal;
    }

    /**
     * @param newVal
     */
    public void setImageURL(java.lang.String newVal) {
        imageURL = newVal;
    }

    /**
     * @param newVal
     */
    public void setShortName(java.lang.String newVal) {
        shortName = newVal;
    }

    public void setRssNewsURL(String rssNewsURL) {
        this.rssNewsURL = rssNewsURL;
    }

    /**
     * @hibernate.set cascade="all-delete-orphan"
     * inverse="false"
     * @hibernate.collection-key column="RC_ID_KEY"
     * @hibernate.collection-one-to-many class="gov.nih.nci.cagrid.portal.domain.PointOfContact"
     */
    public Set getPocCollection() {
        return pocCollection;
    }

    public void setPocCollection(Set pocCollection) {
        this.pocCollection = pocCollection;
    }

    /**
     * method to add Point Of Contact to
     * the service
     *
     * @param poc
     */
    public void addPOC(PointOfContact poc) {
        this.getPocCollection().add(poc);
        poc.setResearchCenter(this);
    }

    /**
     * @hibernate.one-to-one name="registeredService"
     * class="gov.nih.nci.cagrid.portal.domain.RegisteredService"
     * constrained="true"
     */
    public RegisteredService getRegisteredService() {
        return registeredService;
    }

    public void setRegisteredService(RegisteredService registeredService) {
        this.registeredService = registeredService;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ResearchCenter that = (ResearchCenter) o;

        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
        if (homepageURL != null ? !homepageURL.equals(that.homepageURL) : that.homepageURL != null) return false;
        if (imageURL != null ? !imageURL.equals(that.imageURL) : that.imageURL != null) return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null) return false;
        if (locality != null ? !locality.equals(that.locality) : that.locality != null) return false;
        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null) return false;
        if (!pk.equals(that.pk)) return false;
        if (pocCollection != null ? !pocCollection.equals(that.pocCollection) : that.pocCollection != null)
            return false;
        if (postalCode != null ? !postalCode.equals(that.postalCode) : that.postalCode != null) return false;
        if (rssNewsURL != null ? !rssNewsURL.equals(that.rssNewsURL) : that.rssNewsURL != null) return false;
        if (shortName != null ? !shortName.equals(that.shortName) : that.shortName != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (street1 != null ? !street1.equals(that.street1) : that.street1 != null) return false;
        if (street2 != null ? !street2.equals(that.street2) : that.street2 != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (country != null ? country.hashCode() : 0);
        result = 29 * result + (description != null ? description.hashCode() : 0);
        result = 29 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 29 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 29 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 29 * result + (homepageURL != null ? homepageURL.hashCode() : 0);
        result = 29 * result + (imageURL != null ? imageURL.hashCode() : 0);
        result = 29 * result + (locality != null ? locality.hashCode() : 0);
        result = 29 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 29 * result + pk.hashCode();
        result = 29 * result + (street1 != null ? street1.hashCode() : 0);
        result = 29 * result + (street2 != null ? street2.hashCode() : 0);
        result = 29 * result + (state != null ? state.hashCode() : 0);
        result = 29 * result + (shortName != null ? shortName.hashCode() : 0);
        result = 29 * result + (pocCollection != null ? pocCollection.hashCode() : 0);
        result = 29 * result + (rssNewsURL != null ? rssNewsURL.hashCode() : 0);
        return result;
    }
}
