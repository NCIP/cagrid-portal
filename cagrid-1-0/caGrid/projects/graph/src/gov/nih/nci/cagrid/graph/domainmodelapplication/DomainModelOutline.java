package gov.nih.nci.cagrid.graph.domainmodelapplication;

import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class DomainModelOutline extends JPanel
{
	public DomainModelOutlineToolBar toolBar;
	public DomainModelOutlineClippedTreePane    treePane;
	public DomainModelOutlineTree				tree;
	
	public DomainModelExplorer parent;
	
	public boolean hasNullDomainModel = true;
	
	public static int toolBarHeight = 28;
	
	public DomainModelOutline(DomainModelExplorer parent)
	{
	
		this.setLayout(null);
		
		this.parent = parent;
	
		tree = new DomainModelOutlineTree(this);
		treePane = new DomainModelOutlineClippedTreePane();
		treePane.setViewportView(tree);
		toolBar = new DomainModelOutlineToolBar(this);
		
		this.add(treePane);
		this.add(toolBar);
		
		
		
		this.addComponentListener(new DomainModelOutlineComponentListener());
	}
	
	public void setDomainModel(DomainModel model, Vector multiMap)
	{
		if(model != null)
		{
			hasNullDomainModel = false;
			
			this.tree.setDefaultRenderer();
			
			this.tree.removeAll();
			
			DomainModelTreeNode root = new DomainModelTreeNode(model);
			root.name = model.getProjectLongName() + " " + model.getProjectVersion();
			root.type = DomainModelTreeNode.DOMAIN;
			root.setUserObject(root.name);
			
			for(int k = 0; k < multiMap.size(); k++	 )
			{
				DomainModelTreeNode pkg = new DomainModelTreeNode(model);
				MultiMapElement e = (MultiMapElement) multiMap.get(k);
				
				pkg.name = e.head;
				pkg.type = DomainModelTreeNode.PACKAGE;
				pkg.setUserObject(pkg.name);
				
				root.add(pkg);
				
				for(int j = 0; j < e.list.size(); j++)
				{
					DomainModelTreeNode cls = new DomainModelTreeNode(model);
					String cname = (String) e.list.get(j);
					
					cls.name = cname;
					pkg.type = DomainModelTreeNode.CLASS;
					pkg.setUserObject(cname);
					
					pkg.add(cls);
				}
			}
			
			DefaultTreeModel tmodel = (DefaultTreeModel) this.tree.getModel();
			tmodel.setRoot(root);
		}
		else
		{
			this.tree.setNullRenderer();
			hasNullDomainModel = true;
			
			DefaultMutableTreeNode n = new DefaultMutableTreeNode("  No Domain Model selected");
			DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
			treeModel.setRoot(n);
		}
	}
	
	public void resizeChildren()
	{
		toolBar.setBounds(0, 0, getWidth(), toolBarHeight);
		treePane.setBounds(0, toolBarHeight + 1, getWidth(), getHeight() - toolBarHeight - 1);
		this.validate();
	}
	
	
}

class DomainModelOutlineComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		DomainModelOutline s = (DomainModelOutline) e.getSource();
		
		s.resizeChildren();
	}
}
