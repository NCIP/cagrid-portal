package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SelectAggregateTargetsController extends
        AbstractQueryActionController {

    protected void doHandleAction(ActionRequest request, ActionResponse response, Object obj, BindException errors) throws Exception {
        //add targets to query
    }
}
