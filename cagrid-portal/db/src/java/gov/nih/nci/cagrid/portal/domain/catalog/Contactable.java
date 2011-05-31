package gov.nih.nci.cagrid.portal.domain.catalog;

import java.net.URL;

public interface Contactable extends Geolocatable {

    public String getEmailAddress();

    public URL getWebSite();

    public boolean isEmailAddressPublic();

    public String getPhoneNumber();

    public boolean isPhoneNumberPublic();

    public boolean isAddressPublic();
}