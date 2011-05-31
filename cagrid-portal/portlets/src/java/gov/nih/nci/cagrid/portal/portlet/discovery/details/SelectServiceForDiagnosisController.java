/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.details;

import gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController;
import gov.nih.nci.cagrid.portal.portlet.InterPortletMessageSender;
import org.springframework.validation.BindException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 */
public class SelectServiceForDiagnosisController extends
        AbstractActionResponseHandlerCommandController {

    private InterPortletMessageSender interPortletMessageSender;

    /**
     *
     */
    public SelectServiceForDiagnosisController() {

    }

    /**
     * @param commandClass
     */
    public SelectServiceForDiagnosisController(Class commandClass) {
        super(commandClass);

    }

    /**
     * @param commandClass
     * @param commandName
     */
    public SelectServiceForDiagnosisController(Class commandClass,
                                               String commandName) {
        super(commandClass, commandName);

    }

    /*
      * (non-Javadoc)
      *
      * @see gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest,
      *      javax.portlet.ActionResponse, java.lang.Object,
      *      org.springframework.validation.BindException)
      */
    @Override
    protected void doHandleAction(ActionRequest request,
                                  ActionResponse response, Object obj, BindException errors)
            throws Exception {

        SelectDetailsCommand command = (SelectDetailsCommand) obj;
        getInterPortletMessageSender().send(request, command.getUrl());
    }

    public InterPortletMessageSender getInterPortletMessageSender() {
        return interPortletMessageSender;
    }

    public void setInterPortletMessageSender(
            InterPortletMessageSender interPortletMessageSender) {
        this.interPortletMessageSender = interPortletMessageSender;
    }

}
