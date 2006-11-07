package gov.nih.nci.cagrid.browser.tree.query;

import gov.nih.nci.cagrid.browser.tree.exception.QueryWizardException;
import gov.nih.nci.cagrid.browser.tree.node.TargetTreeNode;
import gov.nih.nci.cagrid.browser.tree.node.ValueTreeNode;
import gov.nih.nci.cagrid.browser.tree.util.FQPUtility;
import gov.nih.nci.cagrid.browser.tree.util.QueryUtility;
import gov.nih.nci.cagrid.browser.tree.util.TreeUtil;
import gov.nih.nci.cagrid.data.client.query.CaBIGNestedCriteria;
import gov.nih.nci.cagrid.data.federatedQuery.client.CaBIGFederatedQueryPlan;

import java.util.Observable;
import java.util.Observer;


/**
 * This class translates a Query Wizard Tree into a CQL query
 * by attaching appropraite listeners to the Query Wizard
 * 
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 28, 2006
 * Time: 3:12:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class FQPTreeTranslator implements TreeTranslator {

    public final static String FQP_NAME_ATTRIBUTE = "FederatedQueryPlan";

    private TargetNode targetNode = new TargetNode();
    private TargetTreeNode targetTreeNode;
    private CaBIGFederatedQueryPlan fqp = null;

    private String innerJoinCondition, outerJoinCondition;

    public void setTargetTreeNode(TargetTreeNode targetNode) {
        this.targetTreeNode = targetNode;
    }

    public Object createQueryAsObject() throws QueryWizardException {
         fqp = new CaBIGFederatedQueryPlan(FQP_NAME_ATTRIBUTE);
                fqp.setTarget(targetTreeNode.getTargetClassname(), targetTreeNode.getGsh().getURL());
                /** if tree node values are set **/
                if (targetNode != null) {
                    /** If target node has attributes add attributes **/
                    if (!targetNode.getAttributes().isEmpty()) {
                        CaBIGNestedCriteria nestedCriteria = new CaBIGNestedCriteria(targetTreeNode.getTargetClassname());
                        QueryUtility.nestAttributes(nestedCriteria, targetNode.getAttributes());
                        fqp.addCaBIGNestedCriteria(nestedCriteria);
                    }
                    /** And then add associations to nested criteria node **/
                    FQPUtility.nestAssociations(fqp, targetNode.getAssociations());

                    /** Set the join condition and nest any foreign associations **/
                    String joinCondition = innerJoinCondition + "=" + outerJoinCondition;

                    //hack for demo in case join condition not checked
                    if(joinCondition.indexOf("null")>-1)
                    joinCondition = "gov.nih.nci.cabio.domain.Gene.symbol=edu.georgetown.pir.domain.Gene.name";
                    
                    FQPUtility.nestForeignAssociations(fqp, targetNode.getForeignAssociations(),joinCondition);

                }

        return fqp;
    }

    public String createQuery() throws QueryWizardException {
      /** Check if FQP has not been created yet **/
       //if(fqp==null)
        createQueryAsObject();

        return fqp.toString();
    }

    public Observer getTreeObserver() {
        return new Observer() {

            /**
             * This method is called whenever the observed object is changed. An
             * application calls an <tt>Observable</tt> object's
             * <code>notifyObservers</code> method to have all the object's
             * observers notified of the change.
             *
             * @param o   the observable object.
             * @param arg an argument passed to the <code>notifyObservers</code>
             *            method.
             */

            public void update(Observable o, Object arg) {
                ValueTreeNode valueNode = (ValueTreeNode) o;

                if(valueNode.isJoinCondition()){
                    if(TreeUtil.valueBelongsToForeignAssociation(valueNode)){
                        outerJoinCondition = TreeUtil.getCDEString(valueNode);
                    }
                    else if (TreeUtil.valueBelongsToAssociation(valueNode)){
                        innerJoinCondition = TreeUtil.getCDEString(valueNode);
                    }
               }
                if (valueNode.getValueField().length() > 0)
                    targetNode.addValueTreeNode(valueNode);


            }
        };

    }
}

