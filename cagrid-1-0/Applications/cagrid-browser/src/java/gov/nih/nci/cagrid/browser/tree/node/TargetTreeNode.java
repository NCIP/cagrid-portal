package gov.nih.nci.cagrid.browser.tree.node;

import gov.nih.nci.cagrid.browser.beans.GSH;
import gov.nih.nci.cagrid.browser.tree.util.TreeUtil;
import gov.nih.nci.cagrid.common.servicedata.DomainObjectType;

import java.util.Observer;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 20, 2006
 * Time: 12:16:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class TargetTreeNode extends NonLeafTreeNode {

    private Observer treeObserver;
    private GSH gsh;
    String targetClassname;


    /**
     * @param targetObject
     */
    public TargetTreeNode(GSH targetGSH, DomainObjectType targetObject) {
        super(TreeUtil.TARGET_NODE_TYPE, targetObject.getFullName().getClassName(), null,targetObject);
        //@todo check if you need all details in the constructor or use a different one
        this.gsh = targetGSH;
        this.targetClassname = targetObject.getFullName().getPackageName() + "." + targetObject.getFullName().getClassName();
    }


    public String getTargetClassname() {
        return targetClassname;
    }

    public void setTargetClassname(String targetClassname) {
        this.targetClassname = targetClassname;
    }

    public NonLeafTreeNode getParent() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setParent(NonLeafTreeNode parent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isRoot() {
        return true;
    }

    public Observer getTreeObserver() {
        return treeObserver;
    }

    public void setTreeObserver(Observer treeObserver) {
        this.treeObserver = treeObserver;
    }

    public GSH getGsh() {
        return gsh;
    }

    public void setGsh(GSH gsh) {
        this.gsh = gsh;
    }

}

