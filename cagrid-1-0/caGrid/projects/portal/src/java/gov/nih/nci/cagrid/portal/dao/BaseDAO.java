package gov.nih.nci.cagrid.portal.dao;

import org.springframework.dao.DataAccessException;

import java.util.Collection;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 5:58:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BaseDAO {

    public void saveOrUpdate(Collection objects) throws DataAccessException;

    public void saveOrUpdate(Object obj) throws DataAccessException;

    public java.util.List loadAll(Class cls) throws DataAccessException;

}
