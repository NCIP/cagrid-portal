package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal.dao.UMLClassDao;

import javax.portlet.RenderRequest;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ViewAggregateTargetsController extends AbstractQueryRenderController {


    private ForeignTargetsProvider targetsProvider;
    private TreeFacade cqlQueryTreeFacade;

    private UMLClassDao umlClassDao;
    private Log log = LogFactory.getLog(ViewAggregateTargetsController.class);


    @Override
    protected Object getObject(RenderRequest request) {
        CQLQueryBean cqlQueryBean = (CQLQueryBean) getCqlQueryTreeFacade().getRootNode().getContent();
        AggregateTargetsCommand aggregateTargetsCmd = cqlQueryBean.getAggregateTargets();

        if (aggregateTargetsCmd == null) {
            aggregateTargetsCmd = new AggregateTargetsCommand();
            log.debug("No Available aggregate targets. Refreshing from database");
            aggregateTargetsCmd.setAvailable(targetsProvider.getSemanticallyEquivalentClasses(getQueryModel().getSelectedUmlClass()));
            cqlQueryBean.setAggregateTargets(aggregateTargetsCmd);
        }

        return aggregateTargetsCmd;
    }

    @Override
    protected void addData(RenderRequest request, ModelAndView mav) {
        mav.addObject("primary", getQueryModel().getSelectedUmlClass());
    }

    public UMLClassDao getUmlClassDao() {
        return umlClassDao;
    }

    public void setUmlClassDao(UMLClassDao umlClassDao) {
        this.umlClassDao = umlClassDao;
    }

    public ForeignTargetsProvider getTargetsProvider() {
        return targetsProvider;
    }

    public void setTargetsProvider(ForeignTargetsProvider targetsProvider) {
        this.targetsProvider = targetsProvider;
    }

    @Required
    public TreeFacade getCqlQueryTreeFacade() {
        return cqlQueryTreeFacade;
    }

    public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
        this.cqlQueryTreeFacade = cqlQueryTreeFacade;
    }
}
