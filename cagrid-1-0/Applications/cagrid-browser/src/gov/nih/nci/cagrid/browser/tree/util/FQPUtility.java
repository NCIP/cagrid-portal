package gov.nih.nci.cagrid.browser.tree.util;

import gov.nih.nci.cagrid.browser.tree.query.ForeignAssociationNodeCollection;
import gov.nih.nci.cagrid.data.client.query.CaBIGXMLQuery;
import gov.nih.nci.cagrid.data.federatedQuery.client.ForeignNestedCriteria;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Utility for forming FQP documents
 *
 *  Created by IntelliJ IDEA.
 * User: kherm
 * Date: Apr 5, 2006
 * Time: 4:18:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class FQPUtility extends QueryUtility {

    /**
     * Will nest associations from a ForeignAssociationNode collection into a nestedCriteria object
     * @param nestedCriteria
     * @param associations
     */
    public static void nestForeignAssociations(CaBIGXMLQuery nestedCriteria, ForeignAssociationNodeCollection associations, String joinCondition) {

        Set associationSet = associations.entrySet();
        for (Iterator associationIter = associationSet.iterator(); associationIter.hasNext();) {
            Map.Entry associationEntry = (Map.Entry) associationIter.next();
            ForeignAssociationNodeCollection.ForeignAssociationNode association = (ForeignAssociationNodeCollection.ForeignAssociationNode) associationEntry.getValue();

            ForeignNestedCriteria foreignAssociationCriteria = new ForeignNestedCriteria(association.getForeignAssociationClassname());
            foreignAssociationCriteria.setServiceURL(association.getServiceURL());
            //foreignAssociationCriteria.setJoinCondition("gov.nih.nci.cabio.domain.Gene.symbol=edu.georgetown.pir.domain.Gene.name");
            foreignAssociationCriteria.setJoinCondition(joinCondition);

            QueryUtility.nestAttributes(foreignAssociationCriteria, association.getAttributeNodeCollection());

            nestedCriteria.addCaBIGNestedCriteria(foreignAssociationCriteria);
        }
    }

}
