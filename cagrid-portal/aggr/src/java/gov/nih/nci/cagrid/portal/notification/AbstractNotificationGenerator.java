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

import gov.nih.nci.cagrid.portal.domain.NotificationSubscriber;
import gov.nih.nci.cagrid.portal.domain.Person;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * Base class for notification generators.  This abstracts the NotificaitonMessage
 * from subclasses. Changes to NotificationMessage interface can be handled here
 * by generating the appropriate NotificationMessage using external API's
 *
 * @author kherm manav.kher@semanticbits.com
 * @see VelocityNotificationGenerator
 *      <p/>
 *      User: kherm
 */
public abstract class AbstractNotificationGenerator {
    private String fromAddress;
    private String subject;

    /**
     * To be used by subclasses. This abstracts the message type
     * thus allowing the change of the actual Message impelentation
     * without affecting classes using subclassed message generator
     *
     * @param sub
     * @param model
     * @param text
     * @return
     */
    @Transactional
    protected NotificationMessage getMessage(final NotificationSubscriber sub, final Map<String, Object> model, final String text) {
        NotificationMessage preparator = new NotificationMessage() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                Person p = sub.getPortalUser().getPerson();
                message.setTo(p.getEmailAddress());
                message.setFrom(fromAddress);
                message.setSubject(subject);
                model.put("user", p);
                bindData(model);


                message.setText(text, true);
            }
        };

        return preparator;
    }

    /**
     * Subclasses can override this message and set data
     * in the model
     *
     * @param model
     */
    public void bindData(Map model) {

    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
