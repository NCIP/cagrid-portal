package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 31, 2006
 * Time: 8:22:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GridServiceBaseDAO extends BaseDAO {
    /**
    * Return ID for a EPR string
    *
    * @param epr
    * @return int id
    */
    public Integer getID4EPR(String epr) throws RecordNotFoundException;
}
