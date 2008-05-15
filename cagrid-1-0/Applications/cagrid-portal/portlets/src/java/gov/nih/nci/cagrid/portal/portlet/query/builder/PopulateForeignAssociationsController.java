package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal.portlet.query.cql.UMLClassBean;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;

import javax.portlet.RenderRequest;
import java.util.List;
import java.util.HashMap;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */

public class PopulateForeignAssociationsController extends
        ViewUmlClassTreeController {

    private static String FOREIGN_UML_CLASS_PREFIX = "ForeignUMLClass:";


    private ForeignTargetsProvider targetsProvider;

    public PopulateForeignAssociationsController() {
    }


    @Override
    protected Object getObject(RenderRequest request) {
        TreeNode rootNode = (TreeNode) super.getObject(request);
        TreeNode node = rootNode.find(request.getParameter("path"));

        for (TreeNode n : node.getChildren()) {
            if (n.getName().startsWith(FOREIGN_UML_CLASS_PREFIX)) {
                return rootNode;
            }
        }
        UMLClassBean umlClassBean = (UMLClassBean) node.getContent();

        if (node != null) {
            List<UMLClass> classes = targetsProvider.getSemanticallyEquivalentClasses(umlClassBean.getUmlClass());
            for (UMLClass target : classes) {
                node.getChildren().add(createNode(target, node));
            }
        }
        getUmlClassTreeFacade().setRootNode(rootNode);
        return rootNode;
    }

    @Override
    protected TreeNode createNode(UMLClass umlClass, TreeNode parent) {
        TreeNode node = new TreeNode(parent, FOREIGN_UML_CLASS_PREFIX + umlClass.getId());
        node.setLabel(umlClass.getClassName());
        node.setContent(new UMLClassBean(umlClass));
        getUmlClassTreeNodeListener().onOpen(node, new HashMap());
        return node;
    }


    public ForeignTargetsProvider getTargetsProvider() {
        return targetsProvider;
    }

    public void setTargetsProvider(ForeignTargetsProvider targetsProvider) {
        this.targetsProvider = targetsProvider;
    }
}
