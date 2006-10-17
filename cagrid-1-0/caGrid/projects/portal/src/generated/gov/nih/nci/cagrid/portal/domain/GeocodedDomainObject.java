package gov.nih.nci.cagrid.portal.domain;

/**
 * Represents a Geocoded entity
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 17, 2006
 * Time: 11:27:32 AM
 * To change this template use File | Settings | File Templates.
 */
public interface GeocodedDomainObject extends DomainObject {

    String getCountry();

    Float getLatitude();

    void setLatitude(Float latitude);

    Float getLongitude();

    void setLongitude(Float longitude);

    String getPostalCode();

    String getStreet1();

    String getStreet2();

    String getState();


}
