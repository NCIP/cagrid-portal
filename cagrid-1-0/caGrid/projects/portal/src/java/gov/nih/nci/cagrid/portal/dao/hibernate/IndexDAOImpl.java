package gov.nih.nci.cagrid.portal.dao.hibernate;

import gov.nih.nci.cagrid.portal.dao.IndexDAO;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;

/**
 * @version 1.0
 * @created 22-Jun-2006 6:56:33 PM
 */
public class IndexDAOImpl extends BaseHibernateDAOImpl implements IndexDAO {

    public IndexDAOImpl() {

    }

    /**
     * @param epr
     */
    public void deleteByEPR(String epr) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public IndexService getObjectByPK(Integer pk) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Return ID for a EPR string
     *
     * @param epr
     * @return int id
     */
    public int getID4EPR(String epr) throws RecordNotFoundException {
        /**
         * Since epr is unique there can be only one or none
         */
        IndexService idx;
        try {
            idx = (IndexService) getHibernateTemplate().find("from IndexService index where index.epr = ?", epr).get(0);

        } catch (IndexOutOfBoundsException e) {
            throw new RecordNotFoundException();
        }
        return idx.getKey();

    }

}
