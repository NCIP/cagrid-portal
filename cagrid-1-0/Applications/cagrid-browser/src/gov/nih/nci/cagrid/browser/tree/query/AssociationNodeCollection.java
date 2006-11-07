package gov.nih.nci.cagrid.browser.tree.query;

import gov.nih.nci.cagrid.browser.tree.node.AssociationTreeNode;
import gov.nih.nci.cagrid.browser.tree.node.ValueTreeNode;
import gov.nih.nci.cagrid.browser.tree.util.TreeUtil;
import gov.nih.nci.cagrid.common.servicedata.DomainObjectType;

import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 24, 2006
 * Time: 4:18:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class AssociationNodeCollection extends Hashtable {


    public void addValueNode(ValueTreeNode valueTreeNode) {

        String id = TreeUtil.getId(valueTreeNode);

        AssociationNode associationNode = (AssociationNode) get(id);
        if (associationNode == null) {
            associationNode = new AssociationNode(valueTreeNode);
            put(id, associationNode);
        } else //if association node already exists just add values
            associationNode.addValues(valueTreeNode);


    }


    public class AssociationNode {
        private AttributeNodeCollection attributeNodeCollection = new AttributeNodeCollection();
        private String associationClassname;

        public AssociationNode(ValueTreeNode value) {
            attributeNodeCollection.addValueNode(value);

            DomainObjectType tDomainObject = ((DomainObjectType) value.getParent().getParent().getObject());
            this.associationClassname = tDomainObject.getFullName().getPackageName() + "." + tDomainObject.getFullName().getClassName(); 
        }

        public void addValues(ValueTreeNode value) {
            attributeNodeCollection.addValueNode(value);
        }

        public String getAssociationClassname() {
            return associationClassname;
        }

        public void setAssociationClassname(String associationClassname) {
            this.associationClassname = associationClassname;
        }

        public AttributeNodeCollection getAttributeNodeCollection() {
            return attributeNodeCollection;
        }

        public void setAttributeNodeCollection(AttributeNodeCollection attributeNodeCollection) {
            this.attributeNodeCollection = attributeNodeCollection;
        }
    }


}
