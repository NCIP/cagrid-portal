package gov.nih.nci.cagrid.browser.tree.util;

import edu.duke.cabig.catrip.gui.common.ClassBean;
import edu.duke.cabig.catrip.gui.discovery.DomainModelMetaDataRegistry;
import gov.nih.nci.cagrid.browser.tree.node.AssociationTreeNode;
import gov.nih.nci.cagrid.browser.tree.node.AttributeTreeNode;
import gov.nih.nci.cagrid.browser.tree.node.NonLeafTreeNode;
import gov.nih.nci.cagrid.browser.tree.node.ValueTreeNode;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import org.apache.axis.types.NonNegativeInteger;

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

    public static UMLClass getObjectFromModel(DomainModel objectModel, NonNegativeInteger objectID) {

        UMLClass[] objs = objectModel.getExposedUMLClassCollection().getUMLClass();

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
    public static void loadAttributes(NonLeafTreeNode treeNode, UMLClass obj) {
	UMLAttribute[] attrList = obj.getUmlAttributeCollection().getUMLAttribute();
        for (int i = 0; i < attrList.length; i++) {
            UMLAttribute attribute = attrList[i];
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
    public static void loadAssociations(NonLeafTreeNode treeNode, UMLClass obj, DomainModel objectModel) {
	//
	UMLAssociation[] associationList = null;
	ClassBean cb = DomainModelMetaDataRegistry.lookupClassByFullyQualifiedName(obj.getPackageName() + "." + obj.getClassName());
	//TODO: get associationList;

        for (int i = 0; i < associationList.length; i++) {
            NonNegativeInteger objectID = associationList[i].getTarget().getUMLClassRef().getId();
            UMLClass associationObj = TreeUtil.getObjectFromModel(objectModel, objectID);
            if (associationObj != null) {
                AssociationTreeNode associationNode = new AssociationTreeNode(associationObj, treeNode);
                treeNode.getChildren().add(associationNode);
            }
        }


    }

    /**
     * //todo merge these two methods by converting ForeginObjectNode and AsociationNode into common interface
     * public static void loadForeignAssociations(BaseTreeNode treeNode, GSH foreignGSH, DomainModel foreignModel, UMLClass foreignObject) {
     * UMLClass_associationsList_associationElement[] associationList = foreignObject.getAssociationsList().getAssociationElement();
     * <p/>
     * for (int i = 0; i < associationList.length; i++) {
     * NonNegativeInteger objectID = associationList[i].getTarget().getUMLClassRef().getId();
     * UMLClass associationObj = TreeUtil.getObjectFromModel(foreignModel, objectID);
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
        UMLClass_attributesList_attribute attribute = (UMLClass_attributesList_attribute)attributeNode.getObject();


        UMLClass association = (UMLClass)((NonLeafTreeNode)attributeNode.getParent()).getObject();

        return association.getFullName().getPackageName() + "." +  association.getFullName().getClassName() + "." +  attribute.getName();
    }

}
