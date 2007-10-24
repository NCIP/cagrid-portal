/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeNodeListener;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
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
			int idx = node.getChildren().indexOf(childNode);
			if(idx == -1){
				logger.debug("Adding new child " + childNode.getPath());
				childNode.setLabel(assoc.getRoleName());
				node.getChildren().add(childNode);	
			}else{
				logger.debug("Updating existing child " + childNode.getPath());
				childNode = (TreeNode) node.getChildren().get(idx);
			}
			childNode.setContent(child);
		}
	}

}
