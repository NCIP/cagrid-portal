package gov.nih.nci.cagrid.portal.dao;

import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 29, 2006
 * Time: 12:43:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PointOfContactDAO extends BaseDAO {

    /**
     * Will return a unique list
     * of Points Of Contact.
     * <p/>
     * Unique by a combination of {Email,Role}
     *
     * @return List PointOfContacts
     */
    public List getUniquePeople() throws DataAccessException;

    /**
     * Will do a free text search
     *
     * @param keyword
     * @return List of PointOfContact objects
     */
    public java.util.List keywordSearch(String keyword);

}
