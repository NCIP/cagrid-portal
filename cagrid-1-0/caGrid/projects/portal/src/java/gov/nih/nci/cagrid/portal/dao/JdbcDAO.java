package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.GeoCodeValues;
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

    /**
     * Will do a local lookup
     * for Geocodes based on a Zip code value
     *
     * @param zip
     * @return
     * @throws DataAccessException
     */
    public GeoCodeValues getGeoCodes(String zip) throws DataAccessException;

}
