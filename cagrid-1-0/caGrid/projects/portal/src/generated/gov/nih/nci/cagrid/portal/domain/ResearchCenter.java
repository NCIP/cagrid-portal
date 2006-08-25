package gov.nih.nci.cagrid.portal.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * @version 1.0
 * @hibernate.class table="RESEARCH_CENTER"
 * @created 19-Jun-2006 4:08:50 PM
 */
public class ResearchCenter implements DomainObject {
    private java.lang.String country;
    private java.lang.String description;
    private java.lang.String displayName;
    private java.lang.String geoCoords;
    private java.lang.String homepageURL;
    private java.lang.String imageURL;
    private java.lang.String locality;
    private java.lang.String postalCode;
    private Integer pk;
    private java.lang.String street1;
    private java.lang.String street2;
    private java.lang.String state;
    private java.lang.String shortName;
    private java.util.List pocCollection = new ArrayList();
    private java.lang.String rssNewsURL;

    public ResearchCenter() {
    }

    /**
     * @return
     * @hibernate.id column="ID_KEY"
     * generator-class="increment"
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
     * @hibernate.property column="GEO_CORDINATES"
     */
    public String getGeoCoords() {
        return geoCoords;
    }

    public void setGeoCoords(String geoCoords) {
        this.geoCoords = geoCoords;
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
     * @hibernate.collection-one-to-many column="RC_ID_KEY"
     * class="PointOfContact"
     * cascade="save-update"
     * inverse="true"
     */
    public List getPocCollection() {
        return pocCollection;
    }

    public void setPocCollection(List pocCollection) {
        this.pocCollection = pocCollection;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        final ResearchCenter that = (ResearchCenter) o;

        if (!geoCoords.equals(that.geoCoords)) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return geoCoords.hashCode();
    }
}
