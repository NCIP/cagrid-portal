package gov.nih.nci.cagrid.portal.domain.catalog;

import gov.nih.nci.cagrid.portal.domain.PortalUser;

import javax.persistence.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("person")

public class PersonCatalogEntry extends CatalogEntry implements Contactable {

    public String salutation;

    public String firstName;

    public String middleName;

    public String lastName;

    public String suffix;

    public String street1;

    public String street2;

    public String locality;

    public String stateProvince;

    public String postalCode;

    public String countryCode;

    public Float latitude;

    public Float longitude;

    public boolean emailAddressPublic;

    public boolean phoneNumberPublic;

    public boolean addressPublic;

    public String phoneNumber;

    public String emailAddress;

    public URL webSite;

    public PortalUser about;

    public List<Rating> contributorRatings = new ArrayList<Rating>();

    public List<Comment> commentsOf = new ArrayList<Comment>();

    public List<CatalogEntry> contributions = new ArrayList<CatalogEntry>();

    public List<FavoriteRole> favoriteRole = new ArrayList<FavoriteRole>();

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    public boolean isEmailAddressPublic() {
        return emailAddressPublic;
    }

    public void setEmailAddressPublic(boolean emailAddressPublic) {
        this.emailAddressPublic = emailAddressPublic;
    }

    public boolean isPhoneNumberPublic() {
        return phoneNumberPublic;
    }

    public void setPhoneNumberPublic(boolean phoneNumberPublic) {
        this.phoneNumberPublic = phoneNumberPublic;
    }

    public boolean isAddressPublic() {
        return addressPublic;
    }

    public void setAddressPublic(boolean addressPublic) {
        this.addressPublic = addressPublic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    @OneToOne
    @JoinColumn(name = "person_id")
    public PortalUser getAbout() {
        return about;
    }

    public void setAbout(PortalUser about) {
        this.about = about;
    }

    @OneToMany(mappedBy = "ratingContributor", cascade = CascadeType.ALL)
    public List<Rating> getContributorRatings() {
        return contributorRatings;
    }

    public void setContributorRatings(List<Rating> contributorRatings) {
        this.contributorRatings = contributorRatings;
    }

    // Will delete comments when user is deleted
    @OneToMany(mappedBy = "commentor", cascade = CascadeType.ALL)
    public List<Comment> getCommentsOf() {
        return commentsOf;
    }

    public void setCommentsOf(List<Comment> commentsOf) {
        this.commentsOf = commentsOf;
    }

    @OneToMany(mappedBy = "contributor")
    public List<CatalogEntry> getContributions() {
        return contributions;
    }

    public void setContributions(List<CatalogEntry> contributions) {
        this.contributions = contributions;
    }

    @OneToMany(mappedBy = "favorite")
    public List<FavoriteRole> getFavoriteRole() {
        return favoriteRole;
    }

    public void setFavoriteRole(List<FavoriteRole> favoriteRole) {
        this.favoriteRole = favoriteRole;
    }

    //TODO: create UserType for this
    public URL getWebSite() {
        return webSite;
    }

    public void setWebSite(URL webSite) {
        this.webSite = webSite;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }
}