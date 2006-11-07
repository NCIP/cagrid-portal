package gov.nih.nci.cagrid.browser.tree.query;

import gov.nih.nci.cagrid.browser.tree.node.ValueTreeNode;
import gov.nih.nci.cagrid.browser.tree.util.TreeUtil;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 24, 2006
 * Time: 3:43:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class TargetNode {
    //private TargetTreeNode targetTreeNode;

    private AttributeNodeCollection attributes = new AttributeNodeCollection();
    private AssociationNodeCollection associations = new AssociationNodeCollection();
    private ForeignAssociationNodeCollection foreignAssociations = new ForeignAssociationNodeCollection();

    public AssociationNodeCollection getAssociations() {
        return associations;
    }

    public void setAssociations(AssociationNodeCollection associations) {
        this.associations = associations;
    }

    public AttributeNodeCollection getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributeNodeCollection attributes) {
        this.attributes = attributes;
    }

    public ForeignAssociationNodeCollection getForeignAssociations() {
        return foreignAssociations;
    }

    public void setForeignAssociations(ForeignAssociationNodeCollection foreignAssociations) {
        this.foreignAssociations = foreignAssociations;
    }

    public void addValueTreeNode(ValueTreeNode valueTreeNode) {
        //targetTreeNode = (TargetTreeNode)valueTreeNode.getRoot();

        /** If property belongs to target add to target node **/
        if (TreeUtil.valueBelongsToTarget(valueTreeNode)) {
            attributes.addValueNode(valueTreeNode);
        } else if (TreeUtil.valueBelongsToAssociation(valueTreeNode)) {
            associations.addValueNode(valueTreeNode);
        } else if (TreeUtil.valueBelongsToForeignAssociation(valueTreeNode)) {
            foreignAssociations.addValueNode(valueTreeNode);
        }

    }


}
