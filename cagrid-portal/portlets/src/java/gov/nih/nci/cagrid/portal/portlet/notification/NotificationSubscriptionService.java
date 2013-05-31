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
package gov.nih.nci.cagrid.portal.portlet.notification;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.NotificationSubscriberDao;
import gov.nih.nci.cagrid.portal.dao.NotificationSubscriptionDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.NotificationSubscriber;
import gov.nih.nci.cagrid.portal.domain.NotificationSubscription;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.portlet.AjaxViewGenerator;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.UserModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@RemoteProxy(name = "NotificationSubscriptionService",
        creator = SpringCreator.class,
        creatorParams = @Param(name = "beanName",
                value = "notificationSubscriptionService"))
public class NotificationSubscriptionService extends AjaxViewGenerator {

    private NotificationSubscriptionDao notificationSubscriptionDao;
    private NotificationSubscriberDao notificationSubscriberDao;
    private GridServiceDao gridServiceDao;
    private PortalUserDao portalUserDao;
    private static final Log logger = LogFactory.getLog(NotificationSubscriptionService.class);
    private UserModel userModel;


    /**
     * Will subscribe a user to the service for notifications.
     * If user is already subscribed, then it will unsubscribe the user
     */
    @RemoteMethod
    @Transactional
    public boolean subscribreUnsubscribe(int serviceId) {
        NotificationSubscription sub = loadSubscription(serviceId);
        if (sub != null) {
            notificationSubscriptionDao.delete(sub);
            logger.debug("Unsubscribed user from service " + serviceId);
            return false;
        } else {
            logger.debug("Creating new subscription for service with id " + serviceId);
            sub = new NotificationSubscription();
            sub.setService(gridServiceDao.getById(serviceId));
            NotificationSubscriber subsc = getPortalUser().getSubscriber();
            if (subsc == null) {
                subsc = new NotificationSubscriber();
                subsc.setPortalUser(getPortalUser());
                notificationSubscriberDao.save(subsc);
            }
            sub.setSubscriber(subsc);
            notificationSubscriptionDao.save(sub);
            logger.debug("Subscribed user from service " + serviceId);
            return true;
        }
    }


    @RemoteMethod
    public boolean isSubscribed(int serviceId) throws CaGridPortletApplicationException {
        logger.debug("Checking subscription for " + serviceId);
        return loadSubscription(serviceId) != null;

    }

    protected NotificationSubscription loadSubscription(int serviceId) throws CaGridPortletApplicationException {
        if (getPortalUser() == null) {
            logger.warn("No portal user found. Trying to add notification. Denied!");
            throw new CaGridPortletApplicationException("Please login first");
        }
        GridService service = gridServiceDao.getById(serviceId);
        if (service == null) {
            logger.warn("No service found. Trying to add notification. Denied!");
            throw new CaGridPortletApplicationException("Service not found in DB");
        }

        return notificationSubscriptionDao.getSubscription(service, getPortalUser());

    }

    public NotificationSubscriptionDao getNotificationSubscriptionDao() {
        return notificationSubscriptionDao;
    }

    public void setNotificationSubscriptionDao(NotificationSubscriptionDao notificationSubscriptionDao) {
        this.notificationSubscriptionDao = notificationSubscriptionDao;
    }

    public PortalUser getPortalUser() {
        if (getUserModel().getPortalUser() != null)
            return portalUserDao.getById(getUserModel().getPortalUser().getId());
        return null;
    }


    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    public NotificationSubscriberDao getNotificationSubscriberDao() {
        return notificationSubscriberDao;
    }

    public void setNotificationSubscriberDao(NotificationSubscriberDao notificationSubscriberDao) {
        this.notificationSubscriberDao = notificationSubscriberDao;
    }

    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    public void setPortalUserDao(PortalUserDao portalUserDao) {
        this.portalUserDao = portalUserDao;
    }


	public UserModel getUserModel() {
		return userModel;
	}


	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}
}
