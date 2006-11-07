package gov.nih.nci.cagrid.browser.tree.util;

import gov.nih.nci.cagrid.browser.tree.query.AssociationNodeCollection;
import gov.nih.nci.cagrid.browser.tree.query.AttributeNodeCollection;
import gov.nih.nci.cagrid.data.activity.CaBIGXMLQueryConstants;
import gov.nih.nci.cagrid.data.client.query.CaBIGCriterion;
import gov.nih.nci.cagrid.data.client.query.CaBIGNestedCriteria;
import gov.nih.nci.cagrid.data.client.query.CaBIGXMLQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for forming CQL and FQP quesries
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Apr 5, 2006
 * Time: 3:30:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class QueryUtility {

    /**
     * Will nest attributes from an Attribute Node into a nestedCriteria
     * @param nestedCriteria
     * @param attributeCollection
     */
    public static void nestAttributes(CaBIGNestedCriteria nestedCriteria, AttributeNodeCollection attributeCollection) {

        Set attrSet = attributeCollection.entrySet();
        for (Iterator iter = attrSet.iterator(); iter.hasNext();) {
            Map.Entry attrEntry = (Map.Entry) iter.next();

            AttributeNodeCollection.AttributeNode attrValues = (AttributeNodeCollection.AttributeNode) attrEntry.getValue();
            ArrayList values = attrValues.getValues();
            String attrName = attrValues.getAttributeName();

            for (Iterator valueIter = values.iterator(); valueIter.hasNext();) {
                CaBIGCriterion targetProp = new CaBIGCriterion(attrName, (String) valueIter.next(), CaBIGXMLQueryConstants.CONDITIONAL_EQUAL_TO);
                nestedCriteria.addCriterion(targetProp);
            }
        }
    }

    /**
     * Will nest associations from a Association Node collection into a nestedCriteria object
     * @param nestedCriteria
     * @param associations
     */
    public static void nestAssociations(CaBIGXMLQuery nestedCriteria, AssociationNodeCollection associations) {

        Set associationSet = associations.entrySet();
        for (Iterator associationIter = associationSet.iterator(); associationIter.hasNext();) {
            Map.Entry associationEntry = (Map.Entry) associationIter.next();
            AssociationNodeCollection.AssociationNode association = (AssociationNodeCollection.AssociationNode) associationEntry.getValue();

            CaBIGNestedCriteria associationCriteria = new CaBIGNestedCriteria(association.getAssociationClassname());
            QueryUtility.nestAttributes(associationCriteria, association.getAttributeNodeCollection());

            nestedCriteria.addCaBIGNestedCriteria(associationCriteria);
        }
    }
}
