package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.NotificationLogMessage;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class NotificationLogMessageDao extends AbstractDao<NotificationLogMessage> {

    public Class domainClass() {
        return NotificationLogMessage.class;
    }
}
