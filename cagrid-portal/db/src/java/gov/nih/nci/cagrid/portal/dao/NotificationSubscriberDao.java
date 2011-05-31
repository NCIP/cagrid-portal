package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.NotificationSubscriber;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class NotificationSubscriberDao extends AbstractDao<NotificationSubscriber> {

    public Class domainClass() {
        return NotificationSubscriberDao.class;
    }
}
