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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */

@Entity
@Table(name = "notification_subscriptions")
@GenericGenerator(name = "id-generator", strategy = "native",
        parameters = {
                @Parameter(name = "sequence", value = "seq_noti_subscriptions")
        }
)
public class NotificationSubscription extends AbstractDomainObject {

    private GridService service;
    private NotificationSubscriber subscriber;


    @ManyToOne
    @JoinColumn(name = "service_id")
    public GridService getService() {
        return service;
    }

    public void setService(GridService service) {
        this.service = service;
    }

    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    public NotificationSubscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(NotificationSubscriber subscriber) {
        this.subscriber = subscriber;
    }


}
