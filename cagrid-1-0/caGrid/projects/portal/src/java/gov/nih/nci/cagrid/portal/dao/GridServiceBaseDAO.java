package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;
import org.springframework.dao.DataAccessException;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 31, 2006
 * Time: 8:22:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GridServiceBaseDAO extends BaseDAO {


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

}
