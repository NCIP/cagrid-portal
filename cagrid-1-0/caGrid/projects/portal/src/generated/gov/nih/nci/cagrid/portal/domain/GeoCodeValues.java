package gov.nih.nci.cagrid.portal.domain;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 7, 2006
 * Time: 11:49:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class GeoCodeValues {
    private Float latitude;
    private Float longitude;

    public GeoCodeValues() {
    }

    public GeoCodeValues(Float latitude, Float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
}
