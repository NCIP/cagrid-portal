package gov.nih.nci.cagrid.portal.dao.hibernate;

import gov.nih.nci.cagrid.portal.dao.GridServiceBaseDAO;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 31, 2006
 * Time: 8:20:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridServiceBaseDAOImpl extends BaseDAOImpl
    implements GridServiceBaseDAO {
    /**
     * Return ID for a EPR string
     *
     * @param epr
     * @return int id
     */
    public Integer getID4EPR(String epr) throws RecordNotFoundException {
        /**
         * Since epr is unique there can be only one or none
         */
        Integer idx = null;

        try {
            _logger.debug("Getting ID for service:" + epr + ".");

            List resultSet = getHibernateTemplate().find("Select index.pk from IndexService index where index.EPR = ?",
                    epr);

            /** if epr is not index then try
             * the services table
             */
            if (resultSet.isEmpty()) {
                resultSet = getHibernateTemplate().find("Select service.pk from RegisteredService service where service.EPR = ?",
                        epr);
            }

            //return the first id as it should be unizue there should only be one
            idx = (Integer) resultSet.get(0);
        } catch (IndexOutOfBoundsException e) {
            logger.warn("Record not found for service: " + epr);
            throw new RecordNotFoundException();
        }

        _logger.debug("ID:" + idx + " found for EPR");

        return idx;
    }
}
