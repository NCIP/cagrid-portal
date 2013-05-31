/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.portlet.query.QueryConstants;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNodeListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class CQLQueryTreeNodeListener implements TreeNodeListener {

    private static final Log logger = LogFactory.getLog(CQLQueryTreeNodeListener.class);

    /**
     *
     */
    public CQLQueryTreeNodeListener() {

    }

    /*
      * (non-Javadoc)
      *
      * @see gov.nih.nci.cagrid.portal.portlet.tree.TreeNodeListener#onClose(gov.nih.nci.cagrid.portal.portlet.tree.TreeNode,
      *      java.util.Map)
      */
    public void onClose(TreeNode node, Map params) {

    }

    /*
      * (non-Javadoc)
      *
      * @see gov.nih.nci.cagrid.portal.portlet.tree.TreeNodeListener#onOpen(gov.nih.nci.cagrid.portal.portlet.tree.TreeNode,
      *      java.util.Map)
      */
    public void onOpen(TreeNode node, Map params) {

        if (!(node.getContent() instanceof CriteriaBean)) {
            throw new IllegalArgumentException(
                    "Expected instance of CriteriaBean. Got "
                            + (node.getContent() == null ? null : node
                            .getContent().getClass().getName()));
        }

        CriteriaBean parent = (CriteriaBean) node.getContent();
        for (AssociationBean assoc : parent.getAssociations()) {
            CriteriaBean child = null;
            String _label = assoc.getRoleName();

            if (assoc.getRoleName().indexOf(QueryConstants.FOREIGN_UML_CLASS_PREFIX) > -1) {
                _label = assoc.getCriteriaBean().getUmlClass().getClassName();
            }

            child = assoc.getCriteriaBean();
            TreeNode childNode = new TreeNode(node, assoc.getRoleName());
            int idx = node.getChildren().indexOf(childNode);
            if (idx == -1) {
                logger.debug("Adding new child " + childNode.getPath());
                childNode.setLabel(_label);
                node.getChildren().add(childNode);
            } else {
                logger.debug("Updating existing child " + childNode.getPath());
                childNode = (TreeNode) node.getChildren().get(idx);
            }
            childNode.setContent(child);
        }
    }

}
