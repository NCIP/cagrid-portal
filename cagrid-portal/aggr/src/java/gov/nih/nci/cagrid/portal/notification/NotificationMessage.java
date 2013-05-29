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

import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * Marker interface. Created to easily change message types
 * without affecting other classes.
 * To change message type, change this marker interface and the
 * following classes
 * <p/>
 * AbstractNotificationSender
 * AbstractNotificationGenerator
 * <p/>
 * <p/>
 * will accept this type.
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface NotificationMessage extends MimeMessagePreparator {
}
