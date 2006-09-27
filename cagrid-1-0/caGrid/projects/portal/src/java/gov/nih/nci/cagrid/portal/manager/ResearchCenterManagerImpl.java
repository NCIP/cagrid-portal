package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;

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
        return rcDAO.getUniqueCenters();
    }


}
