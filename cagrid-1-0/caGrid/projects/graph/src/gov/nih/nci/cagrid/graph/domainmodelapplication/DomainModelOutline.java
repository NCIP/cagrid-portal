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
	
	public static int ASCENDING = 0;
	public static int DESCENDING = 1;
	public static int NONE = 2;
	
	public int currentOrder = NONE;
	
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
			root.name = model.getProjectLongName() + "  v. " + model.getProjectVersion();
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
					
					cname = trimClassName(cname);
					
					cls.name = cname;
					cls.type = DomainModelTreeNode.CLASS;
					cls.setUserObject(cname);
					
					pkg.add(cls);
				}
			}
			
			DefaultTreeModel tmodel = (DefaultTreeModel) this.tree.getModel();
			tmodel.setRoot(root);
			this.tree.expandAll();
		}
		else
		{
			hasNullDomainModel = true;
			setNullTree();
		}
	}
	
	
	public void setFilter(String filter, DomainModel model,  Vector packages)
	{
		if(model != null)
		{
			
			this.tree.setDefaultRenderer();
			this.tree.removeAll();
			
			DomainModelTreeNode root = new DomainModelTreeNode(model);
			root.name = model.getProjectLongName() + "  v. " + model.getProjectVersion();
			root.type = DomainModelTreeNode.DOMAIN;
			root.setUserObject(root.name);
			
			for(int k = 0; k < packages.size(); k ++)
			{
				DomainModelTreeNode pkg = new DomainModelTreeNode(model);
				MultiMapElement e = (MultiMapElement) packages.get(k);
				
				pkg.name = e.head;
				pkg.type = DomainModelTreeNode.PACKAGE;
				pkg.setUserObject(pkg.name);
				
				boolean atLeastOne = false;
				
				for(int j = 0; j < e.list.size(); j++)
				{
					DomainModelTreeNode cls = new DomainModelTreeNode(model);
					String cname = (String) e.list.get(j);
					
					String tcname = trimClassName(cname);
					
					cls.name = tcname;
					cls.type = DomainModelTreeNode.CLASS;
					cls.setUserObject(tcname);
					
					// 'all' filters nothing?
					if(filter != null && filter.trim().toUpperCase() != "ALL")
					{
						if(cname.trim().toUpperCase().contains(filter.trim().toUpperCase()))
						{
							pkg.add(cls);
							atLeastOne = true;
						}
					}
					else
					{
						atLeastOne = true;
						pkg.add(cls);
					}
				
				}
				
				if(atLeastOne)
				{
					root.add(pkg);
				}
			}
			
			DefaultTreeModel tmodel = (DefaultTreeModel) this.tree.getModel();
			tmodel.setRoot(root);
			this.tree.expandAll();
		}
		else
		{
			setNullTree();
		}
	}
	
	private void setNullTree()
	{
		this.tree.setNullRenderer();
		hasNullDomainModel = true;
		
		DefaultMutableTreeNode n = new DefaultMutableTreeNode("  No Domain Model specified");
		DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
		treeModel.setRoot(n);
	}
	
	public void setOrdering(int ordering, DomainModel model, Vector packages)
	{
		this.currentOrder = ordering;
		
		if(model != null)
		{
			Vector sortedPackages = insertionSortOnHead(packages);
			
			for(int k = 0; k < sortedPackages.size(); k ++)
			{
				MultiMapElement e = (MultiMapElement) sortedPackages.get(k);
				e.list = insertionSort(e.list);
			}
	
			setDomainModel(model, sortedPackages);
			
		}
		else
		{
			setNullTree();
		}
	}
	
	public void toggleOrdering(DomainModel model, Vector packages)
	{
		if(currentOrder == NONE)
		{
			this.setOrdering(ASCENDING, model, packages );
		}
		else if(currentOrder == ASCENDING)
		{
			this.setOrdering(DESCENDING, model, packages);
		}
		else if(currentOrder == DESCENDING)
		{
			this.setOrdering(NONE, model, packages);
		}
	}
	
	public void resizeChildren()
	{
		toolBar.setBounds(0, 0, getWidth(), toolBarHeight);
		treePane.setBounds(0, toolBarHeight + 1, getWidth(), getHeight() - toolBarHeight - 1);
		this.validate();
	}
	
	public static Vector insertionSort(Vector v)
	{
		int count2;
		
		for(int count1 = 1; count1 < v.size(); count1++)
		{
			String tmp = (String) v.elementAt(count1);

			for(count2 = count1; count2 > 0 && ( ((String)v.elementAt(count2 - 1)).compareTo(tmp) > 0); count2-- )
		    {	
	               v.setElementAt(v.elementAt(count2 - 1), count2); 

			}
			v.setElementAt(tmp, count2); 

		}
		return v;
	}
	
	public static Vector insertionSortOnHead(Vector v)
	{
		int count2;
		
		for(int count1 = 1; count1 < v.size(); count1++)
		{
			MultiMapElement tmp = (MultiMapElement) v.elementAt(count1);
			
			for(count2 = count1; count2 > 0 && ( ((MultiMapElement)v.elementAt(count2 - 1)).compareTo(tmp) > 0); count2-- )
		    {	
	               v.setElementAt(v.elementAt(count2 - 1), count2); 
			}
			v.setElementAt(tmp, count2); 

		}
		return v;
	}
	
	public static String trimClassName(String s)
	{
		StringBuffer rval = new StringBuffer("");
		boolean foundFirstDot = false;
		int i = s.length()-1;
		while(!foundFirstDot && i >= 0)
		{
			char ch = s.charAt(i);
			if(ch == '.')
			{
				foundFirstDot = true;
			}
			else
			{
				rval.insert(0, ch);
				i--;
			}
			
		}
		
		return rval.toString();
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
