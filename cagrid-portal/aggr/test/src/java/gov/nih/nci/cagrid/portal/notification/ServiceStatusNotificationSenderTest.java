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
package gov.nih.nci.cagrid.portal.notification;

import gov.nih.nci.cagrid.portal.aggr.status.ServiceStatusChangeEvent;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ServiceStatusNotificationSenderTest extends AbstractNotificationTest {


    @Test
    public void send() {
        ServiceStatusNotificationSender sender = new ServiceStatusNotificationSender();
        sender.setMailSender(mockSender);

        NotificationMessage _mockMsg = mock(NotificationMessage.class);
        sender.scheduleSend(_mockMsg);
        verify(mockSender).send(_mockMsg);

    }


    @Test
    public void sendDirect() {
        ServiceStatusChangeNotifier notifier = new ServiceStatusChangeNotifier();
        notifier.setGridServiceDao(new MockGridServiceDao());

        ServiceStatusChangeEvent evt = mock(ServiceStatusChangeEvent.class);
        NotificationSender mockSender = mock(NotificationSender.class);
        notifier.setNotificationSender(mockSender);

        notifier.onApplicationEvent(evt);
        verify(mockSender).sendMessage(MockGridServiceDao.subscriber, evt);

    }


}
