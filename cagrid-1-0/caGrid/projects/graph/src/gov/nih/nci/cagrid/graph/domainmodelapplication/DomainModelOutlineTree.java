package gov.nih.nci.cagrid.graph.domainmodelapplication;

import java.awt.Component;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class DomainModelOutlineTree extends JTree
{
	
	
	public DomainModelOutline parent;
	
	public boolean expanded = false;
	
	public DomainModelTreeRenderer defaultRenderer = new DomainModelTreeRenderer();
	public NullRenderer nullRenderer = new NullRenderer();
	
	
	public DomainModelOutlineTree(DomainModelOutline p)
	{
		super();
		
		this.parent = p;
		
		this.setBorder(BorderFactory.createEmptyBorder(2, 2, 4, 4));
		
		this.setCellRenderer(new DomainModelTreeRenderer());
		
		this.addTreeSelectionListener(new DomainModelTreeSelectionListener());
	
		this.setShowsRootHandles(true);

	}
	

	public void setNullRenderer()
	{
		this.setCellRenderer(this.nullRenderer);
	}
	
	public void setDefaultRenderer()
	{
		this.setCellRenderer(this.defaultRenderer);
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
		this.expanded = true;
		expandAll(this, true);
	}
	
	public void collapseAll()
	{
		this.expanded = false;
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

class NullRenderer extends DefaultTreeCellRenderer
{
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

	setIcon(null);
	setFont(new Font("verdana", Font.BOLD, 11));
	
	return this;
    }
}

class DomainModelTreeSelectionListener implements TreeSelectionListener
{
	public void valueChanged(TreeSelectionEvent e)
	{
		
		DomainModelOutlineTree tree = (DomainModelOutlineTree) e.getSource();
	
		if(!tree.parent.hasNullDomainModel)
		{
			DomainModelTreeNode node = (DomainModelTreeNode) tree.getLastSelectedPathComponent();
			
			if(node != null)
			{
				if(node.type == DomainModelTreeNode.CLASS)
				{
					tree.parent.parent.showPage(node, node.pkgName);
				}
				else
				{
					tree.parent.parent.showPage(node, node.name);
				}
			}
		}
	}
}

class DomainModelTreeRenderer extends DefaultTreeCellRenderer {
    
	public ImageIcon classIcon = new ImageIcon(System.getProperty("user.dir") + "\\resource\\classes1.png");
	public ImageIcon packageIcon = new ImageIcon(System.getProperty("user.dir") + "\\resource\\package.png");
	public ImageIcon rootIcon = null; //new ImageIcon(System.getProperty("user.dir") + "\\resource\\package.png");

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
