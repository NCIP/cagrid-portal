package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 28, 2006
 * Time: 5:24:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridServiceManagerImpl extends BaseManagerImpl
    implements GridServiceManager {
    /**
     * Override base implementation
     *
     * @param obj
     */
    public void save(GridService obj) {
        GridService service = (GridService) obj;

        try {
            Integer objectID = gridServiceBaseDAO.getID4EPR(service.getEPR());
            _logger.debug("Setting id for index:" + objectID);
            service.setPk(objectID);

            //if id is found then we need to attach it to session
            //indexDAO.merge(idx);
        } catch (RecordNotFoundException e) {
            // New object since id does not exist
            // Do nothing as this is not unexpected
            _logger.debug("Record not found for index:" + service.getEPR());
        }

        _logger.debug("Saving service " + obj.getEPR() + " into DB");
        gridServiceBaseDAO.saveOrUpdate(service);
    }

    /** Add or update a registered service
     * belonging to this index
     * @param idx
     * @param rService
     */
    public IndexService addRegisteredService(IndexService idx,
        RegisteredService rService) {
        rService.setIndex(idx);

        try {
            // Will first need to set the primary key if service already in DB
            Integer objectID = indexDAO.getID4EPR(rService.getEPR());
            System.out.println("Found existing id" + objectID);
            rService.setPk(objectID);

            //if id is found then we need to attach it to session
            System.out.println("Reattaching service " + rService.getEPR());
        } catch (RecordNotFoundException e) {
            //do nothing as this can happen
            System.out.println("Record Not found ");
        }

        indexDAO.saveOrUpdate(rService);

        return idx;
    }
}
