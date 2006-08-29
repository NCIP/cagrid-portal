package gov.nih.nci.cagrid.portal.manager;

import org.springframework.dao.DataAccessException;


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

    public void save(Object obj) throws DataAccessException;

    public java.util.List loadAll(Class cls) throws DataAccessException;

}
