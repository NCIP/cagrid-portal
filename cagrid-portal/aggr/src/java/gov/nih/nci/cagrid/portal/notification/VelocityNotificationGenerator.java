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
import org.apache.velocity.app.VelocityEngine;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.Map;

/**
 * Will generate notificiations from a velocity template
 * <p/>
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Transactional
public class VelocityNotificationGenerator extends AbstractNotificationGenerator {


    private VelocityEngine velocityEngine;
    private String view;


    public NotificationMessage getMessage(NotificationSubscriber sub, Map<String, Object> model) {
        String text = VelocityEngineUtils.mergeTemplateIntoString(
                velocityEngine, getView(), model);
        return super.getMessage(sub, model, text);
    }


    /**
     * Subclasses can override this message and set data
     * in the model
     *
     * @param model
     */
    public void bindData(Map model) {

    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
