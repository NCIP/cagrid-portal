/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
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
