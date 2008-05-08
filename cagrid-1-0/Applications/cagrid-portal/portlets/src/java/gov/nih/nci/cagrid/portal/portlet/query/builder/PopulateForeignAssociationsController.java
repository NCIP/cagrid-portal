package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;

import javax.portlet.RenderRequest;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */

public class PopulateForeignAssociationsController extends
        ViewUmlClassTreeController {

    private ForeignTargetsProvider targetsProvider;

    public PopulateForeignAssociationsController() {
    }


    @Override
    protected Object getObject(RenderRequest request) {
        TreeNode rootNode = (TreeNode) super.getObject(request);
        if (rootNode != null) {
            List<UMLClass> classes = targetsProvider.getSemanticallyEquivalentClasses(getRootUmlClass());
            for (UMLClass target : classes) {
                rootNode.getChildren().add(createNode(target, rootNode));
            }
        }
        return rootNode;
    }


    public ForeignTargetsProvider getTargetsProvider() {
        return targetsProvider;
    }

    public void setTargetsProvider(ForeignTargetsProvider targetsProvider) {
        this.targetsProvider = targetsProvider;
    }
}
