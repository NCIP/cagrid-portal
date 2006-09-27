package gov.nih.nci.cagrid.portal.dao.hibernate;

import gov.nih.nci.cagrid.portal.dao.ResearchCenterDAO;
import gov.nih.nci.cagrid.portal.domain.GeoCodeValues;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 7, 2006
 * Time: 11:27:05 AM
 * To change this template use File | Settings | File Templates.
 */
public final class ResearchCenterDAOImpl extends BaseDAOImpl
        implements ResearchCenterDAO {

    private javax.sql.DataSource dataSource;

    public GeoCodeValues getGeoCodes(String zip) throws DataAccessException {
        String latLongSQL = "Select LATITUDE, LONGITUDE from ZIPCODES_GEOCODES where ZIP = '" + zip + "'";

        JdbcTemplateSupport jdbc = new JdbcTemplateSupport(dataSource);
        GeoCodeValues values = (GeoCodeValues) jdbc.getJdbcTemplate().query(latLongSQL, new GeoCodeValuesResultExtractor());
        return values;
    }

    public List getUniqueCenters() throws DataAccessException {
        return getHibernateTemplate().find("from ResearchCenter rc group by rc.latitude");
    }

    //setter for Spring
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * Class provides JDBC support to the ResearchCenterDAO
     * <p/>
     * Implements no method but will provide access to HibernateTemplate
     * to
     */
    private class JdbcTemplateSupport extends JdbcDaoSupport {

        public JdbcTemplateSupport(javax.sql.DataSource ds) {
            super.setDataSource(ds);
        }

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
