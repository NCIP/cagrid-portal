package gov.nih.nci.cagrid.browser.tree.query;

import gov.nih.nci.cagrid.browser.tree.exception.QueryWizardException;
import gov.nih.nci.cagrid.browser.tree.node.TargetTreeNode;
import gov.nih.nci.cagrid.browser.tree.node.ValueTreeNode;
import gov.nih.nci.cagrid.browser.tree.util.QueryUtility;
import gov.nih.nci.cagrid.data.client.query.CaBIGNestedCriteria;
import gov.nih.nci.cagrid.data.client.query.CaBIGXMLQuery;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 22, 2006
 * Time: 5:12:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class CQLTreeTranslator implements TreeTranslator {

    private TargetNode targetNode = new TargetNode();
    private TargetTreeNode targetTreeNode;
    private CaBIGXMLQuery xmlQuery = null;


    public Object createQueryAsObject() throws QueryWizardException {
                 this.xmlQuery = new CaBIGXMLQuery("QueryWizard");
                xmlQuery.setTarget(targetTreeNode.getTargetClassname());

                /** if tree node values are set **/
                if (targetNode != null) {
                    /** If target node has attributes add attributes **/
                    if (!targetNode.getAttributes().isEmpty()) {
                        CaBIGNestedCriteria nestedCriteria = new CaBIGNestedCriteria(targetTreeNode.getTargetClassname());
                        QueryUtility.nestAttributes(nestedCriteria, targetNode.getAttributes());
                        xmlQuery.addCaBIGNestedCriteria(nestedCriteria);
                    }
                    /** And then add associations to nested criteria node **/

                    QueryUtility.nestAssociations(xmlQuery, targetNode.getAssociations());
                }
                //** clean up
                targetNode = new TargetNode();

        return this.xmlQuery;
    }

    /**
     * Construct CQL Query
     *
     * @return CQL Query string
     */
    public String createQuery() throws QueryWizardException {

        if(xmlQuery == null)
            createQueryAsObject();

        return xmlQuery.toString();  //To change body of implemented methods use File | Settings | File Templates.

    }

    public Observer getTreeObserver() {
        return new CQLTreeObserver();
    }

    public void setTargetTreeNode(TargetTreeNode targetTreeNode) {
        this.targetTreeNode = targetTreeNode;
    }


    private class CQLTreeObserver implements Observer {
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
            if (valueNode.getValueField().length() > 0)
                targetNode.addValueTreeNode(valueNode);

        }
    }


}
