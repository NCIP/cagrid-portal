package gov.nih.nci.cagrid.portal.notification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;

/**
 * Base Events based notifier class.
 * Implemtntations will handle specefic event types
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class AbstractNotifier implements ApplicationListener {

    private NotificationSender notificationSender;

    protected final Log logger = LogFactory
            .getLog(getClass());


    public NotificationSender getNotificationSender() {
        return notificationSender;
    }

    public void setNotificationSender(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }
}
