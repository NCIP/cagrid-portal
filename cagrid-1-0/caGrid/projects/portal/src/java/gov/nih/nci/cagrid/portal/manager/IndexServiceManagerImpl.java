package gov.nih.nci.cagrid.portal.manager;

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
public class IndexServiceManagerImpl extends BaseManagerImpl
        implements IndexServiceManager {

    /**
     * Override base implementation
     *
     * @param obj
     */
    public void save(Object obj) {
        IndexService idx = (IndexService) obj;

        try {
             Integer objectID = indexDAO.getID4EPR(idx.getEpr());
            _logger.debug("Setting id for index:" + objectID);
            idx.setPk(objectID);

             //if id is found then we need to attach it to session
           //indexDAO.merge(idx);
        } catch (RecordNotFoundException e) {
            // New object since id does not exist
            // Do nothing as this is not unexpected
            _logger.debug("Record not found for index:" + idx.getEpr());
        }
        indexDAO.saveOrUpdate(idx);
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
            Integer objectID = indexDAO.getID4EPR(rService.getEpr());
            System.out.println("Found existing id" + objectID);
            rService.setPk(objectID);

           //if id is found then we need to attach it to session
            System.out.println("Reattaching service " + rService.getEpr());


        } catch (RecordNotFoundException e) {
            //do nothing as this can happen
            System.out.println("Record Not found ");
        }


       indexDAO.saveOrUpdate(rService);

        return idx;

    }
}
