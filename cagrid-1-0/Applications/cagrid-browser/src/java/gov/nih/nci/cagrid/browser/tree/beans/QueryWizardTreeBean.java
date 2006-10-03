package gov.nih.nci.cagrid.browser.tree.beans;

import gov.nih.nci.cagrid.browser.beans.GSH;
import gov.nih.nci.cagrid.browser.tree.exception.QueryWizardException;
import gov.nih.nci.cagrid.browser.tree.node.TargetTreeNode;
import gov.nih.nci.cagrid.browser.tree.query.CQLTreeTranslator;
import gov.nih.nci.cagrid.browser.tree.util.TreeModelLoader;
import gov.nih.nci.cagrid.browser.tree.util.TreeUtil;
import gov.nih.nci.cagrid.common.servicedata.DomainModelType;
import gov.nih.nci.cagrid.common.servicedata.DomainObjectType;
import gov.nih.nci.cagrid.data.federatedQuery.activity.impl.FederatedQueryActivityImpl;
import gov.nih.nci.cagrid.data.federatedQuery.client.CaBIGFederatedQueryPlan;
import gov.nih.nci.cagrid.data.federatedQuery.engine.FederatedQueryEngine;
import org.apache.myfaces.custom.tree2.*;
import org.w3c.dom.Document;
import uk.org.ogsadai.common.XMLUtilities;
import uk.org.ogsadai.common.exception.common.CommonSystemException;

import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 20, 2006
 * Time: 9:43:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class QueryWizardTreeBean implements Serializable {

    private TreeModelLoader modelLoader;
    private gov.nih.nci.cagrid.browser.tree.query.TreeTranslator queryFactory;
    private TreeModel cqlTree;
    private TreeModel fqpTree;
    private TreeNode queryTree;
    private String fqpd;
    private String queryResult;


    public QueryWizardTreeBean() {
    }

    public TreeNode getQueryTree() {
        return queryTree;
    }

    public void setQueryTree(boolean loadForeignAssociation) {
        DomainModelType targetModel = this.modelLoader.getTargetModel();
        GSH targetGSH = this.modelLoader.getTargetGSH();

        /** Target Object of the Tree **/
        DomainObjectType targetObject = this.modelLoader.getTargetObject();
        TargetTreeNode targetNode = new TargetTreeNode(targetGSH, targetObject);

        //@todo throw proper exceptions if not properly configured
        targetNode.setTreeObserver(queryFactory.getTreeObserver());
        queryFactory.setTargetTreeNode(targetNode);

        TreeUtil.loadAttributes(targetNode, targetObject);
        TreeUtil.loadAssociations(targetNode, targetObject, targetModel);

        if (loadForeignAssociation) {
            /** Load External associations **/
            modelLoader.discoverForeignServices(targetNode);
            //todo change method name and move to Util class
        }

        this.queryTree = targetNode;
    }


    public TreeModel getFqpTree() {
        this.fqpTree = new TreeModelBase(getQueryTree());
        return fqpTree;
    }

    public void setFqpTree(TreeModel fqpTree) {
        this.fqpTree = fqpTree;
    }

    public TreeModel getCqlTree() {
        TreeNode targetNode = getQueryTree();
        this.cqlTree = new TreeModelBase(targetNode);
        TreeState expandRootState = new TreeStateBase();
        if (!expandRootState.isNodeExpanded(targetNode.getIdentifier()))
            expandRootState.toggleExpanded(targetNode.getIdentifier());

        cqlTree.setTreeState(expandRootState);
        return cqlTree;
    }

    public void setCqlTree(TreeModel cqlTree) {
        this.cqlTree = cqlTree;
    }


    public gov.nih.nci.cagrid.browser.tree.query.TreeTranslator getQueryFactory() {
        return queryFactory;
    }

    public void setQueryFactory(gov.nih.nci.cagrid.browser.tree.query.TreeTranslator queryFactory) {
        this.queryFactory = queryFactory;
    }

    public TreeModelLoader getModelLoader() {
        return modelLoader;
    }

    public void setModelLoader(TreeModelLoader modelLoader) {
        this.modelLoader = modelLoader;
    }

    public String createQuery() {
        String xmlQuery = null;
        try {
            xmlQuery = queryFactory.createQuery();
        } catch (QueryWizardException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "failed";
        }
        this.modelLoader.getTargetGSH().setCaBIGXMLQuery(xmlQuery);

        return "success";
    }

    /** FQP Wizard calls this method to
     * move forward to the query submission page
     * @return
     */
    public String createFQP(){
        try {
            this.fqpd = queryFactory.createQuery();
        } catch (QueryWizardException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "failed";
        }
        return "success";
    }

    public String submitFQPD(){
        try {
            CaBIGFederatedQueryPlan queryPlan =  (CaBIGFederatedQueryPlan)queryFactory.createQueryAsObject();
          System.out.println(queryPlan.toString());
            FederatedQueryActivityImpl queryActivity = new FederatedQueryActivityImpl(queryPlan);

            FederatedQueryEngine engine = new FederatedQueryEngine();
            Document resultDoc = engine.process(queryActivity);
            queryResult = XMLUtilities.xmlDOMToString(resultDoc);
        } catch (QueryWizardException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "failed";
        } catch (CommonSystemException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.\
            return "failed";
        }

        return "success";
    }

    public String createCQLQueryWizard() {
        gov.nih.nci.cagrid.browser.tree.query.TreeTranslator queryFactory = new CQLTreeTranslator();
        this.setQueryFactory(queryFactory);
        setQueryTree(false);
        return "success";
    }

    public String createFQPQueryWizard() {
        gov.nih.nci.cagrid.browser.tree.query.TreeTranslator queryFactory = new FQPTreeTranslator();
        this.setQueryFactory(queryFactory);
        this.setQueryTree(true);
        return "success";
    }

    public void setJoinCondition(ValueChangeEvent event){
        Object obj = event.getNewValue();
    }

    public String getFqpd() {
        return fqpd;
    }

    public void setFqpd(String fqpd) {
        this.fqpd = fqpd;
    }

    public String getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(String queryResult) {
        this.queryResult = queryResult;
    }
}


