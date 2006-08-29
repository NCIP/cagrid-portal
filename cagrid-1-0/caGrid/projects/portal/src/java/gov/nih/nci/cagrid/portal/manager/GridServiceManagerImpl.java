package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.DomainModel;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.GeoCoderRetreivalException;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;
import gov.nih.nci.cagrid.portal.utils.GeoCoderUtility;
import org.springframework.dao.DataAccessException;

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

    /**
     * Override base implementation
     */
    public void save(IndexService idx) throws DataAccessException {
        try {
            Integer objectID = gridServiceBaseDAO.getBusinessKey(idx);
            _logger.debug("Setting id for index:" + objectID);
            idx.setPk(objectID);

            //if id is found then we need to attach it to session
            //indexDAO.merge(idx);
        } catch (RecordNotFoundException e) {
            // New object since id does not exist
            // Do nothing as this is not unexpected
        }
        super.save(idx);
    }


    public void save(RegisteredService rService) throws DataAccessException {

        try {
            Integer objectID = gridServiceBaseDAO.getBusinessKey(rService);
            rService.setPk(objectID);
        } catch (RecordNotFoundException e) {
            //do nothing
        }

        //save the research center
        if (rService.getResearchCenter() != null)
            save(rService.getResearchCenter());

        //save service to assign ID
        super.save(rService);

        //save domain model
        DomainModel dModel = rService.getDomainModel();
        if (dModel != null) {
            dModel.setPk(rService.getPk());
            super.save(dModel);
        }
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
                GeoCoderUtility.GeocodeResult result = coder.getGeoCode4RC(rc);
                rc.setLatitude(result.getLatitude());
                rc.setLongitude(result.getLongitude());
            } catch (GeoCoderRetreivalException e) {
                _logger.info("Could not reach Geocoding web service. Doing local lookup");

                String latLongSQL = "Select LATITUDE, LONGITUDE from ZIPCODES_GEOCODES where ZIP = '" + rc.getPostalCode() + "'";
                List geoCodes = jdbcDAO.sqlQueryForList(latLongSQL, java.lang.Float.class);

                if (!geoCodes.isEmpty()) {
                    rc.setLatitude((java.lang.Float) geoCodes.get(0));
                    rc.setLongitude((java.lang.Float) geoCodes.get(1));
                }
            }
        }

        try {
            Integer objectID = gridServiceBaseDAO.getBusinessKey(rc);
            rc.setPk(objectID);
        } catch (RecordNotFoundException e) {
            _logger.debug("Record not found for Research  Center. Hibernate will assign new id");
        }
        super.save(rc);
    }


}
