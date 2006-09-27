package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.GeoCodeValues;
import org.springframework.dao.DataAccessException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 7, 2006
 * Time: 11:47:10 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ResearchCenterDAO extends BaseDAO {


    /**
     * Will do a local lookup
     * for Geocodes based on a Zip code value
     *
     * @param zip
     * @return
     * @throws DataAccessException
     */
    public GeoCodeValues getGeoCodes(String zip) throws DataAccessException;

    public java.util.List getUniqueCenters() throws DataAccessException;


}
