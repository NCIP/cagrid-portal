package gov.nih.nci.cagrid.portal.dao;

import org.springframework.dao.DataAccessException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 29, 2006
 * Time: 4:27:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface JdbcDAO {

    /**
     * Execute a DDL statement that
     * returns no resultset
     *
     * @param sql
     * @throws DataAccessException
     */
    void executeUpdate(String sql) throws DataAccessException;

    java.util.List sqlQueryForList(String sql, Class cls) throws DataAccessException;
}
