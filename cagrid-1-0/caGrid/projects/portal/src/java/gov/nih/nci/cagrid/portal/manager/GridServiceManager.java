package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import org.springframework.dao.DataAccessException;

import java.util.List;


/**
 * Grid aware manager. Will process
 * Grid related classes
 *
 * @see GridServiceManager
 * @see BaseManager
 *      <p/>
 *      Created by IntelliJ IDEA.
 *      User: kherm
 *      Date: Jun 29, 2006
 *      Time: 5:47:44 PM
 *      To change this template use File | Settings | File Templates.
 */
public interface GridServiceManager extends BaseManager {

    /**
     * Will return all Registered Services
     *
     * @throws DataAccessException
     */
    public List getAllServices() throws PortalRuntimeException;


    /**
     * Save a Registered Service. THis is what
     * ties all other metadata in the DB
     * <p/>
     * Method will delete the old service
     * and then save a new one. This is
     * done so that metadata elements
     * are not appended to the Service
     * but created fresh. For eg. Service
     * has operation A,B
     * With each save() call Operations
     * get appended to the Service in the DB.
     * To prevent the Operations (Children) list
     * from growing with each save(), the
     * Service(Parent) is delted from DB first
     * <p/>
     * This happens inside a transaction
     * so its transaction safe
     *
     * @param rService
     * @throws PortalRuntimeException
     */


    public void save(RegisteredService rService) throws PortalRuntimeException;
}
