/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import java.util.Map;

import gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeNodeListener;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CQLQueryTreeNodeListener implements TreeNodeListener {

	/**
	 * 
	 */
	public CQLQueryTreeNodeListener() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.portlet.tree.TreeNodeListener#onClose(gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode,
	 *      java.util.Map)
	 */
	public void onClose(TreeNode node, Map params) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.portlet.tree.TreeNodeListener#onOpen(gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode,
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
		for(AssociationBean assoc : parent.getAssociations()){
			CriteriaBean child = assoc.getCriteriaBean();
			TreeNode childNode = new TreeNode(node, assoc.getRoleName());
			childNode.setLabel(assoc.getRoleName());
			node.getChildren().add(childNode);
			childNode.setContent(child);
		}
	}

}
