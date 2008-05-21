package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SelectAggregateTargetsController extends
        AbstractQueryActionController {

    private TreeFacade cqlQueryTreeFacade;

    protected void doHandleAction(ActionRequest request, ActionResponse response, Object obj, BindException errors) throws Exception {
        AggregateTargetsCommand aggregateTargetsCmd = (AggregateTargetsCommand) obj;

        CQLQueryBean cqlQueryBean = (CQLQueryBean) getCqlQueryTreeFacade().getRootNode().getContent();
        //cqlQueryBean.setAggregateTargets(aggregateTargetsCmd);

        cqlQueryBean.getAggregateTargets().getSelected().clear();
        if (aggregateTargetsCmd.getSelected() != null)
            cqlQueryBean.getAggregateTargets().getSelected().addAll(aggregateTargetsCmd.getSelected());
    }

    @Required
    public TreeFacade getCqlQueryTreeFacade() {
        return cqlQueryTreeFacade;
    }

    public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
        this.cqlQueryTreeFacade = cqlQueryTreeFacade;
    }
}



