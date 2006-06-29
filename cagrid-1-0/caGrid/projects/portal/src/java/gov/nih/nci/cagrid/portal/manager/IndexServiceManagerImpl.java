package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.IndexService;
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


}
