package gov.nih.nci.cagrid.graph.domainmodelapplication;

import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

public class DomainModelXMLOutline extends JPanel
{
	public DomainModelXMLOutlineToolBar toolBar;
	public DomainModelXMLOutlineClippedTreePane    treePane;
	public DomainModelXMLOutlineTree				tree;
	
	public static int toolBarHeight = 30;
	
	public DomainModelXMLOutline()
	{
	
		this.setLayout(null);
	
		tree = new DomainModelXMLOutlineTree();
		treePane = new DomainModelXMLOutlineClippedTreePane();
		treePane.setViewportView(tree);
		toolBar = new DomainModelXMLOutlineToolBar();
		
		this.add(treePane);
		this.add(toolBar);
		
		
		
		this.addComponentListener(new DomainModelXMLOutlineComponentListener());
	}
	
	public void resizeChildren()
	{
		toolBar.setBounds(0, 0, getWidth(), toolBarHeight);
		treePane.setBounds(0, toolBarHeight + 1, getWidth(), getHeight() - toolBarHeight - 1);
		this.validate();
	}
	
	
}

class DomainModelXMLOutlineComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		DomainModelXMLOutline s = (DomainModelXMLOutline) e.getSource();
		
		s.resizeChildren();
	}
}
