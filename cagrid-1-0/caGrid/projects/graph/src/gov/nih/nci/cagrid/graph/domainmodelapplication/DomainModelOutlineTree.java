package gov.nih.nci.cagrid.graph.domainmodelapplication;

import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.awt.Component;
import java.awt.Font;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class DomainModelOutlineTree extends JTree
{
	
	
	public DomainModelOutline parent;
	
	public boolean expanded = false;
	
	
	public DomainModelOutlineTree(DomainModelOutline p)
	{
		super();
		
		this.parent = p;
		
		this.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
		
		this.setCellRenderer(new DomainModelTreeRenderer());
		
		
	
		

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

class DomainModelTreeRenderer extends DefaultTreeCellRenderer {
    
	public ImageIcon classIcon = new ImageIcon(System.getProperty("user.dir") + "\\resource\\classes1.png");
	public ImageIcon packageIcon = new ImageIcon(System.getProperty("user.dir") + "\\resource\\package.png");
	public ImageIcon rootIcon = new ImageIcon(System.getProperty("user.dir") + "\\resource\\package.png");

    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
        if (leaf) {
        	this.setFont(new Font("verdana", Font.PLAIN, 11));
            setIcon(classIcon);
            setToolTipText(null);
        } 
        else if(!leaf && row == 0)
        {
            setIcon(rootIcon);
            this.setFont(new Font("verdana", Font.BOLD, 11));
        } 
        else
        {
        	this.setFont(new Font("verdana", Font.PLAIN, 11));
        	setIcon(packageIcon);
        }

        return this;
    }

   
}
