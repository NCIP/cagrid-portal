package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;
import org.springframework.beans.factory.annotation.Required;

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
        cqlQueryBean.getAggregateTargets().getSelected().add(getQueryModel().getSelectedService().getUrl());
    }

    @Required
    public TreeFacade getCqlQueryTreeFacade() {
        return cqlQueryTreeFacade;
    }

    public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
        this.cqlQueryTreeFacade = cqlQueryTreeFacade;
    }
}



