package gov.nih.nci.cagrid.browser.tree.query;

import gov.nih.nci.cagrid.browser.tree.node.ForeignObjectNode;
import gov.nih.nci.cagrid.browser.tree.node.ValueTreeNode;
import gov.nih.nci.cagrid.browser.tree.util.TreeUtil;
import gov.nih.nci.cagrid.common.servicedata.DomainObjectType;

import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Apr 5, 2006
 * Time: 11:16:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class ForeignAssociationNodeCollection extends Hashtable {

    public void addValueNode(ValueTreeNode valueTreeNode) {
        String id = TreeUtil.getId(valueTreeNode);
        String gsh = ((ForeignObjectNode) (valueTreeNode.getParent().getParent())).getGsh().getURL();

        ForeignAssociationNode foreignAssociationNode = (ForeignAssociationNode) get(id);
        if (foreignAssociationNode == null) {
            foreignAssociationNode = new ForeignAssociationNode(valueTreeNode);
            put(id, foreignAssociationNode);
        } else
            foreignAssociationNode.addValues(valueTreeNode);
    }

    public class ForeignAssociationNode {
        private AttributeNodeCollection attributeNodeCollection = new AttributeNodeCollection();
        private String foreignAssociationClassname;
        private String serviceURL;

        public ForeignAssociationNode(ValueTreeNode value) {
            attributeNodeCollection.addValueNode(value);
            DomainObjectType tForeignObject = (DomainObjectType)((ForeignObjectNode) value.getParent().getParent()).getObject();
            this.foreignAssociationClassname = tForeignObject.getFullName().getPackageName() + "." + tForeignObject.getFullName().getClassName();

            this.serviceURL = ((ForeignObjectNode) value.getParent().getParent()).getGsh().getURL();
        }

        public String getServiceURL() {
            return serviceURL;
        }

        public void setServiceURL(String serviceURL) {
            this.serviceURL = serviceURL;
        }

        public void addValues(ValueTreeNode value) {
            attributeNodeCollection.addValueNode(value);
        }

        public String getForeignAssociationClassname() {
            return foreignAssociationClassname;
        }

        public void setForeignAssociationClassname(String foreignAssociationClassname) {
            this.foreignAssociationClassname = foreignAssociationClassname;
        }

        public AttributeNodeCollection getAttributeNodeCollection() {
            return attributeNodeCollection;
        }

        public void setAttributeNodeCollection(AttributeNodeCollection attributeNodeCollection) {
            this.attributeNodeCollection = attributeNodeCollection;
        }
    }

}
