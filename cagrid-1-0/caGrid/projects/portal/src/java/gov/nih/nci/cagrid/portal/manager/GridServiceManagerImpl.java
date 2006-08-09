package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.GeoCoderRetreivalException;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;
import gov.nih.nci.cagrid.portal.utils.GeoCoderUtility;


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
     *
     * @param obj
     */
    public void save(GridService obj) {
        GridService service = (GridService) obj;

        try {
            Integer objectID = gridServiceBaseDAO.getBusinessKey(service);
            _logger.debug("Setting id for index:" + objectID);
            service.setPk(objectID);

            //if id is found then we need to attach it to session
            //indexDAO.merge(idx);
        } catch (RecordNotFoundException e) {
            // New object since id does not exist
            // Do nothing as this is not unexpected
            _logger.debug("Record not found for service:" + service.getEPR() + " . Hibernate will assign new id.");
        }

        _logger.debug("Saving service " + obj.getEPR() + " into DB");
        super.save(service);
    }

    /**
         * Manages storing of a Research Center
         * @param rc
         */
        public void save(ResearchCenter rc){
            //check if geo Co-ords have been set
            if(rc.getGeoCoords() == null)       {
                try {
                    GeoCoderUtility coder = new GeoCoderUtility();
                    coder.getGeoCode4RC(rc);
                } catch (GeoCoderRetreivalException e) {
                    //already logged just bypass the exception
                    rc.setGeoCoords("N/A");
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
