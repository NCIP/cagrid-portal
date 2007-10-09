/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.tree;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class TreeFacade {

	private static final Log logger = LogFactory.getLog(TreeFacade.class);
	
	private TreeNodeRenderer renderer;
	private TreeNode rootNode;
	
	/**
	 * 
	 */
	public TreeFacade() {
		
	}
	
	public String openNode(String path){
		TreeNode node = getRootNode().find(path);
		if(node != null){
			logger.debug("Got node '" + path + "'");
			node.setState(NodeState.OPEN);
		}else{
			logger.debug("Didn't find node '" + path + "'");
		}
		return getRenderer().render(node);
	}
	
	public void closeNode(String path){
		TreeNode node = getRootNode().find(path);
		if(node != null){
			node.setState(NodeState.CLOSED);
		}
	}
	

	public TreeNodeRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(TreeNodeRenderer renderer) {
		this.renderer = renderer;
	}

	public TreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(TreeNode rootNode) {
		this.rootNode = rootNode;
	}

}
