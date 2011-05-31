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
