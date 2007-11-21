/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.tree;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class TreeNode {

	private static final Log logger = LogFactory.getLog(TreeNode.class);
	
	private String name;
	
	private String label;

	private NodeState state = NodeState.CLOSED;

	private TreeNode parent;

	private List<TreeNode> children = new ArrayList<TreeNode>();

	private Object content;


	/**
	 * 
	 */
	public TreeNode() {
		this(null, null);
	}

	public TreeNode(TreeNode parent, String name) {
		setParent(parent);
		setName(name);
	}

	/**
	 * Does a breadth-first search
	 * 
	 * @param path
	 * @return
	 */
	public TreeNode find(String path) {
		TreeNode node = null;
		String myPath = getPath();
		if (path.equals(myPath)) {
			node = this;
		} else if(path.startsWith(myPath)) {
			for (TreeNode n : getChildren()) {
				node = n.find(path);
				if(node != null){
					break;
				}
			}
		}
		return node;
	}
	
	public String getPath(){
		String path = "/" + getName();
		TreeNode p = getParent();
		while(p != null){
			path = "/" + p.getName() + path;
			p = p.getParent();
		}
		return path;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public NodeState getState() {
		return state;
	}

	public void setState(NodeState state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	public String toString(){
		return getPath();
	}
	public boolean equals(Object o){
		return o instanceof TreeNode && ((TreeNode)o).toString().equals(toString());
	}
	public int hash(){
		return toString().hashCode();
	}
}
