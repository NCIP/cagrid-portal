/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.tree;

import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface TreeNodeListener {
	void onOpen(TreeNode node, Map params);
	void onClose(TreeNode node, Map params);
}
