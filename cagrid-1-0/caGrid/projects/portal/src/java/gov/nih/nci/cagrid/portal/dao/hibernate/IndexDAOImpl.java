package gov.nih.nci.cagrid.portal.dao.hibernate;

import gov.nih.nci.cagrid.portal.dao.IndexDAO;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import org.springframework.dao.DataAccessException;

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

    public void saveOrUpdate(IndexService idx) throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(idx);
    }

    public IndexService getObjectByPK(Integer pk) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}