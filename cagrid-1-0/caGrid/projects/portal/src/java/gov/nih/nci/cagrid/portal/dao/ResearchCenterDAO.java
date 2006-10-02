package gov.nih.nci.cagrid.portal.dao;

import org.springframework.dao.DataAccessException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 7, 2006
 * Time: 11:47:10 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ResearchCenterDAO extends BaseDAO {


    public java.util.List getUniqueCenters() throws DataAccessException;

    /**
     * Will do a free text search
     *
     * @param keyword
     * @return List of ResearchCenter objects
     */
    public java.util.List keywordSearch(String keyword);

}
