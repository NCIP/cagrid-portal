package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;
import org.springframework.dao.DataAccessException;

import java.util.List;


/**
 * Service layer. Will log errors
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 28, 2006
 * Time: 5:24:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridServiceManagerImpl extends GeocodingBaseManagerImpl
        implements GridServiceManager {

    public GridServiceManagerImpl() {
        //for spring
    }

    /**
     * @return
     * @throws PortalRuntimeException
     * @see GridServiceManager#getAllServices()
     */
    public List getAllServices() throws PortalRuntimeException {
        try {
            return gridServiceBaseDAO.getAllServices();
        } catch (DataAccessException e) {
            throw new PortalRuntimeException(e);
        }
    }


    /**
     * @param rService
     * @throws PortalRuntimeException
     * @see GridServiceManager#save(gov.nih.nci.cagrid.portal.domain.RegisteredService)
     */
    public synchronized void save(RegisteredService rService) throws PortalRuntimeException {

        try {

            try {
                Integer objectID = gridServiceBaseDAO.getSurrogateKey(rService);

                //if service exists then flush the Domain Model and Operations
                RegisteredService rServiceOld = (RegisteredService) gridServiceBaseDAO.getObjectByPrimaryKey(RegisteredService.class, objectID);
                gridServiceBaseDAO.delete(rServiceOld);

            } catch (RecordNotFoundException e) {
                // Do nothing as this is not unexpected
            }

            if (rService.getResearchCenter() != null)
                geocodeDomainObject(rService.getResearchCenter());

            //save the new service. ORM will create a new ID
            rService.setPk(null);
            gridServiceBaseDAO.saveOrUpdate(rService);

        } catch (Exception e) {
            //log exception and rethrow
            _logger.error("Problem saving Registered Service", e);
            throw new PortalRuntimeException(e);
        }
    }

    /**
     * Keyword base searches. SHould be implemented by specific Managers(implementing classes)
     *
     * @param keyword
     * @return
     * @throws gov.nih.nci.cagrid.portal.exception.PortalRuntimeException
     *
     */
    public List keywordSearch(String keyword) throws PortalRuntimeException {
        try {
            return gridServiceBaseDAO.keywordSearch(keyword);
        } catch (DataAccessException e) {
            _logger.error(e);
            throw new PortalRuntimeException(e);
        }
    }

}
