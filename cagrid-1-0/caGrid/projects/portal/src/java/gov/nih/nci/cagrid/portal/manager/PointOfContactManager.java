package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 29, 2006
 * Time: 12:47:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PointOfContactManager extends BaseManager {

    /**
     * Will return a unique list
     * of Points Of Contact.
     * <p/>
     * Unique by a combination of {Email,Role}
     *
     * @return List PointOfContacts
     */
    public java.util.List getUniquePeople() throws PortalRuntimeException;

}
