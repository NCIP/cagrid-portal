package gov.nih.nci.cagrid.portal.dao.jdbc;

import gov.nih.nci.cagrid.portal.dao.JdbcDAO;
import gov.nih.nci.cagrid.portal.domain.GeoCodeValues;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public GeoCodeValues getGeoCodes(String zip) throws DataAccessException {
        String latLongSQL = "Select LATITUDE, LONGITUDE from ZIPCODES_GEOCODES where ZIP = '" + zip + "'";
        GeoCodeValues values = (GeoCodeValues) getJdbcTemplate().query(latLongSQL, new GeoCodeValuesResultExtractor());
        return values;
    }

    private class GeoCodeValuesResultExtractor implements ResultSetExtractor {
        public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            //should only be one column
            resultSet.first();
            GeoCodeValues obj = new GeoCodeValues(new Float(resultSet.getFloat(1)), new Float(resultSet.getFloat(2)));
            return obj;
        }
    }

}
