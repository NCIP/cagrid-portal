package gov.nih.nci.cagrid.browser.tree.node;

import gov.nih.nci.cagrid.browser.tree.util.TreeUtil;
import gov.nih.nci.cagrid.common.servicedata.DomainObjectType;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 20, 2006
 * Time: 1:16:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AssociationTreeNode extends NonLeafTreeNode {

    public AssociationTreeNode(DomainObjectType associationObject, NonLeafTreeNode parent) {
        super(TreeUtil.ASSOCIATION_VALUE_NODE_TYPE, associationObject.getLongName(), parent, associationObject);

        TreeUtil.loadAttributes(this, associationObject);
    }

}
