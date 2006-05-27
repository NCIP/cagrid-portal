package gov.nih.nci.cagrid.graph.vstheme;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;

public class InvertedMDIPanel {
	
	public Vector pages;
	public Vector pageIcons;
	public Vector pageTitles;
	
	public MultipleComponentContainer container;
	public InvertedTabsPane tabs;
	
	public void addPage(JComponent component, ImageIcon icon, String title)
	{
		this.pages.add(component);
		this.pageIcons.add(icon);
		this.pageTitles.add(title);
		
		this.tabs.addTab(component, title);
		
		this.setActivePage(pages.size() - 1);
		
	}
	
	public void setActivePage(int i)
	{
		this.tabs.setActiveTab(i);
		this.container.showComponent(i);
	}
	
	public void setFocus()
	{
		
	}

	public void setPage(int i , JComponent component)
	{
		
	}
	
	public int getPageCount()
	{
		return 0;
	}
	
}
