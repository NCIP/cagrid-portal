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
package gov.nih.nci.cagrid.portal.domain.catalog;

import gov.nih.nci.cagrid.portal.domain.Participant;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("institution")

public class InstitutionCatalogEntry extends CommunityCatalogEntry implements Contactable {

    public String street1;

    public String street2;

    public String locality;

    public String stateProvince;

    public String postalCode;

    public String countryCode;

    public Float latitude;

    public Float longitude;

    public String emailAddress;

    public URL webSite;

    public String phoneNumber;

    public boolean emailAddressPublic;

    public boolean phoneNumberPublic;

    public boolean addressPublic;

    public Participant about;

    public List<KnowledgeCenterCatalogEntry> knowledgeCenterInstitutionOf = new ArrayList<KnowledgeCenterCatalogEntry>();

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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public URL getWebSite() {
        return webSite;
    }

    public void setWebSite(URL webSite) {
        this.webSite = webSite;
    }

    @Transient
    public void setWebSite(String webSite) throws MalformedURLException {
        setWebSite(new URL(webSite));
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    @ManyToMany(mappedBy = "knowledgeCenterInstitutions")
    public List<KnowledgeCenterCatalogEntry> getKnowledgeCenterInstitutionOf() {
        return knowledgeCenterInstitutionOf;
    }

    public void setKnowledgeCenterInstitutionOf(List<KnowledgeCenterCatalogEntry> knowledgeCenterInstitutionOf) {
        this.knowledgeCenterInstitutionOf = knowledgeCenterInstitutionOf;
    }

    @OneToOne
    @JoinColumn(name = "participant_id")
    public Participant getAbout() {
        return about;
    }

    public void setAbout(Participant about) {
        this.about = about;
    }
}