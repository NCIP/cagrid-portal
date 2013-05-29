/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.DBTestBase;
import gov.nih.nci.cagrid.portal.domain.NotificationLogMessage;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class NotificationLogMessageDaoTest extends DBTestBase<NotificationLogMessageDao> {


    @Test
    public void get() {

        assertNotNull(getDao().getAll());
        NotificationLogMessage loaded = getDao().getById(-1);
        assertFalse(loaded.isSendSuccessful());
        loaded.setSendSuccessful(true);
        getDao().save(loaded);
        NotificationLogMessage reloaded = getDao().getById(-1);
        assertTrue(reloaded.isSendSuccessful());

    }
}
