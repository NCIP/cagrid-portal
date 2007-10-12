/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	private List<TreeNodeListener> listeners = new ArrayList<TreeNodeListener>();

	/**
	 * 
	 */
	public TreeFacade() {

	}

	public String openNode(String path, Map params) {
		return changeNodeState(NodeState.OPEN, path, params);
	}

	public String closeNode(String path, Map params) {
		return changeNodeState(NodeState.CLOSED, path, params);
	}

	private String changeNodeState(NodeState state, String path, Map params) {
		String out = null;
		TreeNode node = getRootNode().find(path);
		if (node != null) {
			node.setState(state);
			for (TreeNodeListener l : getListeners()) {
				if (NodeState.OPEN == state) {
					l.onOpen(node, params);
				} else {
					l.onClose(node, params);
				}
			}
			String render = (String)params.get("render");
			if (render == null || "true".equals(render)) {
				out = getRenderer().render(node, params);
			}
		} else {
			throw new RuntimeException("Didn't find node '" + path + "'.");
		}
		return out;
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

	public List<TreeNodeListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<TreeNodeListener> listeners) {
		this.listeners = listeners;
	}

}
