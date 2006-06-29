package gov.nih.nci.cagrid.portal.manager;

import java.util.List;

/**
 * BaseManager Interface that all Managers
 * will implement
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 6:13:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BaseManager {

    /**
     * Returns All the persistent
     * object for given class
     *
     * @return List
     */
    public List loadAll(Class cls);
}
