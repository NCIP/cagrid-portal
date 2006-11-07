package gov.nih.nci.cagrid.browser.tree.util;

import gov.nih.nci.cagrid.browser.tree.node.*;
import gov.nih.nci.cagrid.common.servicedata.DomainModelType;
import gov.nih.nci.cagrid.common.servicedata.DomainObjectType;
import gov.nih.nci.cagrid.common.servicedata.DomainObjectType_associationsList_associationElement;
import gov.nih.nci.cagrid.common.servicedata.DomainObjectType_attributesList_attribute;
import org.apache.axis.types.NonNegativeInteger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 20, 2006
 * Time: 2:25:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class TreeUtil {

    public static final String TARGET_NODE_TYPE = "targetNode";
    public static final String ATTRIBUTE_NODE_TYPE = "attributeNode";
    public static final String ATTRIBUTE_VALUE_NODE_TYPE = "valueNode";
    public static final String ASSOCIATION_VALUE_NODE_TYPE = "associationNode";
    public static final String FOREIGN_OBJECT_NODE = "foreignObject";


    /**
     * @param objectModel caDSR object model
     * @param objectID    object id to be retreived
     * @return Object
     */

    public static DomainObjectType getObjectFromModel(DomainModelType objectModel, NonNegativeInteger objectID) {

        DomainObjectType[] objs = objectModel.getDomainObject();

        for (int i = 0; i < objs.length; i++) {
            if (objectID.equals(objs[i].getId())) {
                return objs[i];
            }
        }

        return null;
    }

    /**
     * load attributes for an object into the tree node
     *
     * @param treeNode
     * @param obj
     */
    public static void loadAttributes(NonLeafTreeNode treeNode, DomainObjectType obj) {
        /** Add attributes of the object as leaf nodes **/
        DomainObjectType_attributesList_attribute[] attrList = obj.getAttributesList().getAttribute();
        for (int i = 0; i < attrList.length; i++) {
            DomainObjectType_attributesList_attribute attribute = attrList[i];
            AttributeTreeNode attrNode = new AttributeTreeNode(attribute, treeNode);

            /** Add to target node **/
            treeNode.getChildren().add(attrNode);
        }
    }

    /**
     * Load object associations into the tree
     *
     * @param treeNode
     * @param obj
     * @param objectModel
     */
    public static void loadAssociations(NonLeafTreeNode treeNode, DomainObjectType obj, DomainModelType objectModel) {
        DomainObjectType_associationsList_associationElement[] associationList = obj.getAssociationsList().getAssociationElement();

        for (int i = 0; i < associationList.length; i++) {
            NonNegativeInteger objectID = associationList[i].getTarget().getDomainObjectRef().getId();
            DomainObjectType associationObj = TreeUtil.getObjectFromModel(objectModel, objectID);
            if (associationObj != null) {
                AssociationTreeNode associationNode = new AssociationTreeNode(associationObj, treeNode);
                treeNode.getChildren().add(associationNode);
            }
        }


    }

    /**
     * //todo merge these two methods by converting ForeginObjectNode and AsociationNode into common interface
     * public static void loadForeignAssociations(BaseTreeNode treeNode, GSH foreignGSH, DomainModelType foreignModel, DomainObjectType foreignObject) {
     * DomainObjectType_associationsList_associationElement[] associationList = foreignObject.getAssociationsList().getAssociationElement();
     * <p/>
     * for (int i = 0; i < associationList.length; i++) {
     * NonNegativeInteger objectID = associationList[i].getTarget().getDomainObjectRef().getId();
     * DomainObjectType associationObj = TreeUtil.getObjectFromModel(foreignModel, objectID);
     * if (associationObj != null) {
     * ForeignObjectNode associationNode = new ForeignObjectNode(foreignGSH, associationObj, treeNode);
     * treeNode.getChildren().add(associationNode);
     * }
     * }
     * <p/>
     * <p/>
     * }
     */

    public static Collection parseValueNode(ValueTreeNode valueNode) {
        Collection returnValues = new ArrayList();
        String valueField = valueNode.getValueField().trim();
        StringTokenizer valueTokenizer = new StringTokenizer(valueField, ValueTreeNode.VALUE_FIELD_DELIM);

        while (valueTokenizer.hasMoreTokens()) {
            returnValues.add(valueTokenizer.nextToken());
        }

        return returnValues;
    }

    public static boolean valueBelongsToTarget(ValueTreeNode valueNode) {
        return (valueNode.getParent().getParent().equals(valueNode.getRoot()));
    }

    public static boolean valueBelongsToAssociation(ValueTreeNode valueNode) {
        return (valueNode.getParent().getParent() instanceof AssociationTreeNode);
    }

    public static boolean valueBelongsToForeignAssociation(ValueTreeNode valueNode) {
        return (valueNode.getParent().getParent() instanceof ForeignObjectNode);
    }

    public static String getId(ValueTreeNode valueNode) {
        return valueNode.getParent().getParent().getIdentifier();
    }

    /**
     * Gets the complete CDE name from the valueField
     * @param valueTreeNode
     * @return
     */
    public static String getCDEString(ValueTreeNode valueTreeNode){
        AttributeTreeNode attributeNode = (AttributeTreeNode)valueTreeNode.getParent();
        DomainObjectType_attributesList_attribute attribute = (DomainObjectType_attributesList_attribute)attributeNode.getObject();


        DomainObjectType association = (DomainObjectType)((NonLeafTreeNode)attributeNode.getParent()).getObject();

        return association.getFullName().getPackageName() + "." +  association.getFullName().getClassName() + "." +  attribute.getName();
    }

}
