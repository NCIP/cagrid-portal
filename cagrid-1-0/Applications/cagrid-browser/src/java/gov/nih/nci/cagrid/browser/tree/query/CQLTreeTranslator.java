package gov.nih.nci.cagrid.browser.tree.query;

import gov.nih.nci.cagrid.browser.tree.exception.QueryWizardException;
import gov.nih.nci.cagrid.browser.tree.node.TargetTreeNode;
import gov.nih.nci.cagrid.browser.tree.node.ValueTreeNode;


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

    public Object createQueryAsObject() throws QueryWizardException {
        return null;
    }

    /**
     * Construct CQL Query
     *
     * @return CQL Query string
     */
    public String createQuery() throws QueryWizardException {
	return null;
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
