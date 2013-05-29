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

import gov.nih.nci.cagrid.portal.PortalSystemException;
import gov.nih.nci.cagrid.portal.domain.NotificationSubscriber;
import org.springframework.context.ApplicationEvent;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface NotificationSender<T extends ApplicationEvent> {
    void sendMessage(NotificationSubscriber sub, T e) throws PortalSystemException;

    void scheduleSend(NotificationMessage nm) throws PortalSystemException;

}
