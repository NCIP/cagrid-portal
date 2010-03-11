package gov.nih.nci.cagrid.portal.domain.catalog;

public interface Geolocatable {

    public String getStreet1();

    public String getStreet2();

    public String getLocality();

    public String getPostalCode();

    public String getCountryCode();

    public Float getLatitude();

    public Float getLongitude();

    public String getStateProvince();
}