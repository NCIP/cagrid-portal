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
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class ExportToXmlController extends AbstractQueryActionController {

    private TreeFacade cqlQueryTreeFacade;

    /**
     *
     */
    public ExportToXmlController() {

    }

    /**
     * @param commandClass
     */
    public ExportToXmlController(Class commandClass) {
        super(commandClass);

    }

    /**
     * @param commandClass
     * @param commandName
     */
    public ExportToXmlController(Class commandClass, String commandName) {
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
        CQLQueryCommand command = (CQLQueryCommand) obj;
        CQLQueryBean cqlQueryBean = (CQLQueryBean) getCqlQueryTreeFacade().getRootNode().getContent();
        command.setCqlQuery(cqlQueryBean.toXml());
        command.setDataServiceUrl(getUserModel().getSelectedService().getUrl());
    }

    protected Object getCommand(PortletRequest request) throws Exception {
        CQLQueryCommand command = getUserModel().getWorkingQuery();
        if (command == null) {
            command = new CQLQueryCommand();
            getUserModel().setWorkingQuery(command);
        }
        return command;
    }

    @Required
    public TreeFacade getCqlQueryTreeFacade() {
        return cqlQueryTreeFacade;
    }

    public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
        this.cqlQueryTreeFacade = cqlQueryTreeFacade;
    }

}
