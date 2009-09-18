package gov.nih.nci.cagrid.portal.notification;

import gov.nih.nci.cagrid.portal.aggr.status.ServiceStatusChangeEvent;
import gov.nih.nci.cagrid.portal.domain.NotificationSubscriber;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.util.PortalAggrIntegrationTestBase;

/**
 * Integration test initializes the spring container.
 * <p/>
 * Will send email out.
 * So should NOT be run in the automated build
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class NotificationCtxIntegrationTest extends PortalAggrIntegrationTestBase {


    public void testLoadContext() {
        getApplicationContext().getBean("serviceStatusChangeGenerator");
        getApplicationContext().getBean("serviceStatusChangeNotifier");
    }


    public void testSendMessage() {
        ServiceStatusNotificationSender notificationSender = (ServiceStatusNotificationSender) getApplicationContext().getBean("serviceStatusNotificationSender");

        Person person = new Person();
        person.setEmailAddress("kherm@mail.nih.gov");
        PortalUser user = new PortalUser();
        user.setPerson(person);

        NotificationSubscriber sub = new NotificationSubscriber();
        sub.setPortalUser(user);

        ServiceStatusChangeEvent evt = new ServiceStatusChangeEvent(this);
        evt.setOldStatus(ServiceStatus.ACTIVE);
        evt.setNewStatus(ServiceStatus.INACTIVE);
        evt.setServiceUrl("http://service-url");

        notificationSender.sendMessage(sub, evt);



    }

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-notification-aspects.xml",
                "classpath*:applicationContext-db.xml"

        };
    }

}
