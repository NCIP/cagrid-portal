package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;
import org.springframework.dao.DataAccessException;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 31, 2006
 * Time: 8:22:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GridServiceBaseDAO extends BaseDAO {

    /**
     * Will get an Ordered List of services
     *
     * @return
     * @throws DataAccessException
     */
    public List getAllServices() throws DataAccessException;

    /**
     * Will return the business key depending
     * on the type of object
     *
     * @param obj
     * @return Integer key value
     * @throws RecordNotFoundException will happen if no record is found
     * @throws DataAccessException     serious error
     */
    public Integer getSurrogateKey(DomainObject obj) throws DataAccessException, RecordNotFoundException;

    /**
     * Will do a free text search
     *
     * @param keyword
     * @return List of PointOfContact objects
     */
    public java.util.List keywordSearch(String keyword) throws DataAccessException;

}
