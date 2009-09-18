package gov.nih.nci.cagrid.portal.notification;

import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * Marker interface. Created to easily change message types
 * without affecting other classes.
 * To change message type, change this marker interface and the
 * following classes
 * <p/>
 * AbstractNotificationSender
 * AbstractNotificationGenerator
 * <p/>
 * <p/>
 * will accept this type.
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface NotificationMessage extends MimeMessagePreparator {
}
