package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.common.GeoCodeValues;
import gov.nih.nci.cagrid.portal.domain.GeocodedDomainObject;
import gov.nih.nci.cagrid.portal.exception.GeoCoderRetreivalException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 25, 2006
 * Time: 11:42:14 AM
 * To change this template use File | Settings | File Templates.
 */
public interface DomainObjectGeocoder {
    GeoCodeValues geocodeDomainObject(GeocodedDomainObject obj) throws GeoCoderRetreivalException;
}
