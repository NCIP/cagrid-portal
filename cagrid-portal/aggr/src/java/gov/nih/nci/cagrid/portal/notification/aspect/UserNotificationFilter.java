/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.notification.aspect;

import gov.nih.nci.cagrid.portal.PortalSystemException;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.NotificationLogMessageDao;
import gov.nih.nci.cagrid.portal.domain.NotificationLogMessage;
import gov.nih.nci.cagrid.portal.domain.NotificationSubscriber;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationEvent;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Transactional
public class UserNotificationFilter {
    private NotificationLogMessageDao notificationLogMessageDao;
    private GridServiceDao gridServiceDao;

    private static final Log logger = LogFactory
            .getLog(UserNotificationFilter.class);

    /**
     * Intercepts
     * NotificationSender.sendMessage(gov.nih.nci.cagrid.portal.domain.NotificationSubscriber notificationSubscriber, T t);
     *
     * @param pjp
     * @param sub
     * @throws Throwable
     */
    public void filter(ProceedingJoinPoint pjp, NotificationSubscriber sub, ApplicationEvent evt) throws Throwable {
        if (sub.isHaltSubscriptions())
            logger.debug("Subscriber with id " + sub.getId() + " has halted notifications. Will not send email");
        else {
            NotificationLogMessage nLog = new NotificationLogMessage(sub, evt.getClass().getSimpleName());
            try {
                pjp.proceed();
                nLog.setSendSuccessful(true);
            } catch (Throwable throwable) {
                logger.warn("Could not send notificaiton to subscriber " + sub.getId());
                throw new PortalSystemException(throwable);
            } finally {
                notificationLogMessageDao.save(nLog);
            }
        }
    }

    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    @Required
    public NotificationLogMessageDao getNotificationLogMessageDao() {
        return notificationLogMessageDao;
    }

    public void setNotificationLogMessageDao(NotificationLogMessageDao notificationLogMessageDao) {
        this.notificationLogMessageDao = notificationLogMessageDao;
    }
}
