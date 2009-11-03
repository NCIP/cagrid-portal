package gov.nih.nci.cagrid.portal.domain.catalog;

import java.net.URL;

public interface Contactable extends Geolocatable {

    public String getEmailAddress();

    public URL getWebSite();

    public Boolean isEmailAddressPublic();

    public String getPhoneNumber();

    public Boolean isPhoneNumberPublic();

    public Boolean isAddressPublic();
}