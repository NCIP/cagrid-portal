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
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.NotificationSubscriber;
import gov.nih.nci.cagrid.portal.domain.NotificationSubscription;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ServiceStatusChangeNotifierTest {
    private ServiceStatusChangeNotifier notifier;


    @Before
    public void setup() {
        notifier = new ServiceStatusChangeNotifier();
    }

    @Test
    public void event() {
        String serviceUrl = "http://service-url";

        NotificationSubscriber sub = new NotificationSubscriber();
        NotificationSubscription subn = new NotificationSubscription();
        subn.setSubscriber(sub);

        List<NotificationSubscription> mockedList = new ArrayList<NotificationSubscription>();
        mockedList.add(subn);

        GridService service = new GridService();
        service.setSubscriptions(mockedList);

        GridServiceDao mockDao = mock(GridServiceDao.class);
        when(mockDao.getByUrl(serviceUrl)).thenReturn(service);
        notifier.setGridServiceDao(mockDao);

        // register a sender
        ServiceStatusChangeEvent evt = new ServiceStatusChangeEvent(this);
        evt.setServiceUrl(serviceUrl);

        NotificationSender mockSender = mock(NotificationSender.class);
        notifier.setNotificationSender(mockSender);
        notifier.onApplicationEvent(evt);

        // verify message is sent
        verify(mockSender, times(1)).sendMessage(sub, evt);

        mockedList.add(subn);
        notifier.onApplicationEvent(evt);
        verify(mockSender, times(3)).sendMessage(sub, evt);

    }
}
