package gov.nih.nci.cagrid.browser.tree.node;

import gov.nih.nci.cagrid.browser.tree.util.TreeUtil;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;

import org.apache.myfaces.custom.tree2.TreeNode;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 20, 2006
 * Time: 12:10:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class AttributeTreeNode extends NonLeafTreeNode {

    private TreeNode valueNode;


    public AttributeTreeNode(UMLAttribute attribute, NonLeafTreeNode parent) {
        super(TreeUtil.ATTRIBUTE_NODE_TYPE, attribute.getName(), parent, attribute);

        /** Value node encapsulates the value entered in the tree
         * attribute text field
         *
         */
        this.setValueNode(new ValueTreeNode(this));

    }


    public TreeNode getValueNode() {
        return valueNode;
    }

    public void setValueNode(TreeNode valueNode) {
        this.getChildren().add(valueNode);
        this.valueNode = valueNode;
    }


}
