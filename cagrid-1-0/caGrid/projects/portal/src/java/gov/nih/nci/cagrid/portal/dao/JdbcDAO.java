package gov.nih.nci.cagrid.portal.dao;

import org.springframework.dao.DataAccessException;

import java.util.Map;

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

    /**
     * Will run SQL and return a row back from DB.
     * Each column in the row is mapped to a key-value
     * pair in the map. Column name is the key
     *
     * @param sql
     * @return
     * @throws DataAccessException
     */
    Map getRowForSQL(String sql) throws DataAccessException;


}
