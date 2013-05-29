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
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.NotificationSubscription;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.Participant;

import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.sql.SQLException;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class NotificationSubscriptionDao extends AbstractDao<NotificationSubscription>{

    public Class domainClass() {
        return NotificationSubscription.class;
    }

    public NotificationSubscription getSubscription(GridService service, PortalUser user){
        NotificationSubscription sub = null;

          List<NotificationSubscription> subs =  getHibernateTemplate().find(
				"from NotificationSubscription sub where sub.subscriber.portalUser.id = ? and sub.service.id = ?",
				new Object[] {
                        user.getId(),
                        service.getId()
                });


		if (subs.size() > 1) {
			throw new NonUniqueResultException("Found " + subs.size()
					+ " NotificationSubscription objects for user");
		} else if (subs.size() == 1) {
			sub = (NotificationSubscription) subs.get(0);
		}

        return sub;
    }
 

}
