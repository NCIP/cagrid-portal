package gov.nih.nci.cagrid.portal.notification;

import gov.nih.nci.cagrid.portal.PortalSystemException;
import gov.nih.nci.cagrid.portal.domain.NotificationSubscriber;
import org.springframework.context.ApplicationEvent;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface NotificationSender<T extends ApplicationEvent> {
    void sendMessage(NotificationSubscriber sub, T e) throws PortalSystemException;

    void scheduleSend(NotificationMessage nm) throws PortalSystemException;

}
