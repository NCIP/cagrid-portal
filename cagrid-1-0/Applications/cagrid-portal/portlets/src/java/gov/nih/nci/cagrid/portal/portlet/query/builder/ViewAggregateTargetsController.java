package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.dao.UMLClassDao;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;

import org.springframework.validation.BindException;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ViewAggregateTargetsController extends AbstractQueryRenderController {


    private ForeignTargetsProvider targetsProvider;
    private UMLClassDao umlClassDao;

    @Override
    protected Object getObject(RenderRequest request) {
        return targetsProvider.getSemanticallyEquivalentClasses(getQueryModel().getSelectedUmlClass());
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
}
