package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.DBTestBase;
import gov.nih.nci.cagrid.portal.domain.Participant;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ParticipantDaoTest extends DBTestBase<ParticipantDao> {


    @Test
    public void get(){
        List<Participant> participants = getDao().getByWorkspaceAbbreviation("ws1");
        assertEquals(participants.size(),1);

        Participant sample = new Participant();
        sample.setEmailAddress("email@email");

        Participant loaded = getDao().getByExample(sample);
        assertNotNull(loaded);

        participants = getDao().getByWorkspaceAbbreviation("ws3");
        participants.contains(loaded);


    }
}
