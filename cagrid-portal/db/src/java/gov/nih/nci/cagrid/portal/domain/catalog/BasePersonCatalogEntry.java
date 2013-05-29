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

import javax.persistence.MappedSuperclass;
import java.net.URL;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@MappedSuperclass
public class BasePersonCatalogEntry extends CatalogEntry implements Contactable {
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

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }


    //TODO: create UserType for this
    public URL getWebSite() {
        return webSite;
    }

    public void setWebSite(URL webSite) {
        this.webSite = webSite;
    }
}
