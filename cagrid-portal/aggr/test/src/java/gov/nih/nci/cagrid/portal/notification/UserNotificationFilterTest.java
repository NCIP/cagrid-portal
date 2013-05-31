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
package gov.nih.nci.cagrid.portal.notification;

import gov.nih.nci.cagrid.portal.aggr.status.ServiceStatusChangeEvent;
import gov.nih.nci.cagrid.portal.domain.NotificationLogMessage;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class UserNotificationFilterTest extends AbstractNotificationTest {
    private ServiceStatusNotificationSender sender;
    private ServiceStatusChangeEvent evt;

    @Before
    public void setup() {
        sender = (ServiceStatusNotificationSender) ctx.getBean("serviceStatusNotificationSender");
        sender.setMailSender(mockSender);
        evt = new ServiceStatusChangeEvent(this);
        evt.setOldStatus(ServiceStatus.ACTIVE);
        evt.setNewStatus(ServiceStatus.INACTIVE);
        evt.setServiceUrl("http://service-url");
    }

    @Test
    public void send() {

        sender.sendMessage(sub, evt);
        verify(mockSender).send((NotificationMessage) anyObject());

// halt notifications
        sub.setHaltSubscriptions(true);
        sender.sendMessage(sub, evt);
// make sure an email is not sent again       
        verify(mockSender, times(1)).send((NotificationMessage) anyObject());

        sub.setHaltSubscriptions(false);
        sender.sendMessage(sub, evt);
// make sure an email is sent       
        verify(mockSender, times(2)).send((NotificationMessage) anyObject());


    }

    @Test
    // test to see if logs are being created
    public void sendLog() {
        sender.sendMessage(sub, evt);
        assertNotNull(notificationLogMessageDao.getAll());
        assertEquals(1, notificationLogMessageDao.getAll().size());
        NotificationLogMessage log = notificationLogMessageDao.getAll().get(0);
        assertTrue("Send should have been successfull", log.isSendSuccessful());

        sub.setHaltSubscriptions(true);
        sender.sendMessage(sub, evt);
        assertEquals("Message should be sent", 1, notificationLogMessageDao.getAll().size());

        sub.setHaltSubscriptions(false);
        sender.sendMessage(sub, evt);
        assertEquals(2, notificationLogMessageDao.getAll().size());

    }


}
