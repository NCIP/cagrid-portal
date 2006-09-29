package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.dao.ResearchCenterDAO;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 27, 2006
 * Time: 3:16:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResearchCenterManagerImpl extends BaseManagerImpl
        implements ResearchCenterManager {

    private ResearchCenterDAO rcDAO;

    /**
     * Will return all UNIQUE/DISTINCT
     * research centers (based on geocoding information)
     * <p/>
     * If you need ALL persistent centers use loadAll method
     *
     * @return
     * @throws gov.nih.nci.cagrid.portal.exception.PortalRuntimeException
     *
     * @see gov.nih.nci.cagrid.portal.manager.BaseManager#loadAll(Class)
     */
    public List getUniqueCenters() throws PortalRuntimeException {
        List result;
        try {
            result = rcDAO.getUniqueCenters();
        } catch (DataAccessException e) {
            _logger.error("Error getting Centers from DAO", e);
            throw new PortalRuntimeException(e);
        }
        return result;

    }

    public void setRcDAO(ResearchCenterDAO rcDAO) {
        this.rcDAO = rcDAO;
    }
}
