package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.GeoCodeValues;
import gov.nih.nci.cagrid.portal.domain.GeocodedDomainObject;
import gov.nih.nci.cagrid.portal.exception.GeoCoderRetreivalException;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.utils.GeoCoderUtility;
import org.springframework.dao.DataAccessException;

/**
 * Base manager that handles geocoding
 * Is declared abstract as it is not to be used
 * directly
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 17, 2006
 * Time: 11:37:51 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GeocodingBaseManagerImpl extends BaseManagerImpl {

    /**
     * Will put required geocoding information
     * information into an GeocodedDomainObject
     * <p/>
     * Will put Geocoding information
     * into the Research Center
     *
     * @param obj
     */
    protected void geocodeDomainObject(GeocodedDomainObject obj) throws PortalRuntimeException {
        //check if geo Co-ords have been set
        if (obj.getLatitude() == null) {
            try {
                //Try the external geocoding service first
                GeoCoderUtility coder = new GeoCoderUtility();
                GeoCodeValues result = GeoCoderUtility.geocodeDomainObject(obj);
                obj.setLatitude(result.getLatitude());
                obj.setLongitude(result.getLongitude());
            } catch (GeoCoderRetreivalException e) {
                _logger.warn("Could not reach Geocoding web service. Doing local lookup");
                try {
                    //Do a local lookup
                    GeoCodeValues result = jdbcDAO.getGeoCodes(obj.getPostalCode());
                    obj.setLatitude(result.getLatitude());
                    obj.setLongitude(result.getLongitude());
                } catch (DataAccessException e1) {
                    throw new PortalRuntimeException("Error doing local Zip code lookup.", e);
                }
            }
        }
    }
}
