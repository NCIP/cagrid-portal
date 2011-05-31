package gov.nih.nci.cagrid.portal.notification;

import gov.nih.nci.cagrid.portal.aggr.status.ServiceStatusChangeEvent;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.NotificationSubscription;
import org.springframework.context.ApplicationEvent;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ServiceStatusChangeNotifier extends AbstractNotifier {
    private GridServiceDao gridServiceDao;

    @Transactional
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ServiceStatusChangeEvent) {
            try {
                ServiceStatusChangeEvent e = (ServiceStatusChangeEvent) event;

                GridService service = gridServiceDao.getByUrl(e.getServiceUrl());

                List<NotificationSubscription> subscriptions = service.getSubscriptions();
                for (NotificationSubscription sub : subscriptions) {
                    logger.debug("Sending notification for status change event");
                    getNotificationSender().sendMessage(sub.getSubscriber(), e);
                }

            } catch (Exception ex) {
                logger.error("Error scheduling notificaiton", ex);
            }

        }

    }

    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }
}
