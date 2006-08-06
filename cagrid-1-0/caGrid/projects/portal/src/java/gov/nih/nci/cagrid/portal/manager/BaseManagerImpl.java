package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.dao.BaseDAO;
import gov.nih.nci.cagrid.portal.dao.IndexDAO;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Category;


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
    protected Category _logger = Category.getInstance(getClass().getName());



    /**
     * Returns All the persistent
     * object for given class
     *
     * @return List
     */
    public List loadAll(Class cls) {
        return baseDAO.loadAll(cls);
    }

    public void saveAll(Collection objects) {
        baseDAO.saveOrUpdate(objects);
    }

    public void save(Object obj) {
        baseDAO.saveOrUpdate(obj);
    }

    /** Setter for Spring **/
    public void setBaseDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    /** Setter for Spring **/
    public void setIndexDAO(IndexDAO indexDAO) {
        this.indexDAO = indexDAO;
    }
}
