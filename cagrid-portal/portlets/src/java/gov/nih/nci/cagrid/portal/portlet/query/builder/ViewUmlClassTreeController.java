/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.UMLClassBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.UMLClassTreeNodeListener;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;
import org.springframework.web.portlet.ModelAndView;

import javax.portlet.RenderRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class ViewUmlClassTreeController extends
        AbstractQueryRenderController {

    private TreeFacade umlClassTreeFacade;
    private UMLClassTreeNodeListener umlClassTreeNodeListener;

    private UMLClassDao umlClassDao;

    private List<String> predicates = new ArrayList<String>();

    /**
     *
     */
    public ViewUmlClassTreeController() {

    }

    /* (non-Javadoc)
      * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
      */
    @Override
    protected Object getObject(RenderRequest request) {
        TreeNode rootNode = null;
        UMLClass umlClass = getRootUmlClass();
        if (umlClass == null) {
            logger.debug("no UMLClass selected");
        } else {
            logger.debug("UMLClass:" + umlClass.getId() + " selected");

            //Need to associate with current session
            rootNode = getUmlClassTreeFacade().getRootNode();
            if (rootNode == null || !umlClass.getId().equals(((UMLClassBean) rootNode.getContent()).getUmlClass().getId())) {
                logger.debug("Creating new tree for UMLClass:" + umlClass.getId());
                rootNode = createNode(umlClass, null);
            }
            getUmlClassTreeFacade().setRootNode(rootNode);
        }
        return rootNode;
    }

    protected TreeNode createNode(UMLClass umlClass, TreeNode parent) {
        TreeNode node = new TreeNode(parent, "UMLClass:" + umlClass.getId());
        node.setLabel(umlClass.getClassName());
        node.setContent(new UMLClassBean(umlClass));
        getUmlClassTreeNodeListener().onOpen(node, new HashMap());
        return node;
    }


    protected UMLClass getRootUmlClass() {
        UMLClass umlClass = getQueryModel().getSelectedUmlClass();

        if (umlClass != null) {
            umlClass = getUmlClassDao().getById(umlClass.getId());
        }

        return umlClass;

    }

    /**
     * Needed by foreign associations. But need to load
     * only once
     */
    @Override
    protected void addData(RenderRequest request, ModelAndView mav) {
        logger.debug("Loading predicates list of length" + predicates.size());
        mav.addObject("predicates", getPredicates());
    }


    public UMLClassDao getUmlClassDao() {
        return umlClassDao;
    }

    public void setUmlClassDao(UMLClassDao umlClassDao) {
        this.umlClassDao = umlClassDao;
    }

    public TreeFacade getUmlClassTreeFacade() {
        return umlClassTreeFacade;
    }

    public void setUmlClassTreeFacade(TreeFacade umlClassTreeFacade) {
        this.umlClassTreeFacade = umlClassTreeFacade;
    }

    public UMLClassTreeNodeListener getUmlClassTreeNodeListener() {
        return umlClassTreeNodeListener;
    }

    public void setUmlClassTreeNodeListener(
            UMLClassTreeNodeListener umlClassTreeNodeListener) {
        this.umlClassTreeNodeListener = umlClassTreeNodeListener;
    }

    public List<String> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<String> predicates) {
        this.predicates = predicates;
    }
}
