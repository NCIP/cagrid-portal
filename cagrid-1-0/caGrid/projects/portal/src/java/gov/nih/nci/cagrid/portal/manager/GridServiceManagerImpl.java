package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.*;
import gov.nih.nci.cagrid.portal.exception.GeoCoderRetreivalException;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;
import gov.nih.nci.cagrid.portal.utils.GeoCoderUtility;
import org.springframework.dao.DataAccessException;

import java.util.Iterator;
import java.util.List;


/**
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


    public List getAllServices() throws DataAccessException {
        return super.loadAll(RegisteredService.class);
    }

    /**
     * All other domain objects are saved this way
     */
    public void save(DomainObject obj) throws DataAccessException {
        try {
            Integer objectID = gridServiceBaseDAO.getBusinessKey(obj);
            obj.setPk(objectID);
        } catch (RecordNotFoundException e) {
            // New object since id does not exist
            // Do nothing as this is not unexpected
            _logger.info("Record not found for domain object. Creating new one");
        }
        super.save(obj);
    }


    public void save(RegisteredService rService) throws DataAccessException {
        // save service
        _logger.debug("Saving registered service " + rService.getEPR());
        try {
            Integer objectID = gridServiceBaseDAO.getBusinessKey(rService);
            rService.setPk(objectID);
        } catch (RecordNotFoundException e) {
            // New object since id does not exist
            // Do nothing as this is not unexpected
            _logger.info("Record not found for domain object. Creating new one");
        }

        //save domain model which is a child 1:1 association
        DomainModel dModel = rService.getDomainModel();
        if (dModel != null) {
            dModel.setRegisteredService(rService);
            dModel.setPk(rService.getPk());
        }

        _logger.info("Saving Registered Service:" + rService.getEPR());
        super.save(rService);
    }

    /**
     * Manages storing of a Research Center
     *
     * @param rc
     */
    public void save(ResearchCenter rc) throws DataAccessException {
        //check if geo Co-ords have been set
        if (rc.getLatitude() == null) {
            try {
                GeoCoderUtility coder = new GeoCoderUtility();
                GeoCodeValues result = coder.getGeoCode4RC(rc);
                rc.setLatitude(result.getLatitude());
                rc.setLongitude(result.getLongitude());
            } catch (GeoCoderRetreivalException e) {
                _logger.info("Could not reach Geocoding web service. Doing local lookup");
                GeoCodeValues result = rcDAO.getGeoCodes(rc.getPostalCode());
                rc.setLatitude(result.getLatitude());
                rc.setLongitude(result.getLongitude());
            }
        }

        /** save POC's **/
        if (!rc.getPocCollection().isEmpty()) {
            for (Iterator pocIter = rc.getPocCollection().iterator(); pocIter.hasNext();) {
                PointOfContact poc = (PointOfContact) pocIter.next();
                _logger.debug("Saving POC");
                save(poc);
            }
        }

        _logger.debug("Saving RC with " + rc.getPocCollection().size() + " POC's");
        save((DomainObject) rc);
    }

}
