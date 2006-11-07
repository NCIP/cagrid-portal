package gov.nih.nci.cagrid.browser.tree.node;

import gov.nih.nci.cagrid.browser.beans.GSH;
import gov.nih.nci.cagrid.browser.tree.util.TreeUtil;
import gov.nih.nci.cagrid.common.servicedata.DomainObjectType;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 23, 2006
 * Time: 12:00:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForeignObjectNode extends NonLeafTreeNode {
    private GSH gsh;

    public ForeignObjectNode(GSH foreignGSH, DomainObjectType foreignObject, NonLeafTreeNode parent) {
        super(TreeUtil.FOREIGN_OBJECT_NODE, foreignObject.getLongName(), parent, foreignObject);
        this.gsh = foreignGSH;

        TreeUtil.loadAttributes(this, foreignObject);
    }

    public GSH getGsh() {
        return gsh;
    }

    public void setGsh(GSH gsh) {
        this.gsh = gsh;
    }
}
