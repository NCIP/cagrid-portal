package gov.nih.nci.cagrid.browser.tree.node;


import gov.nih.nci.cagrid.browser.tree.util.TreeUtil;
import gov.nih.nci.cagrid.common.servicedata.DomainObjectType_attributesList_attribute;
import org.apache.myfaces.custom.tree2.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 21, 2006
 * Time: 11:49:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class ValueTreeNode extends Observable implements TreeNode, Comparable {

    public static final String VALUE_FIELD_DELIM = ",";

    String valueField;
    NonLeafTreeNode parent;
    NonLeafTreeNode root;
    DomainObjectType_attributesList_attribute attribute;

    private String description = "node";
    private String type = TreeUtil.ATTRIBUTE_VALUE_NODE_TYPE;
    private boolean leaf = true; //default value is true
    private ArrayList children = new ArrayList();
    private boolean joinCondition = false;


    public ValueTreeNode(NonLeafTreeNode parent) {
        this.setParent(parent);

        /** Retreive root node and add its observers if any
         *
         */
        root = parent;
        while (!root.isRoot()) root = root.getParent();
        this.attribute = (DomainObjectType_attributesList_attribute)((AttributeTreeNode) parent).getObject();

        this.addObserver(((TargetTreeNode) root).getTreeObserver());
    }

    public DomainObjectType_attributesList_attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(DomainObjectType_attributesList_attribute attribute) {
        this.attribute = attribute;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
        setChanged();
        notifyObservers();

    }

    public NonLeafTreeNode getRoot() {
        return root;
    }

    public void setRoot(TargetTreeNode root) {
        this.root = root;
    }

    public NonLeafTreeNode getParent() {
        return parent;
    }

    public void setParent(NonLeafTreeNode parent) {
        this.parent = parent;
    }

    /**
     * Value node is leaf. ALways will return true
     *
     * @return
     */
    public boolean isLeaf() {
        return true;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public List getChildren() {
        return children;
    }

    /**
     * Gets the type of {@link org.apache.myfaces.custom.tree2.TreeNode}.
     *
     * @return The node type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Sets the type of {@link org.apache.myfaces.custom.tree2.TreeNode}.
     *
     * @param type The node type
     */
    public void setType(String type) {
        this.type = type;
    }

    public boolean isJoinCondition() {
        return joinCondition;
    }

    public void setJoinCondition(boolean joinCondition) {
        this.joinCondition = joinCondition;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the identifier associated with the {@link org.apache.myfaces.custom.tree2.TreeNode}.
     *
     * @param identifier The identifier
     */
    public void setIdentifier(String identifier) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Gets the identifier asociated with the {@link org.apache.myfaces.custom.tree2.TreeNode}.
     *
     * @return the identifier
     */
    public String getIdentifier() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Gets the number of children this node has.
     *
     * @return the number of children
     */
    public int getChildCount() {
        return children.size();
    }

    public int compareTo(Object obj) {
        // branches come before leaves, after this criteria nodes are sorted alphabetically
        TreeNode otherNode = (TreeNode) obj;

        if (isLeaf() && !otherNode.isLeaf()) {
            // leaves come after branches
            return 1;
        } else if (!isLeaf() && otherNode.isLeaf()) {
            // branches come before leaves
            return -1;
        } else {
            // both nodes are leaves or both node are branches, so compare the descriptions
            return getDescription().compareTo(otherNode.getDescription());
        }

    }
}

