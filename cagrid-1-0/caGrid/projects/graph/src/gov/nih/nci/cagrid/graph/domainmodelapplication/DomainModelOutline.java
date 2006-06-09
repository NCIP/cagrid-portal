package gov.nih.nci.cagrid.graph.domainmodelapplication;

import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

public class DomainModelOutline extends JPanel
{
	public DomainModelOutlineToolBar toolBar;
	public DomainModelOutlineClippedTreePane    treePane;
	public DomainModelOutlineTree				tree;
	
	public static int toolBarHeight = 30;
	
	public DomainModelOutline()
	{
	
		this.setLayout(null);
	
		tree = new DomainModelOutlineTree();
		treePane = new DomainModelOutlineClippedTreePane();
		treePane.setViewportView(tree);
		toolBar = new DomainModelOutlineToolBar();
		
		this.add(treePane);
		this.add(toolBar);
		
		
		
		this.addComponentListener(new DomainModelOutlineComponentListener());
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
