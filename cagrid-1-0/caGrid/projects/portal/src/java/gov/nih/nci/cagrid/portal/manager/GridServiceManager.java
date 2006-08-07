package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.GridService;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 5:47:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GridServiceManager extends BaseManager {
    public void save(GridService obj);
}
