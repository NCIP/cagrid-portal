package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.GeoCodeValues;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.GeoCoderRetreivalException;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;
import gov.nih.nci.cagrid.portal.utils.GeoCoderUtility;
import org.springframework.dao.DataAccessException;

import java.util.List;


/**
 * Service layer. Will log errors
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 28, 2006
 * Time: 5:24:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridServiceManagerImpl extends BaseManagerImpl
        implements GridServiceManager {

    public GridServiceManagerImpl() {
        //for spring
    }

    /**
     * @return
     * @throws PortalRuntimeException
     * @see GridServiceManager#getAllServices()
     */
    public List getAllServices() throws PortalRuntimeException {
        try {
            return super.loadAll(RegisteredService.class);
        } catch (DataAccessException e) {
            throw new PortalRuntimeException(e);
        }
    }


    /**
     * @param rService
     * @throws PortalRuntimeException
     * @see GridServiceManager#save(gov.nih.nci.cagrid.portal.domain.RegisteredService)
     */
    public synchronized void save(RegisteredService rService) throws PortalRuntimeException {

        try {

            try {
                Integer objectID = gridServiceBaseDAO.getSurrogateKey(rService);

                //if service exists then flush the Domain Model and Operations
                RegisteredService rServiceOld = (RegisteredService) gridServiceBaseDAO.getObjectByPrimaryKey(RegisteredService.class, objectID);
                gridServiceBaseDAO.delete(rServiceOld);

            } catch (RecordNotFoundException e) {
                // Do nothing as this is not unexpected
            }

            if (rService.getResearchCenter() != null)
                prepCenter(rService.getResearchCenter());

            //save the new service. ORM will create a new ID
            rService.setPk(null);
            baseDAO.saveOrUpdate(rService);

        } catch (Exception e) {
            //log exception and rethrow
            _logger.error("Problem saving Registered Service", e);
            throw new PortalRuntimeException(e);
        }
    }


    /**
     * Will put required
     * information into an RC object
     * <p/>
     * Will put Geocoding information
     * into the Research Center
     *
     * @param rc
     */
    private void prepCenter(ResearchCenter rc) throws PortalRuntimeException {
        //check if geo Co-ords have been set
        if (rc.getLatitude() == null) {
            try {
                //Try the external geocoding service first
                GeoCoderUtility coder = new GeoCoderUtility();
                GeoCodeValues result = coder.getGeoCode4RC(rc);
                rc.setLatitude(result.getLatitude());
                rc.setLongitude(result.getLongitude());
            } catch (GeoCoderRetreivalException e) {
                _logger.warn("Could not reach Geocoding web service. Doing local lookup");
                try {
                    //Do a local lookup
                    GeoCodeValues result = jdbcDAO.getGeoCodes(rc.getPostalCode());
                    rc.setLatitude(result.getLatitude());
                    rc.setLongitude(result.getLongitude());
                } catch (DataAccessException e1) {
                    throw new PortalRuntimeException("Error doing local Zip code lookup.", e);
                }
            }
        }
    }
}
