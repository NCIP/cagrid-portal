package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.QueryConstants;
import gov.nih.nci.cagrid.portal.portlet.query.cql.UMLClassBean;
import gov.nih.nci.cagrid.portal.portlet.query.dcql.ForeignUMLClassBean;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;

import javax.portlet.RenderRequest;
import java.util.HashMap;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */

public class PopulateForeignAssociationsController extends
        ViewUmlClassTreeController {


    private ForeignTargetsProvider targetsProvider;
    private UMLClassDao umlClassDao;

    public PopulateForeignAssociationsController() {
    }


    @Override
    protected Object getObject(RenderRequest request) {
        TreeNode rootNode = (TreeNode) super.getObject(request);
        TreeNode node = rootNode.find(request.getParameter("path"));

        for (TreeNode n : node.getChildren()) {
            if (n.getName().startsWith(QueryConstants.FOREIGN_UML_CLASS_PREFIX)) {
                return rootNode;
            }
        }
        UMLClassBean umlClassBean = (UMLClassBean) node.getContent();

        if (node != null) {
        	UMLClass example = getUmlClassDao().getById(umlClassBean.getUmlClass().getId());
            List<UMLClass> classes = targetsProvider.getSemanticallyEquivalentClasses(example);
            for (UMLClass target : classes) {
                node.getChildren().add(createNode(target, node));
            }
        }
        getUmlClassTreeFacade().setRootNode(rootNode);
        return rootNode;
    }

    @Override
    protected TreeNode createNode(UMLClass umlClass, TreeNode parent) {
        TreeNode node = new TreeNode(parent, QueryConstants.FOREIGN_UML_CLASS_PREFIX + umlClass.getClassName());
        node.setLabel(umlClass.getClassName());
        UMLClassBean parentBean = (UMLClassBean) parent.getContent();
        node.setContent(new ForeignUMLClassBean(umlClass, parentBean.getAttributes().get(0)));
        getUmlClassTreeNodeListener().onOpen(node, new HashMap());
        return node;
    }


    public ForeignTargetsProvider getTargetsProvider() {
        return targetsProvider;
    }

    public void setTargetsProvider(ForeignTargetsProvider targetsProvider) {
        this.targetsProvider = targetsProvider;
    }


	public UMLClassDao getUmlClassDao() {
		return umlClassDao;
	}


	public void setUmlClassDao(UMLClassDao umlClassDao) {
		this.umlClassDao = umlClassDao;
	}


}
