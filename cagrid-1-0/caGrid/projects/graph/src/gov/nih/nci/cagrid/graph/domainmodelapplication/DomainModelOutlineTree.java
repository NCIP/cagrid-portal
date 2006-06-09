package gov.nih.nci.cagrid.graph.domainmodelapplication;

import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class DomainModelOutlineTree extends JTree
{
	public boolean expanded = false;
	
	public DomainModelOutlineTree()
	{
		super();
		
		this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		

	}
	
	public void setDomainModel(DomainModel model)
	{
		
	}
	
	public void toggleExpansion()
	{
		if(expanded)
		{
			this.collapseAll();
			expanded = false;
		}
		else
		{
			this.expandAll();
			expanded = true;
		}
	}
	
	public void expandAll()
	{
		expandAll(this, true);
	}
	
	public void collapseAll()
	{
		expandAll(this, false);
		this.expandPath(new TreePath(this.getModel().getRoot()));
	}
	
    // If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
	
    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
    
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }
    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
    
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

}
