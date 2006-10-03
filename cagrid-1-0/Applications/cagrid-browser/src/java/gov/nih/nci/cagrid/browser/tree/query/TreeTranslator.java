package gov.nih.nci.cagrid.browser.tree.query;

import gov.nih.nci.cagrid.browser.tree.exception.QueryWizardException;
import gov.nih.nci.cagrid.browser.tree.node.TargetTreeNode;

import java.util.Observer;

/**
 * Will Translate a Query Wizard Tree into specific query types (CQL, FQP etc)
 * 
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 22, 2006
 * Time: 5:06:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TreeTranslator {

    public void setTargetTreeNode(TargetTreeNode targetNode);

    public String createQuery() throws QueryWizardException;

    public Object createQueryAsObject() throws QueryWizardException;

    public Observer getTreeObserver();
}
