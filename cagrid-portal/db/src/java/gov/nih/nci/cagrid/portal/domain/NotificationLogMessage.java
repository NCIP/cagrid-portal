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
package gov.nih.nci.cagrid.portal.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Entity
@Table(name = "notification_log")
@GenericGenerator(name = "id-generator", strategy = "native",
        parameters = {
                @Parameter(name = "sequence", value = "seq_noti_log")
        }
)
public class NotificationLogMessage extends AbstractDomainObject {

    private String notificationType;
    private NotificationSubscriber subscriber;
    private Date timestamp;
    private boolean sendSuccessful;


    public NotificationLogMessage() {
    }

    public NotificationLogMessage(NotificationSubscriber subscriber, String notificationType) {
        this.subscriber = subscriber;
        this.notificationType = notificationType;
        this.timestamp = new Date();
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    @OneToOne
    @JoinColumn(name = "subscriber_id")
    public NotificationSubscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(NotificationSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSendSuccessful() {
        return sendSuccessful;
    }

    public void setSendSuccessful(boolean sendSuccessful) {
        this.sendSuccessful = sendSuccessful;
    }
}
