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
