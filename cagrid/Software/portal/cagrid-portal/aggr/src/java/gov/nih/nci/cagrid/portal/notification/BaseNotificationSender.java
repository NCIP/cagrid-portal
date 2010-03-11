package gov.nih.nci.cagrid.portal.notification;

import gov.nih.nci.cagrid.portal.PortalSystemException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationEvent;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class BaseNotificationSender<T extends ApplicationEvent> implements NotificationSender<T> {
    private JavaMailSender mailSender;
    private VelocityNotificationGenerator notificationGenerator;


    protected final Log logger = LogFactory
            .getLog(getClass());

    //Todo probably store in DB for scalability
    public void scheduleSend(NotificationMessage nm) throws PortalSystemException {
        try {
            getMailSender().send(nm);
        } catch (MailException e) {
            logger.warn("Could not send Notification Message " + e);
            throw new PortalSystemException(e);
        }
    }

    @Required
    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public VelocityNotificationGenerator getNotificationGenerator() {
        return notificationGenerator;
    }

    public void setNotificationGenerator(VelocityNotificationGenerator notificationGenerator) {
        this.notificationGenerator = notificationGenerator;
    }
}
