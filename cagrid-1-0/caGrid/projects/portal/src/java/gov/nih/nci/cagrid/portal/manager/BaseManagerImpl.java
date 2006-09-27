package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.dao.BaseDAO;
import gov.nih.nci.cagrid.portal.dao.GridServiceBaseDAO;
import gov.nih.nci.cagrid.portal.dao.JdbcDAO;
import gov.nih.nci.cagrid.portal.dao.ResearchCenterDAO;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;

import java.util.List;


/**
 * Implementation of the BaseManager Interface
 * Is abstract because its not supposed to be used
 * directly. Extending class needs to
 * implement the save method
 *
 * @see BaseManager
 *      <p/>
 *      <p/>
 *      Created by IntelliJ IDEA.
 *      User: kherm
 *      Date: Jun 28, 2006
 *      Time: 6:23:16 PM
 *      To change this template use File | Settings | File Templates.
 */
public abstract class BaseManagerImpl implements BaseManager {
    protected BaseDAO baseDAO;

    protected GridServiceBaseDAO gridServiceBaseDAO;
    protected JdbcDAO jdbcDAO;
    protected ResearchCenterDAO rcDAO;

    protected Category _logger = Category.getInstance(getClass().getName());


    public Object getObjectByPrimaryKey(Class cls, Integer id) throws PortalRuntimeException {
        try {
            return baseDAO.getObjectByPrimaryKey(cls, id);
        } catch (DataAccessException e) {
            throw new PortalRuntimeException(e);
        }
    }

    public List loadAll(Class cls) throws PortalRuntimeException {
        try {
            return baseDAO.loadAll(cls);
        } catch (DataAccessException e) {
            throw new PortalRuntimeException(e);
        }
    }


    /**
     * setters for spring *
     */

    public void setRcDAO(ResearchCenterDAO rcDAO) {
        this.rcDAO = rcDAO;
    }


    public void setBaseDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    public void set_logger(Category _logger) {
        this._logger = _logger;
    }

    public void setGridServiceBaseDAO(GridServiceBaseDAO gridServiceBaseDAO) {
        this.gridServiceBaseDAO = gridServiceBaseDAO;
    }

    public void setJdbcDAO(JdbcDAO jdbcDAO) {
        this.jdbcDAO = jdbcDAO;
    }

}
