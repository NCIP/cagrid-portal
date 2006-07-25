package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;
import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 28, 2006
 * Time: 5:24:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexServiceManagerImpl extends BaseManagerImpl implements IndexServiceManager {

    public IndexService findIndexServiceByPK(final Integer key) {
        return (IndexService) indexDAO.getObjectByPK(key);
    }

    public IndexService findIndexServiceByEPR(EndpointReferenceType epr) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.

    }

    /**
     * Override base implementation
     *
     * @param obj
     */
    public void save(Object obj) {
        IndexService idx = (IndexService) obj;
        try {
            int id = indexDAO.getID4EPR(idx.getEpr());
            idx.setKey(id);
        } catch (RecordNotFoundException e) {
            // New object since id does not exist
            // Do nothing as this is not unexpected
        }

        baseDAO.saveOrUpdate(idx);
    }
}

