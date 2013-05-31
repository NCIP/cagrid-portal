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
package gov.nih.nci.cagrid.portal.notification;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class VelocityNotificationGeneratorTest extends AbstractNotificationTest {


    @Test
    public void sendCtx() {

        // lets test the velocity view
        VelocityNotificationGenerator generator = (VelocityNotificationGenerator) ctx.getBean("serviceStatusChangeGenerator");
        assertNotNull("Could not create velocity based notification generator", generator);
        try {
            generator.getMessage(sub, null);

        } catch (Exception e) {
            fail("Should not fail to generate view with null model");
        }

        Map model = mock(HashMap.class);
        try {
            NotificationMessage msg = generator.getMessage(sub, model);
            MimeMessage mailMsg = mock(MimeMessage.class);
            msg.prepare(mailMsg);


        } catch (Exception e) {
            fail("Should not fail to generate view with valid model " + e.getMessage());
        }

    }

}
