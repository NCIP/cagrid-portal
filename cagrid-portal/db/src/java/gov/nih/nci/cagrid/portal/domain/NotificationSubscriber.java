package gov.nih.nci.cagrid.portal.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Entity
@Table(name = "notification_subscriber")
@GenericGenerator(name = "id-generator", strategy = "native",
        parameters = {
                @Parameter(name = "sequence", value = "seq_noti_subscriber")
        }
)
public class NotificationSubscriber extends AbstractDomainObject {

    private PortalUser portalUser;
    private boolean haltSubscriptions;

    @OneToOne
    @JoinColumn(name = "user_id")
    public PortalUser getPortalUser() {
        return portalUser;
    }

    public void setPortalUser(PortalUser portalUser) {
        this.portalUser = portalUser;
    }

    public boolean isHaltSubscriptions() {
        return haltSubscriptions;
    }

    public void setHaltSubscriptions(boolean haltSubscriptions) {
        this.haltSubscriptions = haltSubscriptions;
    }
}
