package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.domain.CaBIGParticipant;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Checks if caBIG Participant data
 * is in DB. CaBIG Participant data is
 * created as part of Portal seed data
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 26, 2006
 * Time: 3:52:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class CaBIGManagerLocalTestCase extends BaseSpringDataAccessAbstractTestCase {
    private CaBIGWorkspaceManager caBIGManager;

    public void testManager() {
        List participantList = caBIGManager.getUniqueParticipants();
        //make sure participants exist in DB
        assertNotNull(participantList);
        assertTrue(participantList.size() > 1);

        for (Iterator iter = participantList.iterator(); iter.hasNext();) {
            CaBIGParticipant participant = (CaBIGParticipant) iter.next();

            //name is a required attribute
            assertNotNull(participant.getName());

            //make sure participants belong to 1 or more workspaces
            Set workspaces = participant.getWorkspaceCollection();
            assertTrue(workspaces.size() > 0);
        }


    }


    public void setCaBIGManager(CaBIGWorkspaceManager caBIGManager) {
        this.caBIGManager = caBIGManager;
    }
}
