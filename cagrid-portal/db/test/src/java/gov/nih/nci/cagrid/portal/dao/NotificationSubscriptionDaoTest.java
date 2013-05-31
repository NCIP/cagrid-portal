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

import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class NotificationSubscriptionDaoTest extends DaoTestBase<NotificationSubscriptionDao> {

    private String dummyUrl = "http://";

    @Test
    public void saveAndRetreive() {

        GridService service = new GridService();
        service.setUrl(dummyUrl);
        ((AbstractDao) getDao()).save(service);

        NotificationSubscriber subscriber = new NotificationSubscriber();
        subscriber.setPortalUser(createUser("email1"));
        ((AbstractDao) getDao()).save(subscriber);

        NotificationSubscription subs = new NotificationSubscription();
        subs.setService(service);
        subs.setSubscriber(subscriber);
        getDao().save(subs);

        getDao().save(subs);

        assertEquals(subscriber.getPortalUser(), getDao().getAll().get(0).getSubscriber().getPortalUser());
        assertNotNull(getDao().getAll().get(0));
        assertNotNull(getDao().getAll().get(0).getSubscriber());
        assertNotNull(getDao().getAll().get(0).getSubscriber().getPortalUser());
        assertEquals(getDao().getAll().get(0).getSubscriber().getPortalUser().getPerson().getEmailAddress(), "email1");

        // add another subscription for a different user
        NotificationSubscription subs2 = new NotificationSubscription();
        NotificationSubscriber subscriber2 = new NotificationSubscriber();
        PortalUser user = createUser("email12");
        subscriber2.setPortalUser(user);
        ((AbstractDao) getDao()).save(subscriber2);

        subs2.setSubscriber(subscriber2);
        subs2.setService(service);

        getDao().save(subs2);

        assertEquals(2, getDao().getAll().size());
        assertNotNull(getDao().getAll().get(1).getSubscriber());
        assertNotNull(getDao().getAll().get(1).getSubscriber().getPortalUser());
        assertEquals(getDao().getAll().get(1).getSubscriber().getPortalUser().getPerson().getEmailAddress(), "email12");

        assertEquals(subs2, getDao().getSubscription(service, user));

        subscriber2.setPortalUser(null);
        getDao().save(subs2);


    }

    private PortalUser createUser(String emailAddress) {
        Person p = new Person();
        p.setEmailAddress(emailAddress);
        ((AbstractDao) getDao()).save(p);

        PortalUser user = new PortalUser();
        user.setPerson(p);

        ((AbstractDao) getDao()).save(user);
        return user;


    }
}
