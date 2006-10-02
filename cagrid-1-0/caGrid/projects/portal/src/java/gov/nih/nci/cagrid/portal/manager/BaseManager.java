package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import org.springframework.dao.DataAccessException;


/**
 * BaseManager Interface unaware
 * of any Grid related objects.
 * Will store/process generic Domain Objects
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 6:13:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BaseManager {

    public Object getObjectByPrimaryKey(Class cls, Integer id) throws PortalRuntimeException;

    /**
     * Save a Domain Object in DB.
     * This will be implemented
     * by implementing class
     * <p/>
     * Generic save method. Should be
     * overriden in child classes
     */
    public void save(DomainObject obj) throws PortalRuntimeException;

    /**
     * Will retreive all persistent objects
     * of type cls
     *
     * @param cls Class of objects to be retreived
     * @return
     * @throws DataAccessException
     */
    public java.util.List loadAll(Class cls) throws PortalRuntimeException;

    /**
     * Keyword base searches. SHould be implemented by specific Managers(implementing classes)
     *
     * @param keyword
     * @return
     * @throws PortalRuntimeException
     */
    public java.util.List keywordSearch(String keyword) throws PortalRuntimeException;
}
