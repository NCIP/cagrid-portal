package gov.nih.nci.cagrid.browser.tree.query;

import gov.nih.nci.cagrid.browser.tree.node.AttributeTreeNode;
import gov.nih.nci.cagrid.browser.tree.node.ValueTreeNode;
import gov.nih.nci.cagrid.browser.tree.util.TreeUtil;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 24, 2006
 * Time: 4:07:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class AttributeNodeCollection extends Hashtable {


    protected void addValueNode(ValueTreeNode valueTreeNode) {
        AttributeTreeNode attributeTreeNode = (AttributeTreeNode) valueTreeNode.getParent();
        String id = attributeTreeNode.getIdentifier();

        AttributeNode attributeNode = new AttributeNode(valueTreeNode);
        put(id, attributeNode);


    }


    public class AttributeNode {

        private ArrayList values = new ArrayList();
        private String attributeName;

        public AttributeNode(ValueTreeNode valueTreeNode) {
            values.addAll(TreeUtil.parseValueNode(valueTreeNode));
            this.attributeName = valueTreeNode.getAttribute().getName();
        }

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public ArrayList getValues() {
            return values;
        }

        public void setValues(ArrayList values) {
            this.values = values;
        }

    }

}
