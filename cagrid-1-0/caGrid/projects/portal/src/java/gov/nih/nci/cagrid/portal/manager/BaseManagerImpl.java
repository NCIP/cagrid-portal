package gov.nih.nci.cagrid.portal.manager;


import gov.nih.nci.cagrid.portal.dao.GridServiceBaseDAO;
import gov.nih.nci.cagrid.portal.dao.JdbcDAO;
import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;
import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;

import java.util.List;


/**
 * Implementation of the BaseManager Interface
 * Is abstract because its not supposed to be used
 * directly. Extending class needs to
 * implement the save method
 * <p/>
 * Declared abstract so it cannot be used directly
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


    protected GridServiceBaseDAO gridServiceBaseDAO;
    protected JdbcDAO jdbcDAO;

    protected Category _logger = Category.getInstance(getClass().getName());

    /**
     * @param obj
     * @throws PortalRuntimeException
     * @see BaseManager#save(gov.nih.nci.cagrid.portal.domain.DomainObject)
     */
    public void save(DomainObject obj) throws PortalRuntimeException {
        try {
            try {
                Integer objectID = gridServiceBaseDAO.getSurrogateKey(obj);
                obj.setPk(objectID);
            } catch (RecordNotFoundException e) {
                // Do nothing as this is not unexpected
                _logger.info("Record not found for " + obj.getClass() + ". Creating new one with ORM assigned ID");
            }
            gridServiceBaseDAO.saveOrUpdate(obj);
        } catch (DataAccessException e) {
            throw new PortalRuntimeException(e);
        }
    }

    public Object getObjectByPrimaryKey(Class cls, Integer id) throws PortalRuntimeException {
        try {
            return gridServiceBaseDAO.getObjectByPrimaryKey(cls, id);
        } catch (DataAccessException e) {
            throw new PortalRuntimeException(e);
        }
    }

    public List loadAll(Class cls) throws PortalRuntimeException {
        try {
            return gridServiceBaseDAO.loadAll(cls);
        } catch (DataAccessException e) {
            throw new PortalRuntimeException(e);
        }
    }

    /**
     * Will return the total number of instances in the DB
     * for a particular class.
     *
     * @param cls
     * @return
     * @throws gov.nih.nci.cagrid.portal.exception.PortalRuntimeException
     *
     */
    public int getCount(Class cls) throws PortalRuntimeException {
        return gridServiceBaseDAO.getCount(cls);
    }

    /**
     * setters for spring *
     */

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
