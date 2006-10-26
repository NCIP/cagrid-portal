package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.dao.CaBIGParticipantDAO;
import gov.nih.nci.cagrid.portal.domain.CaBIGParticipant;
import gov.nih.nci.cagrid.portal.domain.CaBIGWorkspace;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;
import org.springframework.dao.DataAccessException;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 17, 2006
 * Time: 11:07:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class CaBIGWorkspaceManagerImpl extends GeocodingBaseManagerImpl
        implements CaBIGWorkspaceManager {

    private CaBIGParticipantDAO caBIGDAO;


    public List getUniqueParticipants() throws PortalRuntimeException {
        try {
            return caBIGDAO.getUniqueParticipants();
        } catch (DataAccessException e) {
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void save(CaBIGWorkspace workspace) throws PortalRuntimeException {

        if (workspace.getParticipants() != null) {
            for (Iterator iter = workspace.getParticipants().iterator(); iter.hasNext();) {
                CaBIGParticipant participant = (CaBIGParticipant) iter.next();
                geocodeDomainObject(participant);
                try {
                    Integer objectID = gridServiceBaseDAO.getSurrogateKey(participant);
                    participant.setPk(objectID);
                } catch (RecordNotFoundException e) {
                    // Do nothing as this is not unexpected
                    _logger.info("Record not found for " + participant.getClass() + ". Creating new one with ORM assigned ID");
                }
            }
        }
        _logger.debug("Saving workspace" + workspace.getShortName() + " with " + workspace.getParticipants().size() + " participants");
        super.save(workspace);
    }


    public void setCaBIGDAO(CaBIGParticipantDAO caBIGDAO) {
        this.caBIGDAO = caBIGDAO;
    }
}
