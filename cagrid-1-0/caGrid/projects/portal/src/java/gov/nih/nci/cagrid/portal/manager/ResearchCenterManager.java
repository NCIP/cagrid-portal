package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 27, 2006
 * Time: 3:10:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ResearchCenterManager extends BaseManager {


    /**
     * Will return all UNIQUE/DISTINCT
     * research centers (based on geocoding information)
     * <p/>
     * If you need ALL persistent centers use loadAll method
     *
     * @return
     * @throws PortalRuntimeException
     * @see BaseManager#loadAll(Class)
     */
    public java.util.List getUniqueCenters() throws PortalRuntimeException;

}
