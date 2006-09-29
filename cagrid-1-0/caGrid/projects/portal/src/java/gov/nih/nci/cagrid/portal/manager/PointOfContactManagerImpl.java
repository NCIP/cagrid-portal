package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.dao.PointOfContactDAO;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 29, 2006
 * Time: 12:48:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class PointOfContactManagerImpl extends BaseManagerImpl
        implements PointOfContactManager {

    private PointOfContactDAO pocDAO;

    /**
     * Will return a unique list
     * of People (Points Of Contact).
     * <p/>
     * Unique by a combination of {Email,Role}
     *
     * @return List PointOfContacts
     */

    public List getUniquePeople() throws PortalRuntimeException {
        List result;
        try {
            result = pocDAO.getUniquePeople();
        } catch (DataAccessException e) {
            _logger.error("Error getting a lit of POC's from DB", e);
            throw new PortalRuntimeException(e);
        }
        return result;
    }

    public void setPocDAO(PointOfContactDAO pocDAO) {
        this.pocDAO = pocDAO;
    }

}
