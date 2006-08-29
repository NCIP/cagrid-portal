package gov.nih.nci.cagrid.portal.dao.jdbc;

import gov.nih.nci.cagrid.portal.dao.JdbcDAO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 29, 2006
 * Time: 4:16:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class JdbcDAOImpl extends JdbcDaoSupport implements JdbcDAO {

    public void executeUpdate(String sql) throws DataAccessException {
        getJdbcTemplate().update(sql);
    }

    public java.util.List sqlQueryForList(String sql, Class cls) throws DataAccessException {
        return getJdbcTemplate().queryForList(sql, cls);
    }

}
