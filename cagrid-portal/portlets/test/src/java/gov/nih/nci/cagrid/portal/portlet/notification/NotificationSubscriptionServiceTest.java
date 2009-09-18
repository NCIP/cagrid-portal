package gov.nih.nci.cagrid.portal.portlet.notification;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.NotificationSubscriberDao;
import gov.nih.nci.cagrid.portal.dao.NotificationSubscriptionDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.NotificationSubscriber;
import gov.nih.nci.cagrid.portal.domain.NotificationSubscription;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.portlet.query.QueryModel;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class NotificationSubscriptionServiceTest {


    @Test
    public void all() throws Exception {

        GridServiceDao _mockGridServiceDao = mock(GridServiceDao.class);
        when(_mockGridServiceDao.getById(anyInt())).thenReturn(new GridService());

        NotificationSubscriptionDao _mockNDao = mock(NotificationSubscriptionDao.class);
        when(_mockNDao.getSubscription(new GridService(), new PortalUser())).thenReturn(null);

        NotificationSubscriptionService service = new NotificationSubscriptionService();
        service.setNotificationSubscriptionDao(_mockNDao);
        service.setGridServiceDao(_mockGridServiceDao);

        try {
            service.loadSubscription(0);
            fail("Cannot return subscription without user in session");
        } catch (Exception e) {
            //expected
        }


        //no exception once user is available
        PortalUser _mockUser = mock(PortalUser.class);
        when(_mockUser.getId()).thenReturn(1);

        QueryModel _mockQModel = mock(QueryModel.class);
        when(_mockQModel.getPortalUser()).thenReturn(_mockUser);
        service.setQueryModel(_mockQModel);

        NotificationSubscription _mockSubcription = mock(NotificationSubscription.class);
        when(_mockNDao.getSubscription(new GridService(), _mockUser)).thenReturn(_mockSubcription);

        PortalUserDao _mockPUserDao = mock(PortalUserDao.class);
        when(_mockPUserDao.getById(1)).thenReturn(_mockUser);
        service.setPortalUserDao(_mockPUserDao);

        NotificationSubscription sub = service.loadSubscription(0);
        assertNotNull(sub);
        assertTrue(service.isSubscribed(0));

        // expect subscription is deleted
        assertFalse(service.subscribreUnsubscribe(0));
        verify(_mockNDao).delete(_mockSubcription);

        // now lets return no subscription
        when(_mockNDao.getSubscription(new GridService(), _mockUser)).thenReturn(null);
        NotificationSubscriberDao _mockSubsciberDao = mock(NotificationSubscriberDao.class);
        service.setNotificationSubscriberDao(_mockSubsciberDao);
        when(_mockUser.getSubscriber()).thenReturn(new NotificationSubscriber());


        // and expect new subscription is created
        service.subscribreUnsubscribe(0);
        verify(_mockNDao).save(new NotificationSubscription());

        // return no subscriber and verify subscriber is created
        when(_mockUser.getSubscriber()).thenReturn(null);
        service.subscribreUnsubscribe(0);
        verify(_mockNDao, times(2)).save(new NotificationSubscription());
        verify(_mockSubsciberDao).save(new NotificationSubscriber());


    }


}
