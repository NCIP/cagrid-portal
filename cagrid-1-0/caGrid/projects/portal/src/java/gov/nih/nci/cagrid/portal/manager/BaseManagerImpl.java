package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.dao.BaseDAO;
import gov.nih.nci.cagrid.portal.dao.GridServiceBaseDAO;
import gov.nih.nci.cagrid.portal.dao.IndexDAO;
import org.apache.log4j.Category;

import java.util.Collection;
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
    protected GridServiceBaseDAO gridServiceBaseDAO;
    protected Category _logger = Category.getInstance(getClass().getName());

    public void saveAll(Collection objects) {
        baseDAO.saveOrUpdate(objects);
    }

    public void save(Object obj) {
        baseDAO.saveOrUpdate(obj);
    }

    public List loadAll(Class cls) {
        return baseDAO.loadAll(cls);
    }

    /** Setter for Spring **/
    public void setBaseDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    /** Setter for Spring **/
    public void setIndexDAO(IndexDAO indexDAO) {
        this.indexDAO = indexDAO;
    }

    public void set_logger(Category _logger) {
        this._logger = _logger;
    }

    public void setGridServiceBaseDAO(GridServiceBaseDAO gridServiceBaseDAO) {
        this.gridServiceBaseDAO = gridServiceBaseDAO;
    }
}
