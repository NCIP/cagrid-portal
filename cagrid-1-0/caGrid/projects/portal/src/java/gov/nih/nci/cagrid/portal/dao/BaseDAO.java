package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.DomainObject;
import org.hibernate.HibernateException;
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

    public Object getObjectByPrimaryKey(Class cls, Integer id) throws DataAccessException;

    public void saveOrUpdate(Collection objects) throws DataAccessException;

    public void saveOrUpdate(DomainObject obj) throws DataAccessException;

    public java.util.List loadAll(Class cls) throws DataAccessException;

    /**
     * Remove a persistent instance from the datastore.
     * The argument may be an instance associated with the
     * receiving Session or a transient instance with an identifier
     * associated with existing persistent state.
     * This operation cascades to associated instances if the
     * association is mapped with cascade="delete".
     *
     * @param obj the instance to be removed
     * @throws HibernateException
     */
    public void delete(DomainObject obj) throws HibernateException;

    public void deleteAll(Collection objects) throws DataAccessException;

}
