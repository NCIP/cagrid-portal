package gov.nih.nci.cagrid.portal.notification.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Aspect that will enable/disable
 * notifications feature for the Portal.
 * <p/>
 * Controlled with the cagrid.portal.notifications.enabled
 * property
 * <p/>
 * <p/>
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalNotificationsFilter {
    private static final Log logger = LogFactory
            .getLog(PortalNotificationsFilter.class);

    private boolean enableNotifications;

    public void filter(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("Filtering notifications");
        if (enableNotifications) {
            logger.info("Notifications are enabled. Will allow notification to be sent");
            pjp.proceed();
        } else
            logger.info("Notifications are DISABLED. Will NOT allow notification to be sent");
    }


    public boolean isEnableNotifications() {
        return enableNotifications;
    }

    public void setEnableNotifications(boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }
}
