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
public class IndexServiceManagerImpl extends BaseManagerImpl implements IndexServiceManager {

    private Integer getID4EPR(String epr) throws RecordNotFoundException {
           return indexDAO.getID4EPR(epr);
       }

    /**
     * Override base implementation
     *
     * @param obj
     */
    public void save(Object obj) {
        IndexService idx = (IndexService) obj;
        try {
            idx.setPk(getID4EPR(idx.getEpr()));
        } catch (RecordNotFoundException e) {
            // New object since id does not exist
            // Do nothing as this is not unexpected
        }

        baseDAO.saveOrUpdate(idx);
    }


    /** Add or update a registered service
     * belonging to this index
     * @param idx
     * @param service
     */
    public IndexService addRegisteredService(IndexService idx, RegisteredService service){
        return indexDAO.addRegisteredService(idx,service);

    }


}

