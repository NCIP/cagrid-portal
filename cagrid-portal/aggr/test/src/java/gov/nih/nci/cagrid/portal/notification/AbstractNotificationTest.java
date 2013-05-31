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

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.NotificationLogMessageDao;
import gov.nih.nci.cagrid.portal.domain.*;
import gov.nih.nci.cagrid.portal.notification.aspect.UserNotificationFilter;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AbstractNotificationTest {

    protected NotificationSubscriber sub;
    protected String toEmailAddress = "to";
    protected StaticApplicationContext ctx;
    protected JavaMailSender mockSender;
    protected NotificationLogMessageDao notificationLogMessageDao;

    @Before
    public void prepare() {
        mockSender = mock(JavaMailSender.class);

        Person p = new Person();
        p.setEmailAddress(toEmailAddress);
        PortalUser user = new PortalUser();
        user.setPerson(p);
        user.setGridIdentity("gridId");
        sub = new NotificationSubscriber();
        sub.setPortalUser(user);

        MockGridServiceDao.subscriber = sub;

        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"applicationContext-notification-aspects.xml", "applicationContext-db.xml"});
        ctx = new StaticApplicationContext(context);
        UserNotificationFilter filter = (UserNotificationFilter) ctx.getBean("userNotificationFilter");
        notificationLogMessageDao = new MockNotificationLogMessageDao();
        filter.setNotificationLogMessageDao(notificationLogMessageDao);

    }

    protected class MockNotificationLogMessageDao extends NotificationLogMessageDao {

        private List<NotificationLogMessage> inMemoryList = new ArrayList<NotificationLogMessage>();

        @Override
        public void save(NotificationLogMessage domainObject) {
            inMemoryList.add(domainObject);
        }

        @Override
        public List<NotificationLogMessage> getAll() {
            return inMemoryList;
        }
    }

    protected static class MockGridServiceDao extends GridServiceDao {
        static NotificationSubscriber subscriber;


        @Override
        public GridService getByUrl(String url) {
            GridService gridService = new GridService();
            gridService.setUrl("http://grid-service");
            NotificationSubscription sub = new NotificationSubscription();
            sub.setSubscriber(new NotificationSubscriber());

            gridService.getSubscriptions().add(sub);
            return gridService;
        }
    }
}
