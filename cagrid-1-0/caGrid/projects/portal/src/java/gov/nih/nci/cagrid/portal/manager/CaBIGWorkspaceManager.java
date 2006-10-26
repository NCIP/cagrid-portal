package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.CaBIGWorkspace;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 17, 2006
 * Time: 1:26:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CaBIGWorkspaceManager extends BaseManager {

    public List getUniqueParticipants() throws PortalRuntimeException;

    /**
     * Save a caBIG workspace and its participants
     *
     * @param workspace
     * @throws PortalRuntimeException
     */
    public void save(CaBIGWorkspace workspace) throws PortalRuntimeException;

}
