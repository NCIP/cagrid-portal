package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.dao.BaseDAO;
import gov.nih.nci.cagrid.portal.dao.IndexDAO;

import java.util.List;

/**
 * Implementation of the BaseManager Interface
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 28, 2006
 * Time: 6:23:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseManagerImpl implements BaseManager {


    protected BaseDAO baseDAO;
    protected IndexDAO indexDAO;

    protected BaseDAO getBaseDAO() {
        return baseDAO;
    }

    protected void setBaseDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    protected IndexDAO getIndexDAO() {
        return indexDAO;
    }

    protected void setIndexDAO(IndexDAO _indexDAO) {
        this.indexDAO = _indexDAO;
    }

    /**
     * Returns All the persistent
     * object for given class
     *
     * @return List
     */
    public List loadAll(Class cls) {
        return baseDAO.loadAll(cls);
    }
}
