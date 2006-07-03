package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.IndexService;
import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 5:47:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IndexServiceManager extends BaseManager {
    IndexService findIndexServiceByPK(Integer key);

    IndexService findIndexServiceByEPR(EndpointReferenceType epr);


}
