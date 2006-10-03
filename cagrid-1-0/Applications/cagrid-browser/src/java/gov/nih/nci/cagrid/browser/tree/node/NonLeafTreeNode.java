package gov.nih.nci.cagrid.browser.tree.node;

import org.apache.myfaces.custom.tree2.TreeNodeBase;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 22, 2006
 * Time: 5:22:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class NonLeafTreeNode extends TreeNodeBase {

    NonLeafTreeNode parent;
    Object object;

    protected NonLeafTreeNode(String type, String identifier, NonLeafTreeNode parent, Object object) {
        super(type, identifier, identifier, false);
        this.parent = parent;
        this.object = object;
    }


    public NonLeafTreeNode getParent() {
        return parent;
    }

    public void setParent(NonLeafTreeNode parent) {
        this.parent = parent;
    }


    /**
     * If this is a root (Target) node. Only Target node is root
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * the Object that this node contains
     * @return
     */
    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
